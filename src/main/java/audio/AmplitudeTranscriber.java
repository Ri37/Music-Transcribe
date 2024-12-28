package audio;

import java.util.ArrayList;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import gui.Note;

public class AmplitudeTranscriber implements Transcriber<short[]>{

    static final PitchProcessor.PitchEstimationAlgorithm algorithm = PitchProcessor.PitchEstimationAlgorithm.FFT_PITCH;
    static final int sampleRate = 44100;
    static final int bufferSize = 512;
    static final int overlap = 256;
    static final float bufferMs = (float) bufferSize / (float) sampleRate;

    static final int quietThreshold = 6;
    static final int noteLengthTreshold = 3;

    public record NoteSketch (
        int noteNum,
        int startIndex, 
        int endIndex
    ){}

    public Note[] transcribe(short[] samples) {

        float[] pitches = new float[samples.length / (bufferSize - overlap)];        

        // samples need to be converted to float and scaled down to be between -1 and 1
        float[] scaledSamples = new float[samples.length];
        for (int i = 0; i < samples.length; i++) {
            scaledSamples[i] = ((float) samples[i] / Short.MAX_VALUE);
        }

        // convert ampitudes to pitches from ~12ms long buffers with 50% overlap
        int iterator = 0;
        int pitchIndex = 0;
        for (;iterator < scaledSamples.length - bufferSize; iterator += (bufferSize - overlap)) {
            float[] samplePiece = new float[bufferSize];
            System.arraycopy(scaledSamples, iterator, samplePiece, 0, bufferSize);

            PitchDetectionResult result = algorithm.getDetector(sampleRate, samplePiece.length).getPitch(samplePiece);
            pitches[pitchIndex] = result.getPitch();
            pitchIndex++;
        }
    
        // Hertz to notes with length

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

            for (int i = firstNoteInd + 1; i < pitches.length - 1; i++) {
                Integer currentNote = FrequencyToNoteMap.getNoteFromFrequency(pitches[i]);
                if (currentNote == null) {
                    continue;
                }

                NoteSketch previousNoteSketch = notesAndTimes.get(notesAndTimes.size()-1);
                // if the directly previous pitch was the same, just lengthen it
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

        //lets take out short sounds, probably fails      
        int oneslashtwolengthnotes = 0;
        for (int i = 0; i < notesAndTimes.size(); i++) {
            if (notesAndTimes.get(i).endIndex - notesAndTimes.get(i).startIndex  < noteLengthTreshold) {
                notesAndTimes.remove(i);
                i--;
                oneslashtwolengthnotes++;
            }
        }
        System.err.println("Too short notes count: " + oneslashtwolengthnotes);

        //create Note list
        Note[] retVal = new Note[notesAndTimes.size()];
        for (int i = 0; i < notesAndTimes.size(); i++) {
            NoteSketch ns = notesAndTimes.get(i);
            // length for 60 bpm
            float length = (ns.endIndex - ns.startIndex) * bufferMs;
            System.out.println("bufferms: " + bufferMs);
            System.out.println("l: " + length);
            String noteLength = "";
            if (length <= 0.25) {
                noteLength = "16th";
            } else if (length <= 0.5) {
                noteLength = "8th";
            } else if (length <= 1.0) {
                noteLength = "quarter";
            } else if (length <= 2.0) {
                noteLength = "half";
            } else {
                noteLength = "full";
            }

            float startTime = ns.startIndex * (bufferMs - ((float) overlap / (float) sampleRate));
            float endTime = ns.endIndex * (bufferMs - ((float) overlap / (float) sampleRate));

            Note note = new Note(ns.noteNum, noteLength, startTime, endTime);
            System.out.println("note: " + ns.noteNum + " len: " + noteLength + " start: " + startTime + "end: " + endTime);
            retVal[i] = note;
        }

        return retVal;
    }
}

