package audio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.UnsupportedAudioFileException;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

import gui.Note;
import audio.FrequencyToNoteMap;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import java.io.File;
import java.io.IOException;

public class AmplitudeTranscriber implements Transcriber<short[]>{

    static final PitchProcessor.PitchEstimationAlgorithm algorithm = PitchProcessor.PitchEstimationAlgorithm.FFT_PITCH;
    static final int sampleRate = 44100;
    static final int bufferSize = 512; // 12ms
    static final int overlap = 256;

    // For some reason notes still not get conneceted, while they are in the threshold.... 
    static final int quietThreshold = 6;
    static final int noteLengthTreshold = 15;

    public record NoteSketch (
        int noteNum,
        int startIndex, 
        int endIndex
    ){
        //DEBUG
        @Override
        public String toString() {
            return "Note: " + noteNum + "\nStart Index: " + startIndex + "\nEnd Index: " + endIndex; 
            // return "Note: " + noteNum;
        }
    }

    public Note[] transcribe(short[] samples) {

        float[] pitches = new float[samples.length / (bufferSize - overlap)];        

        // Samples need to be converted to float and scaled down to be between -1 and 1
        float[] scaledSamples = new float[samples.length];
        for (int i = 0; i < samples.length; i++) {
            scaledSamples[i] = ((float) samples[i] / Short.MAX_VALUE);
        }

        // Convert ampitudes to pitches from 23ms long buffers with 50% overlap
        int iterator = 0;
        int pitchIndex = 0;
        for (;iterator < scaledSamples.length - bufferSize; iterator += (bufferSize - overlap)) {
            float[] samplePiece = new float[bufferSize];
            System.arraycopy(scaledSamples, iterator, samplePiece, 0, bufferSize);

            PitchDetectionResult result = algorithm.getDetector(sampleRate, samplePiece.length).getPitch(samplePiece);
            pitches[pitchIndex] = result.getPitch();
            pitchIndex++;

            // System.out.println(result.getPitch());
        }

        String debugstuff = "";
        int debugcounter = 0;
        for (float p : pitches) {
            if (debugcounter > 3000) {break;}
            debugstuff = debugstuff + ", " + debugcounter + ".: " + p;
            debugcounter++;
        }
        System.out.println(debugstuff);

        // Convert remaining smaller-than-buffer sized part, if its presetn
        
        if (iterator < (scaledSamples.length - 1)) {
            float[] samplePiece = new float[(scaledSamples.length - 1) - (iterator - 1)];
            System.arraycopy(scaledSamples, iterator, samplePiece, 0, (scaledSamples.length - 1) - (iterator - 1));
            
            PitchDetectionResult result = algorithm.getDetector(sampleRate, samplePiece.length).getPitch(samplePiece);
            pitches[pitches.length - 1] = result.getPitch();
        } 
    
        // Hertz to notes with length
        // (every pitch element other then the last represents a 23ms piece's pitch, with a 23/2 offset,
        // last does for a (full length - iterator) / 44.100)ms piece)
        // TODO: summarize the pitch-time pairs and create notes [summary, pitch-note table]

        ArrayList<NoteSketch> notesAndTimes = new ArrayList<>();
        int firstNoteInd = 0;
        Integer firstNotePitch = FrequencyToNoteMap.getNoteFromFrequency(pitches[firstNoteInd]);
        while(firstNotePitch == null && firstNoteInd < pitches.length) {
            firstNotePitch =  FrequencyToNoteMap.getNoteFromFrequency(pitches[firstNoteInd++]);
        }

        if (firstNotePitch != null) {
            System.err.println("First note index: " + firstNoteInd);

            NoteSketch firstNoteSketch = new NoteSketch(firstNotePitch.intValue(), 
                firstNoteInd, firstNoteInd);
            notesAndTimes.add(firstNoteSketch);

            //last buffer shorter, later?
            for (int i = firstNoteInd + 1; i < pitches.length - 1; i++) {
                Integer currentNote = FrequencyToNoteMap.getNoteFromFrequency(pitches[i]);
                if (currentNote == null) {
                    continue;
                }

                NoteSketch previousNoteSketch = notesAndTimes.get(notesAndTimes.size()-1);
                // if the directly previous pitch was the same, just lengthen it
                // i-3 (-1 for errors) because of 50% overlap (should be calculated by buffer / overlap rate, but idc rn)
                if (previousNoteSketch.endIndex >= i - quietThreshold && previousNoteSketch.noteNum == currentNote.intValue()) {
                    // lengthen prev sound
                    NoteSketch newValue = new NoteSketch(currentNote.intValue(), previousNoteSketch.startIndex, 
                        i);
                    notesAndTimes.set(notesAndTimes.size()-1, newValue);
                }
                // add new sound to list 
                else {
                    float startTime = (i * (bufferSize - overlap)) / (float) sampleRate;
                    float endTime = startTime + (bufferSize / (float) sampleRate);

                    NoteSketch newNote = new NoteSketch(currentNote.intValue(), i, i);
                    notesAndTimes.add(newNote);
                }
            }
        }

        //lets take out 1/2 buffer long sounds, probably fails
        // or should i connect them
        
        int oneslashtwolengthnotes = 0;
        for (int i = 0; i < notesAndTimes.size(); i++) {
            if (notesAndTimes.get(i).endIndex - notesAndTimes.get(i).startIndex  < noteLengthTreshold) {
                notesAndTimes.remove(i);
                i--;
                oneslashtwolengthnotes++;
            }
        }
        System.out.println("Too short notes count: " + oneslashtwolengthnotes);
        
        

        //DEBUG
        for (int i = 0; i < Math.min(200, notesAndTimes.size() - 1); i++) {
            System.err.println(notesAndTimes.get(i));
        }
        System.err.println("Note count: " + notesAndTimes.size());

        //create Note list
        Note[] retVal = new Note[notesAndTimes.size()];
        for (int i = 0; i < notesAndTimes.size(); i++) {
            Note note = new Note(notesAndTimes.get(i).noteNum, "8th"); //%20 for debug
            retVal[i] = note;
        }

        return retVal;
    }

