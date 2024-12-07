package audio;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bytedeco.javacpp.Loader;

public class FFmpegAudioSampler extends AudioSampler<short[]> {

	public FFmpegAudioSampler(AudioProcessor audioProcessor, String format) {
		super(audioProcessor, format);
	}

	@Override
	public short[] getSamples() {
		File outputFile = audioProcessor.getOutputFile();

		if (outputFile != null && outputFile.exists()) {
			String ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);

			ProcessBuilder pb = new ProcessBuilder(	ffmpeg,
													"-i", outputFile.getAbsolutePath(),
													"-vn",
													"-sn",
													"-dn",
													"-f", format,
													"pipe:1");

			try {
				Process p = pb.start();
				InputStream is = p.getInputStream();

				ByteArrayOutputStream buff = new ByteArrayOutputStream();
				byte[] chunk = new byte[4096];
				int bytesRead = 0;

				while (-1 != (bytesRead = is.read(chunk))) {
					buff.write(chunk, 0, bytesRead);
				}

				byte[] bytes = buff.toByteArray();
				short[] ret = new short[bytes.length/2];
				for (int i = 0; i < bytes.length/2; i++) {
					ret[i] = (short)((bytes[2 * i + 1] << 8) | (bytes[2 * i] & 0xFF));
				}

				int res = p.waitFor();

				if (res != 0) {
					throw new RuntimeException("FFmpeg process failed with exit code: " + res);
				}

				return ret;
			}
			catch (IOException | InterruptedException e) {
				throw new RuntimeException("Failed to process audio file: " + e.getMessage(), e);
			}
		}
		else {
			throw new RuntimeException("Processed file is not available.");
		}
	}
}
