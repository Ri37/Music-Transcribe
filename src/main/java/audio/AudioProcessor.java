package audio;

import java.io.File;

public abstract class AudioProcessor {

	protected final int sampleRate;
	protected final String format;
	protected final int channels;

	public AudioProcessor(int sampleRate, String format, int channels) {
		this.sampleRate = sampleRate;
		this.format = format;
		this.channels = channels;
	}

	public abstract void processFile(File audioFile);
}