    //DEBUG:
    private static short[] generateTestSignal() {
        int sampleRate = 44100; // 44.1 kHz
        double frequency = 440.0; // A4 pitch
        int durationSeconds = 3; // 3 seconds
        int totalSamples = sampleRate * durationSeconds;
    
        short[] signal = new short[totalSamples];
        double amplitude = 32767; // Maximum amplitude for short
    
        for (int i = 0; i < totalSamples / 3; i++) {
            // Generate sine wave signal
            signal[i] = (short) (amplitude * Math.sin(2 * Math.PI * frequency * i / sampleRate));
        }
        for (int i = totalSamples / 3 + 1; i < totalSamples / 3 + 44100; i++) {
            // Generate sine wave signal
            signal[i] = (short) 0;
        }
        for (int i = totalSamples / 3 + 44101; i < totalSamples; i++) {
            // Generate sine wave signal
            signal[i] = (short) (amplitude * Math.sin(2 * Math.PI * frequency * (740. / 440.) * i / sampleRate));
        }
        
        System.out.println("SIgnals length: " + signal.length);
    
        return signal;
    }
    

    public static void main(String[] args) {
        /* 
        short[] testSignal = generateTestSignal();
        System.out.println("TestSignal legnth: " + testSignal.length);
        // for (short signal : testSignal) {System.err.println(signal);}
        AmplitudeTranscriber a = new AmplitudeTranscriber();
        a.transcribe(testSignal);
        */

        String filePath = "C:/Users/rajcs/Downloads/ms (1).wav"; // Replace with your MP3 file path

        try 
        {
            short[] amplitudes = loadAmplitudesFromMP3(filePath);
            //short[] amplitudes = generateTestSignal();
            System.out.println("Loaded amplitudes array of size: " + amplitudes.length);

            for (short a : amplitudes) {
                // System.err.println(a);
            }

            AmplitudeTranscriber a = new AmplitudeTranscriber();
            a.transcribe(amplitudes);
        }
          
        catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }

    }

        public static short[] loadAmplitudesFromMP3(String filePath) throws IOException, UnsupportedAudioFileException {
        File mp3File = new File(filePath);
        System.err.println("asd");

        // Convert MP3 to raw audio
        var dispatcher = AudioDispatcherFactory.fromFile(mp3File, 1024, 0);
        System.err.println("asd2");

        // Store amplitude data
        ArrayList<Short> amplitudeList = new ArrayList<>();

        // Add a processor to read amplitudes from the audio stream
        dispatcher.addAudioProcessor(new AudioProcessor() {
            @Override
            public boolean process(AudioEvent audioEvent) {
                float[] floatBuffer = audioEvent.getFloatBuffer(); // Normalized amplitudes (-1 to 1)
                for (float sample : floatBuffer) {
                    // Convert normalized float (-1 to 1) to short (-32768 to 32767)
                    amplitudeList.add((short) (sample * Short.MAX_VALUE));
                }
                return true;
            }

            @Override
            public void processingFinished() {
                // Processing complete
            }
        });

        // Run the dispatcher (blocking operation)
        dispatcher.run();

        // Convert ArrayList to short[]
        short[] amplitudes = new short[amplitudeList.size()];
        for (int i = 0; i < amplitudeList.size(); i++) {
            amplitudes[i] = amplitudeList.get(i);
        }

        return amplitudes;
    }
}
    ////////////////////

