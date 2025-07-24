# 🎵 Music App – Android MP3/WAV Player with Visualizer

The **Music App** is a modern Android application built using Java that allows users to browse and play `.mp3` and `.wav` files from their device's storage. It features a visually appealing music player UI, audio visualization using waveforms, media controls, and seamless file browsing.

## 🚀 Features

- 🎧 Scan and display all `.mp3` and `.wav` songs from the device's Music directory
- ▶️ Play, pause, skip forward, rewind, and navigate between tracks
- 🔊 Real-time waveform visualizer using Android's `Visualizer` API
- ⏱️ SeekBar with time duration display (start/end)
- 🔁 Automatically play the next song after the current track finishes
- 📁 Custom song list view with dynamic file loading
- 🔄 Rotation animation on song artwork during playback

## 🛠️ Built With

- **Language:** Java
- **Framework:** Android SDK
- **Libraries:**
  - [Dexter](https://github.com/Karumi/Dexter) – for handling runtime permissions
  - Android `MediaPlayer` – for audio playback
  - Android `Visualizer` – for waveform capture
  - Android `Handler` – for periodic UI updates

## 📸 Screenshots

> *(Add screenshots or screen recordings here if available)*

## 📱 How to Run

1. Clone this repository:
   ```bash
   git clone https://github.com/your-username/music-app.git
Open it in Android Studio

Connect a physical device or emulator with Android 6.0 (API 23) or higher

Grant storage and microphone permissions (for waveform visualization)

Run the app and browse songs stored in /Music directory

🧩 Project Structure
com.example.music
├── MainActivity.java          # Displays the list of available songs
├── PlayerActivity.java        # Handles music playback, UI controls, and waveform
├── WaveformView.java          # Custom View to render waveform using Visualizer
├── SongItemAdapter.java       # (Custom adapter if implemented for song list)

🛤️ Future Improvements
Add support for playlists and favorites

Implement background playback and notification controls

Integrate album art and metadata display

Improve visualizer with FFT (frequency) support

🔒 Permissions Required
READ_MEDIA_AUDIO or READ_EXTERNAL_STORAGE – To read audio files from device

RECORD_AUDIO – To enable real-time audio visualization

📄 License
This project is open-source and available under the MIT License.
