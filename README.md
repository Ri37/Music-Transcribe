# Music-Transcribe

Welcome to the **Music Transcriber App**! This application is designed to facilitate music transcription by providing an intuitive interface for working with sheet music, transcribing audio files, and managing musical notes. Below is a detailed guide to help you get started.

## Prerequisites

To run the **Music Transcriber App**, ensure the following requirements are met:

* **Java 21**: Download and install Java 21 from [Oracle](https://www.oracle.com/java/) or any trusted Java distribution provider.

## Features

### Working Window

The main interface of the **Music Transcriber App** provides tools for:

* **Panning**: Move the view horizontally or vertically to navigate the sheet.
* **Zooming**: Adjust the zoom level for detailed or broad views of the sheet music.

### Left Panel

The left panel houses essential modes and controls for interacting with your sheet music:

#### Modes

1. **View Mode**: Navigate and inspect the sheet music without making changes.
2. **Add Notes Mode**:
   * Use the **scroll wheel** to select the pitch of the note.
   * Use a **right-click** to select the rhythmic length.
   * Use a **left-click** to add the note to the desired location on the sheet.
3. **Delete Notes Mode**:
   * Click on a note to delete it while in this mode.

#### Controls

* **Add Rows**: Add additional rows to the sheet music.
* **Reset View**: Re-center the view if you pan out too far.
* **Zoom In/Out**: Adjust the zoom level to your preference.

### Menu Bar

The menu bar at the top of the application contains the following options:

#### File Tab

1. **New**: Create a new, empty sheet music file.
2. **Save**: Save the current sheet music as a MIDI file.
3. **Read Audio**: Load an audio file and transcribe it into sheet music.
4. **Information**: Display details about the creators of the application.

## Usage

1. **Start the Application**: Launch the app using Java 21.
2. **Load Audio (Optional)**: Use the "Read Audio" option in the File menu to load an audio file for transcription.
3. **Edit Sheet Music**:
   * Use the left panel to add, modify, or delete notes.
   * Add rows for more staff lines as needed.
4. **Save Your Work**: Use the "Save" option to export the sheet music as a MIDI file.

## Sample Audio

To test the transcription feature, sample audio files are available in the `<span>src/main/resources</span>` directory.

---

We hope you enjoy using the **Music Transcriber App** for all your transcription needs! For support or further information, access the "Information" option in the File menu.
