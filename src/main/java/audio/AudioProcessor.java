package audio;

import java.io.File;

public abstract class AudioProcessor {

	protected final int sampleRate;
	protected final String acodec;
	protected final int channels;

	protected File outputFile;

	public AudioProcessor(int sampleRate, String acodec, int channels) {
		this.sampleRate = sampleRate;
		this.acodec = acodec;;
		this.channels = channels;
	}

	public abstract void processFile(File audioFile);
	public abstract void close();

	public final File getOutputFile() {
		return this.outputFile;
	}
}
