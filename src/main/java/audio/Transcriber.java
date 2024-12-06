package audio;

import gui.Note;

public interface Transcriber<T> {
	Note[] transcribe(T samples);
}
