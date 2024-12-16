package audio;

import java.util.ArrayList;

import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

import gui.Note;
import audio.FrequencyToNoteMap;

public class AmplitudeTranscriber implements Transcriber<short[]>{

    static PitchProcessor.PitchEstimationAlgorithm algorithm = PitchProcessor.PitchEstimationAlgorithm.YIN;
    static int sampleRate = 44100;
    static int bufferSize = 1024;
    static int overlap = 512;
    static float bufferLengthMs = (float) bufferSize / sampleRate;
    static float overlapMs = (float) overlap / sampleRate;

    public record NoteSketch (
        int noteNum,
        float startTime, 
        float endTime,
        int pitchIndex
    ){
        //DEBUG
        @Override
        public String toString() {
            return "Note: " + noteNum + "\nStart Time: " + startTime + "\nEnd Time: " + endTime + "\nPitchindex: " + pitchIndex; 
        }
    }

    public Note[] transcribe(short[] samples) {

        float[] pitches = new float[samples.length / (bufferSize - overlap)];        

        // Samples need to be converted to float and scaled down to be between -1 and 1
        float[] scaledSamples = new float[samples.length];
        for (int i = 0; i < samples.length; i++) {
            scaledSamples[i] = ((float) samples[i] / Short.MAX_VALUE);
            System.out.println(scaledSamples[i]);
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
        }

        // Convert remaining smaller-than-buffer sized part, if its presetn
        
        if (iterator < (scaledSamples.length - 1)) {
            float[] samplePiece = new float[(scaledSamples.length - 1) - (iterator - 1)];
            System.arraycopy(scaledSamples, iterator, samplePiece, 0, (scaledSamples.length - 1) - (iterator - 1));
            
            PitchDetectionResult result = algorithm.getDetector(sampleRate, samplePiece.length).getPitch(samplePiece);
            pitches[pitches.length - 1] = result.getPitch();
        } 
        
        //DEBUG:
        for(float pitch : pitches) {
            System.out.println(pitch);
        }
        ////////////////


        // Hertz to notes with length
        // (every pitch element other then the last represents a 23ms piece's pitch, with a 23/2 offset,
        // last does for a (full length - iterator) / 44.100)ms piece)
        // TODO: summarize the pitch-time pairs and create notes [summary, pitch-note table]

        ArrayList<NoteSketch> notesAndTimes = new ArrayList<>();
        int firstNoteInd = 0;
        Integer firstNotePitch = FrequencyToNoteMap.getNoteFromFrequency(pitches[firstNoteInd]);
        while(firstNotePitch == null) {
            if (firstNoteInd == pitches.length) {
                break;
            }
            firstNotePitch =  FrequencyToNoteMap.getNoteFromFrequency(pitches[firstNoteInd++]);
        }

        if (firstNotePitch != null) {
            NoteSketch firstNoteSketch = new NoteSketch(firstNotePitch.intValue(), 
                firstNoteInd * (bufferLengthMs - overlapMs), firstNoteInd * (bufferLengthMs - overlapMs) + bufferLengthMs,
                firstNoteInd);
            notesAndTimes.add(firstNoteSketch);

            //last buffer shorter, later?
            for (int i = firstNoteInd + 1; i < pitches.length - 1; i++) {
                Integer currentNote = FrequencyToNoteMap.getNoteFromFrequency(pitches[i]);
                if (currentNote == null) {
                    continue;
                }

                NoteSketch previousNoteSketch = notesAndTimes.get(notesAndTimes.size()-1);
                // if the directly previous pitch was the same, just lengthen it
                if ( previousNoteSketch.pitchIndex == i - 1 && previousNoteSketch.noteNum == currentNote.intValue()) {
                    // lengthen prev sound 
                    NoteSketch newValue = new NoteSketch(currentNote.intValue(), previousNoteSketch.startTime, 
                        previousNoteSketch.endTime + bufferLengthMs, i);
                    notesAndTimes.set(notesAndTimes.size()-1, newValue);
                }
                // add new sound to list 
                else {
                    NoteSketch newNote = new NoteSketch(currentNote.intValue(), i * (bufferLengthMs - overlapMs), 
                        i * (bufferLengthMs - overlapMs) + bufferLengthMs, i);
                    notesAndTimes.add(newNote);
                }
            }
        }

        //DEBUG
        for (NoteSketch n : notesAndTimes) {
            System.err.println(n);
        }

        //valamiért megduplázódik a hossz
        //create Note list


        return new Note[0];
    }

    //DEBUG:
    private static short[] generateTestSignal() {
        int sampleRate = 44100; // 44.1 kHz
        double frequency = 440.0; // A4 pitch
        int durationSeconds = 1; // 2 seconds
        int totalSamples = sampleRate * durationSeconds;
    
        short[] signal = new short[totalSamples];
        double amplitude = 32767; // Maximum amplitude for short
    
        for (int i = 0; i < totalSamples; i++) {
            // Generate sine wave signal
            signal[i] = (short) (amplitude * Math.sin(2 * Math.PI * frequency * i / sampleRate));
        }
    
        return signal;
    }
    

    public static void main(String[] args) {
        short[] testSignal = generateTestSignal();
        // for (short signal : testSignal) {System.err.println(signal);}
        AmplitudeTranscriber a = new AmplitudeTranscriber();
        a.transcribe(testSignal);
    }
    ////////////////////
}
