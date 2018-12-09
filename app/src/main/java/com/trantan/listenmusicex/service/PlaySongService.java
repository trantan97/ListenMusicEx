package com.trantan.listenmusicex.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.trantan.listenmusicex.PlayListFragment;
import com.trantan.listenmusicex.handle.PlayerHandler;
import com.trantan.listenmusicex.handle.PlayerListener;
import com.trantan.listenmusicex.model.Song;

import java.io.IOException;
import java.util.ArrayList;

public class PlaySongService extends Service implements PlayerHandler,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener {
    private static final String TAG = "PlaySongService";
    public static final String ACTION_PREVIOUS = "ACTION_PREVIOUS";
    public static final String ACTION_PLAY_PAUSE = "ACTION_PLAY_PAUSE";
    public static final String ACTION_NEXT = "ACTION_NEXT";
    public static final int DEFAULT_POSITION = -1;
    private MediaPlayer mMediaPlayer;
    private ArrayList<Song> mSongs;
    private boolean mIsLoopAll = true;
    private IBinder mIBinder;
    private int mPosition;
    private CurrentTimeTask timeTask;
    private PlayerListener mPlayerListener;

    @Override
    public void onCreate() {
        super.onCreate();
        mIBinder = new MyBinder();
        mPosition = DEFAULT_POSITION;
//        getSingleton();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Bundle bundle = intent.getBundleExtra(PlayListFragment.BUNDLE_PLAY_LIST);
        mSongs = (ArrayList<Song>) bundle.getSerializable(PlayListFragment.PLAY_LIST);
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        if (timeTask != null) timeTask.setRun(false);
        mMediaPlayer.stop();
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        receiverIntent(intent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
    }

    @Override
    public void createPlayer(int position) {
        Log.d(TAG, "createPlayer: " + position);
        if (position < 0) return;
        mMediaPlayer = getSingleton();
        try {
            mMediaPlayer.setDataSource(mSongs.get(position).getLinkSong());
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnCompletionListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiverIntent(Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case ACTION_PLAY_PAUSE: {
                if (mMediaPlayer.isPlaying()) {
                    pausePlayer();
                } else startPlayer();
                break;
            }
            default:
        }
    }

    private MediaPlayer getSingleton() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } else mMediaPlayer.reset();
        return mMediaPlayer;
    }

    public void setPlayerListener(PlayerListener playerListener) {
        mPlayerListener = playerListener;
    }

    public Song getCurrentSong() {
        return mPosition < 0 ? null : mSongs.get(mPosition);
    }

    @Override
    public void startPlayer() {
        if (mMediaPlayer != null) mMediaPlayer.start();
    }

    @Override
    public void pausePlayer() {
        if (isPlaying()) mMediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mMediaPlayer == null ? 0 : mMediaPlayer.getDuration();
    }

    @Override
    public int getCurrrentTime() {
        return mMediaPlayer == null ? 0 : mMediaPlayer.getCurrentPosition();
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer == null ? false : mMediaPlayer.isPlaying();
    }

    @Override
    public void seekSong(int time) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(time);
        }
    }

    @Override
    public void loopSong(boolean isLoop) {
        if (mMediaPlayer != null) mMediaPlayer.setLooping(isLoop);
    }

    @Override
    public void stopService() {
        stopSelf();
    }

    @Override
    public void changeSong(int position) {
        if (timeTask != null) {
            timeTask.setRun(false);
        }
        if (mIsLoopAll && position >= mSongs.size()) position = 0;
        if (mIsLoopAll && position < 0) position = mSongs.size() - 1;
        if (!mIsLoopAll && (position < 0 || position >= mSongs.size())) {
            mMediaPlayer.pause();
            position = DEFAULT_POSITION;
        }
        Log.d(TAG, "changeSong: " + position);
        mPosition = position;
        mPlayerListener.listenChangeSong(position);
        createPlayer(position);
    }

    @Override
    public void nextSong() {
        changeSong(++mPosition);
    }

    @Override
    public void previosSong() {
        changeSong(--mPosition);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "onCompletion: ");
        changeSong(++mPosition);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "onPrepared: ");
        timeTask = new CurrentTimeTask();
        timeTask.execute();
        mMediaPlayer.start();
        mPlayerListener.listenTotalTime(getDuration());
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "onError: ");
        createPlayer(mPosition);
        return true;
    }

    public class MyBinder extends Binder {
        public PlaySongService getService() {
            return PlaySongService.this;
        }
    }

    class CurrentTimeTask extends AsyncTask<Void, Integer, Void> {
        private boolean mIsRun = true;

        public void setRun(boolean run) {
            mIsRun = run;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (mIsRun) {
                if (mMediaPlayer != null) {
                    publishProgress(mMediaPlayer.getCurrentPosition());
//                    Log.d(TAG, "doInBackground: "+mMediaPlayer.getCurrentPosition());
                }
                try {

                    Thread.sleep(100);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mPlayerListener.listenCurrentTime(values[0]);
        }
    }
}
