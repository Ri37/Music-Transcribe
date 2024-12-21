package gui.components.menubar.actions;

import gui.Constants;
import gui.Note;
import gui.Page;
import gui.SheetRow;
import gui.components.SheetMusicCanvas;
import audio.AmplitudeTranscriber;
import audio.AudioProcessor;
import audio.AudioSampler;
import audio.Transcriber;

import audio.FFmpegAudioProcessor;
import audio.FFmpegAudioSampler;
import audio.DummyTranscriber;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFrame;

public class OpenAudioAction extends AbstractAction {

	private final JFrame frame;
	private final SheetMusicCanvas canvas;

	public OpenAudioAction(JFrame frame, SheetMusicCanvas canvas) {
		super("Read audio");

		this.frame = frame;
		this.canvas = canvas;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();

		FileNameExtensionFilter audioExtensionFilter = new FileNameExtensionFilter(
			"Audio Files (*.mp3, *.wav, *.flac, *.aac, *.ogg, *.wma, *.m4a, *.opus, "
				+ "*.aiff, *.amr, *.caf, *.ra, *.au, *.snd, *.voc, *.mpc, "
				+ "*.ac3, *.dts, *.mka, *.tta, *.wv, *.3gp)",
			"mp3", "wav", "flac", "aac", "ogg", "wma", "m4a", "opus",
			"aiff", "amr", "caf", "ra", "au", "snd", "voc", "mpc",
			"ac3", "dts", "mka", "tta", "wv", "3gp"
		);
		fileChooser.setFileFilter(audioExtensionFilter);

		int res = fileChooser.showOpenDialog(frame);

		if (res == JFileChooser.APPROVE_OPTION) {
			File audioFile = fileChooser.getSelectedFile();

			// Convert to desired format
			AudioProcessor audioProcessor = new FFmpegAudioProcessor(44100, "pcm_s16le", 1);
			audioProcessor.processFile(audioFile);

			// Retrieve raw samples
			AudioSampler<short[]> audioSampler = new FFmpegAudioSampler(audioProcessor, "s16le");
			short[] samples = audioSampler.getSamples();

			audioProcessor.close();

			// AI transcribe logic returns the Notes
			Transcriber<short[]> transcriber = new AmplitudeTranscriber();
			Note[] notes = transcriber.transcribe(samples);

			// Place Notes
			List<SheetRow> rows = new ArrayList<>();
			SheetRow currentRow = null;

			for (int i = 0; i < notes.length; i++) {

				if (i % Constants.NOTES_PER_ROW == 0) {
					int startY = 50 + rows.size() * Constants.ROW_SPACING;
					currentRow = new SheetRow(startY);
					rows.add(currentRow);
				}

				currentRow.addNote(notes[i]);

			}

			Page page = new Page(rows);
			canvas.setPage(page);
		}
	}

}
