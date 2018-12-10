package com.trantan.listenmusicex;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.trantan.listenmusicex.handle.PlayerListener;
import com.trantan.listenmusicex.notification.MusicNotification;
import com.trantan.listenmusicex.service.PlaySongService;
import com.trantan.listenmusicex.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaySongFragment extends Fragment implements PlayerListener,
        View.OnClickListener {
    private static final String TAG = "PlaySongFragment";
    private TextView mNameSong;
    private TextView mNameSinger;
    private TextView mTotalTime;
    private TextView mCurrentTime;
    private ImageView mPlayPause;
    private ImageView mNextSong;
    private ImageView mPreviousSong;
    private ImageView mImageSong;
    private SeekBar mSeekBar;
    private boolean mIsPlaying;
    private PlaySongService mSongService;

    public PlaySongFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_song, container, false);
        setUpUi(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void setUpUi(View view) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mSongService = mainActivity.getSongService();
        mSongService.addPlayerListener(this);

        mNameSong = view.findViewById(R.id.text_name_song);
        mNameSinger = view.findViewById(R.id.text_singer);
        mTotalTime = view.findViewById(R.id.text_total_time);
        mCurrentTime = view.findViewById(R.id.text_current_time);
        mPlayPause = view.findViewById(R.id.image_play_pause);
        mNextSong = view.findViewById(R.id.image_next);
        mImageSong = view.findViewById(R.id.image_song);
        mPreviousSong = view.findViewById(R.id.image_previous);
        mSeekBar = view.findViewById(R.id.seekBar);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int mPogress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPogress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStopTrackingTouch: " + mPogress);
                if (mSongService != null) {
                    mSongService.seekSong(mPogress);
                }
            }
        });
        mPlayPause.setOnClickListener(this);
        mNextSong.setOnClickListener(this);
        mPreviousSong.setOnClickListener(this);
    }



    private void changeImagePlayPause() {
        if (mSongService.isPlaying()) {
            Utils.rotateImage(mImageSong, true);
            mPlayPause.setImageResource(R.drawable.ic_pause_24dp);
        } else {
            Utils.rotateImage(mImageSong, false);
            mPlayPause.setImageResource(R.drawable.ic_play_24dp);
        }
    }

    @Override
    public void onClick(View v) {
        if (mSongService.getCurrentSong() == null) {
            mSongService.changeSong(0);
            return;
        }
        switch (v.getId()) {
            case R.id.image_play_pause:
                changePlayPause();
                break;
            case R.id.image_next:
                Utils.rotateImage(mImageSong, false);
                mSongService.nextSong();
                break;
            case R.id.image_previous:
                Utils.rotateImage(mImageSong, false);
                mSongService.previosSong();
                break;
            default:
        }
    }

    private void changePlayPause() {
        if (mSongService.isPlaying()) mSongService.pausePlayer();
        else mSongService.startPlayer();
        MusicNotification.updateNotification(mSongService.isPlaying());
        changeImagePlayPause();
    }

    @Override
    public void listenCurrentTime(int currentTime) {
        mSeekBar.setProgress(currentTime);
        mCurrentTime.setText(Utils.passTimeToString(currentTime));
    }

    @Override
    public void listenTotalTime(int totalTime) {
        mPlayPause.setImageResource(R.drawable.ic_pause_24dp);
        Utils.rotateImage(mImageSong,true);

        mNameSong.setText(mSongService.getCurrentSong().getNameSong());
        mNameSinger.setText(mSongService.getCurrentSong().getSinger());

        mSeekBar.setMax(totalTime);
        mTotalTime.setText(Utils.passTimeToString(totalTime));
    }

    @Override
    public void listenChangeSong(int position) {

    }

    @Override
    public void listenPausePlaySong() {
        changeImagePlayPause();
    }
}
