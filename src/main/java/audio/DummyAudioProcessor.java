package audio;

import java.io.File;

public class DummyAudioProcessor extends AudioProcessor {

	public DummyAudioProcessor(int sampleRate, String format, int channels) {
		super(sampleRate, format, channels);
	}

	@Override
	public void processFile(File audioFile) {}
}
