import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegLogCallback;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class JavaCVTest {

	private static String path;

	@BeforeAll
	static void initTests() throws URISyntaxException {
		FFmpegLogCallback.set();

		URL resource = JavaCVTest.class.getClassLoader().getResource("audio/normal-hitclap3.wav");
        assertNotNull(resource, "Missing test audio file");
        path = new File(resource.toURI()).getAbsolutePath();
	}

	@Test
	void testFFmpegFrameGrabberInit() {
		try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(path)) {
			grabber.setFormat("wav");
			grabber.start();

			assertNotNull(grabber.getFormat(), "Format should not be null");

			grabber.stop();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	void testFFmpegFrameGrabberChannelCount() {
		try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(path)) {
			grabber.setFormat("wav");
			grabber.start();

			assertEquals(grabber.getAudioChannels(), 2, "The test file should contain 2 audio channels");

			grabber.stop();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
