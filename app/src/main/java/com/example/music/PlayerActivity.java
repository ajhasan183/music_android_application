package com.example.music;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.EdgeToEdge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    TextView songNameTextPlayer, startDurationText, stopDurationText;
    SeekBar seekBar;
    ImageButton playButton;
    ImageButton nextButton, previousButton, fastForwardButton, fastRewindButton;
    ImageView song_image;

    WaveformView waveformView;
    Visualizer visualizer;

    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;

    private Handler handler = new Handler();

    private Runnable updateSeekBarRunnable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentPosition);
                startDurationText.setText(formatTime(currentPosition));
                handler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        songNameTextPlayer = findViewById(R.id.song_name_text_player);
        startDurationText = findViewById(R.id.start_duration_text);
        stopDurationText = findViewById(R.id.stop_duration_text);
        seekBar = findViewById(R.id.seekbar);
        playButton = findViewById(R.id.play_button);
        waveformView = findViewById(R.id.visualizer_view);
        song_image = findViewById(R.id.song_image);
        nextButton = findViewById(R.id.next_button);
        previousButton = findViewById(R.id.previous_button);
        fastForwardButton = findViewById(R.id.fast_forward_button);
        fastRewindButton = findViewById(R.id.fast_rewind_button);

        Intent intent = getIntent();
        mySongs = (ArrayList<File>) intent.getSerializableExtra("songs");
        position = intent.getIntExtra("position", 0);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        if (mySongs != null && mySongs.size() > 0 && position >= 0 && position < mySongs.size()) {
            File songFile = mySongs.get(position);
            songNameTextPlayer.setText(songFile.getName());
            songNameTextPlayer.setSelected(true);

            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(songFile.getAbsolutePath());
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
                songNameTextPlayer.setText("Error loading song");
                return;
            }
        } else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.testsong);
            songNameTextPlayer.setText("test_song.mp3");
            songNameTextPlayer.setSelected(true);
        }

        mediaPlayer.start();

        seekBar.setMax(mediaPlayer.getDuration());
        stopDurationText.setText(formatTime(mediaPlayer.getDuration()));
        startDurationText.setText(formatTime(0));

        handler.post(updateSeekBarRunnable);

        seekBar.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(this, R.color.colorPrimary),
                PorterDuff.Mode.MULTIPLY
        );

        seekBar.getThumb().setColorFilter(
                ContextCompat.getColor(this, R.color.colorPrimary),
                PorterDuff.Mode.SRC_IN
        );

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextButton.performClick();
            }
        });

        playButton.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                playButton.setBackgroundResource(R.drawable.play_icon);
                mediaPlayer.pause();
                handler.removeCallbacks(updateSeekBarRunnable);
            } else {
                playButton.setBackgroundResource(R.drawable.pause_icon);
                mediaPlayer.start();
                handler.post(updateSeekBarRunnable);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();

                position = (position + 1) % mySongs.size();

                Uri uri = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                songNameTextPlayer.setText(mySongs.get(position).getName());
                mediaPlayer.start();

                setupVisualizer();

                playButton.setBackgroundResource(R.drawable.pause_icon);
                startAnimation(song_image);

                seekBar.setMax(mediaPlayer.getDuration());
                stopDurationText.setText(formatTime(mediaPlayer.getDuration()));
                startDurationText.setText(formatTime(0));
                seekBar.setProgress(0);
                handler.post(updateSeekBarRunnable);
            }
        });


        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();

                position = ((position - 1)<0)? (mySongs.size() - 1) :(position - 1);

                Uri uri1 = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri1);
                songNameTextPlayer.setText(mySongs.get(position).getName());
                mediaPlayer.start();

                setupVisualizer();

                playButton.setBackgroundResource(R.drawable.pause_icon);
                startAnimation(song_image);

                seekBar.setMax(mediaPlayer.getDuration());
                stopDurationText.setText(formatTime(mediaPlayer.getDuration()));
                startDurationText.setText(formatTime(0));
                seekBar.setProgress(0);
                handler.post(updateSeekBarRunnable);
            }
        });

        fastForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
                }
            }
        });

        fastRewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean userTouch = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (userTouch && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                    startDurationText.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                userTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                userTouch = false;
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO}, 1);
            } else {
                setupVisualizer();
            }
        } else {
            setupVisualizer();
        }

    }

    private void setupVisualizer() {
        if (mediaPlayer == null) return;

        int audioSessionId = mediaPlayer.getAudioSessionId();
        if (audioSessionId <= 0) return;

        if (visualizer != null) {
            visualizer.release();
        }

        try {
            visualizer = new Visualizer(audioSessionId);
            visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);

            visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
                @Override
                public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                    if (waveformView != null) {
                        waveformView.updateWaveform(waveform);
                    }
                }

                @Override
                public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
                }
            }, Visualizer.getMaxCaptureRate() / 2, true, false);

            visualizer.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatTime(int milliseconds) {
        int minutes = (milliseconds / 1000) / 60;
        int seconds = (milliseconds / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            setupVisualizer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (visualizer != null) {
            visualizer.release();
            visualizer = null;
        }
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            handler.removeCallbacks(updateSeekBarRunnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (visualizer != null) {
            visualizer.release();
            visualizer = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(updateSeekBarRunnable);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupVisualizer();
            } else {
                Log.e("PlayerActivity", "Audio recording permission denied");
            }
        }
    }

    public void startAnimation(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(song_image, "rotation", 0f, 360f);
        animator.setDuration(1000);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator);

        set.start();
    }
}
