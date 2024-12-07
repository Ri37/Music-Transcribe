package audio;

import org.bytedeco.javacpp.Loader;

import java.io.File;
import java.io.IOException;

public class FFmpegAudioProcessor extends AudioProcessor {

	public FFmpegAudioProcessor(int sampleRate, String acodec, int channels) {
		super(sampleRate, acodec, channels);
	}

	@Override
	public void processFile(File audioFile) {
		String ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);

		String outputFileName = audioFile.getName().replaceFirst("\\.[^.]+$", "") + "_processed.wav";
		this.outputFile = new File(audioFile.getParent(), outputFileName);

		ProcessBuilder pb = new ProcessBuilder(	ffmpeg,
												"-i", audioFile.getAbsolutePath(),
												"-vn",
												"-sn",
												"-dn",
												"-ar", String.valueOf(sampleRate),
												"-acodec", acodec,
												"-ac", String.valueOf(channels),
												outputFile.getAbsolutePath());

		try {
			int res = pb.inheritIO().start().waitFor();

			if (res != 0) {
				throw new RuntimeException("FFmpeg process failed with exit code: " + res);
			}
		}
		catch (IOException | InterruptedException e) {
			throw new RuntimeException("Failed to process audio file: " + e.getMessage(), e);
		}
	}
}
