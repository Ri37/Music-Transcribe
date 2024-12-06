package audio;

import gui.Note;

public class DummyTranscriber implements Transcriber<short[]> {

	@Override
	public Note[] transcribe(short[] samples) {
		return new Note[] {
			new Note(0, "quarter"),
			new Note(1, "half"),
			new Note(2, "full"),
			new Note(0, "quarter"),
			new Note(1, "half"),
			new Note(2, "full"),
			new Note(0, "quarter"),
			new Note(1, "half"),
			new Note(2, "full"),
			new Note(0, "quarter"),
			new Note(1, "half"),
			new Note(2, "full")
		};
	}
}
