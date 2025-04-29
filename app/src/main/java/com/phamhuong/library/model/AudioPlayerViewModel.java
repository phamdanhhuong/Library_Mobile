package com.phamhuong.library.model;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;

import java.io.IOException;

public class AudioPlayerViewModel extends AndroidViewModel {
    private Context context;
    private MediaPlayer mediaPlayer;
    private ExoPlayer exoPlayer;

    private MutableLiveData<Boolean> isPlaying = new MutableLiveData<>(false);
    private MutableLiveData<Long> currentPosition = new MutableLiveData<>(0L);
    private MutableLiveData<Long> duration = new MutableLiveData<>(0L);
    private MutableLiveData<String> currentBookTitle = new MutableLiveData<>();

    public AudioPlayerViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }


    public LiveData<Boolean> isPlaying() {
        return isPlaying;
    }

    public LiveData<Long> currentPosition() {
        return currentPosition;
    }

    public LiveData<Long> duration() {
        return duration;
    }

    public LiveData<String> currentBookTitle() {
        return currentBookTitle;
    }

    public void playAudio(String audioUrl, String bookTitle, boolean useExoPlayer) {
        currentBookTitle.setValue(bookTitle);
        isPlaying.setValue(true);
        if (useExoPlayer) {
            playWithExoPlayer(audioUrl);
        } else {
            playWithMediaPlayer(audioUrl);
        }
        startProgressUpdate();
    }

    private void playWithMediaPlayer(String audioUrl) {
        releaseMediaPlayer();
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                duration.setValue((long) mp.getDuration());
            });
            mediaPlayer.setOnCompletionListener(mp -> {
                isPlaying.setValue(false);
                currentPosition.setValue(0L);
            });
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                isPlaying.setValue(false);
                return false;
            });
        } catch (IOException e) {
            isPlaying.setValue(false);
            Log.e("AudioPlayer", "Error setting data source: " + e.getMessage());
        }
    }

    private void playWithExoPlayer(String audioUrl) {
        releaseExoPlayer();
        exoPlayer = new ExoPlayer.Builder(context).build();
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(audioUrl));
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
        duration.setValue(exoPlayer.getDuration());
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isNowPlaying) {
                isPlaying.setValue(isNowPlaying);
            }

            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_READY) {
                    duration.setValue(exoPlayer.getDuration());
                } else if (state == Player.STATE_ENDED) {
                    currentPosition.setValue(0L);
                }
            }
        });

    }

    public void pauseAudio() {
        isPlaying.setValue(false);
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        if (exoPlayer != null && exoPlayer.isPlaying()) {
            exoPlayer.pause();
        }
    }
    public void resumeAudio() {
        isPlaying.setValue(true);
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (exoPlayer != null) {
            exoPlayer.play();
        }
        startProgressUpdate(); // Tiếp tục cập nhật progress
    }

    public void stopAudio() {
        isPlaying.setValue(false);
        releaseMediaPlayer();
        releaseExoPlayer();
        currentPosition.setValue(0L);
    }
    public void setPlaybackSpeed(float speed) {
        if (exoPlayer != null) {
            exoPlayer.setPlaybackParameters(exoPlayer.getPlaybackParameters().withSpeed(speed));
        }
    }

    public void seekTo(long position) {
        currentPosition.setValue(position);
        if (mediaPlayer != null) {
            mediaPlayer.seekTo((int) position);
        }
        if (exoPlayer != null) {
            exoPlayer.seekTo(position);
        }
    }

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable updateRunnable;

    private void startProgressUpdate() {
        if (updateRunnable != null) {
            handler.removeCallbacks(updateRunnable);
        }

        updateRunnable = new Runnable() {
            @Override
            public void run() {
                if (isPlaying.getValue() != null && isPlaying.getValue()) {
                    if (mediaPlayer != null) {
                        currentPosition.setValue((long) mediaPlayer.getCurrentPosition());
                    } else if (exoPlayer != null) {
                        currentPosition.setValue(exoPlayer.getCurrentPosition());
                    }
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.postDelayed(updateRunnable, 1000);
    }


    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void releaseExoPlayer() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        handler.removeCallbacks(updateRunnable);
        releaseMediaPlayer();
        releaseExoPlayer();
    }

}