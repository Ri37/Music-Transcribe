package audio;

import be.tarsos.dsp.pitch.PitchDetector;
import be.tarsos.dsp.pitch.PitchProcessor;
import gui.Note;

public class AmplitudeTranscriber implements Transcriber<short[]>{

    PitchProcessor.PitchEstimationAlgorithm algorithm = PitchProcessor.PitchEstimationAlgorithm.YIN;
    int sampleRate = 44100;
    int bufferSize = 1024;
    int overlap = 512;

    public Note[] transcribe(short[] samples) {

        // 44.1 khz samples to something smaller like 23ms

        // Needs to be converted to float and scaled down to be between -1 and 1

        // Convert ampitudes to hertz

        // Hertz to notes (+length?)

        return new Note[0];
    } 
}
