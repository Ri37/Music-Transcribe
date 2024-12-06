package audio;

public abstract class AudioSampler<T> {

	protected final AudioProcessor audioProcessor;

	public AudioSampler(AudioProcessor audioProcessor) {
		this.audioProcessor = audioProcessor;
	}

	public abstract T getSamples();
}
