package audio;

import be.tarsos.dsp.pitch.AMDF;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import gui.Note;

public class AmplitudeTranscriber implements Transcriber<short[]>{

    static PitchProcessor.PitchEstimationAlgorithm algorithm = PitchProcessor.PitchEstimationAlgorithm.YIN;
    static int sampleRate = 44100;
    static int bufferSize = 1024;
    static int overlap = 512;

    public Note[] transcribe(short[] samples) {

        float[] pitches = new float[(samples.length / (bufferSize - overlap)) + 1];        

        // Needs to be converted to float and scaled down to be between -1 and 1
        float[] scaledSamples = new float[samples.length];
        for (int i = 0; i < samples.length; i++) {
            scaledSamples[i] = (float) samples[i] / Float.MAX_VALUE;
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


        return new Note[0];
    }

    //DEBUG:
    private static short[] generateTestSignal() {
        // Generates a simple sine wave signal for testing (440 Hz, A4)
        int sampleRate = 44100;
        double frequency = 440.0; // A4
        int duration = 2; // 2 seconds
        short[] signal = new short[sampleRate * duration];
        for (int i = 0; i < signal.length; i++) {
            signal[i] = (short) Math.sin(2 * Math.PI * frequency * i / sampleRate);
        }
        return signal;
    }

    public static void main(String[] args) {
        short[] testSignal = generateTestSignal();
        AmplitudeTranscriber a = new AmplitudeTranscriber();
        a.transcribe(testSignal);
    }
    ////////////////////
}
