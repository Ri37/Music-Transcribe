package audio;

public class DummyAudioSampler extends AudioSampler<short[]> {

	public DummyAudioSampler(AudioProcessor audioProcessor) {
		super(audioProcessor);
	}

	@Override
	public short[] getSamples() {
		return new short[] {0, 1000, -1000, Short.MIN_VALUE, Short.MAX_VALUE};
	}
}
