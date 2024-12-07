package audio;

public abstract class AudioSampler<T> {

	protected final AudioProcessor audioProcessor;
	protected String format;

	public AudioSampler(AudioProcessor audioProcessor, String format) {
		this.audioProcessor = audioProcessor;
		this.format = format;
	}

	public abstract T getSamples();
}
