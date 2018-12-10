package com.trantan.listenmusicex;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.trantan.listenmusicex.adapter.RecyclerAdapter;
import com.trantan.listenmusicex.handle.OnClickSong;
import com.trantan.listenmusicex.handle.PlayerListener;
import com.trantan.listenmusicex.model.Song;
import com.trantan.listenmusicex.notification.MusicNotification;
import com.trantan.listenmusicex.service.PlaySongService;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayListFragment extends Fragment implements
        OnClickSong,
        PlayerListener,
        View.OnClickListener{
    private static final String TAG = "PlayListFragment";
    public static final String PLAY_AT_POSITION = " PLAY_AT_POSITION";
    public static final String PLAY_LOOP_ALL = " PLAY_LOOP_ALL";
    public static final String PLAY_LOOP_ONE = " PLAY_LOOP_ONE";
    private static final String LIST_SONGS = "Danh sách bài hát (%s)";
    private ArrayList<Song> mSongs;
    private TextView mPlayList;
    private RecyclerView mRecyclerSongs;
    private View mMiniPlayer;
    private ImageView mPlayPause;
    private ImageView mNextSong;
    private ImageView mPreviousSong;
    private ProgressBar mProgressBar;
    private boolean mIsPlaying;
    private boolean mIsLoopAll;
    private boolean mIsLoopONE;
    private RecyclerAdapter mAdapter;
    private PlaySongService mSongService;
    public PlayListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_list, container, false);
        setUpUi(view);
        return view;
    }

    private void setUpUi(View view) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mSongs = mainActivity.loadSongs();
        mSongService = mainActivity.getSongService();
        mSongService.addPlayerListener(this);

        mPlayList = view.findViewById(R.id.text_play_list);
        mRecyclerSongs = view.findViewById(R.id.recycler_songs);
        mMiniPlayer = view.findViewById(R.id.layout_mini_player);

        mPlayPause = mMiniPlayer.findViewById(R.id.image_play_pause);
        mNextSong = mMiniPlayer.findViewById(R.id.image_next);
        mPreviousSong = mMiniPlayer.findViewById(R.id.image_previous);
        mProgressBar = mMiniPlayer.findViewById(R.id.progress_current_time);

        mAdapter = new RecyclerAdapter(mSongs, this);
        mRecyclerSongs.setAdapter(mAdapter);
        mPlayList.setText(String.format(LIST_SONGS, mSongs.size()));

//        mConnection = new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                Log.d(TAG, "onServiceConnected: ");
//                mSongService = ((PlaySongService.MyBinder) service).getService();
//                mIsBound = true;
//                //send to playsongfragment
//                mPlayListToPlaySong.sendSongService(mSongService);
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//                Log.d(TAG, "onServiceDisconnected: ");
//                mIsBound = false;
//                getActivity().unbindService(mConnection);
//            }
//        };
//        bindService();
        mPlayPause.setOnClickListener(this);
        mNextSong.setOnClickListener(this);
        mPreviousSong.setOnClickListener(this);
    }


    @Override
    public void onClickSong(int position) {
        Log.d(TAG, "onClickSong: ");
        mSongService.changeSong(position);
    }

    @Override
    public void listenCurrentTime(int currentTime) {
        mMiniPlayer.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(currentTime);
    }

    @Override
    public void listenTotalTime(int totalTime) {
        mPlayPause.setImageResource(R.drawable.ic_pause_24dp);
        mProgressBar.setMax(totalTime);
    }

    @Override
    public void listenChangeSong(int position) {
        mAdapter.setSelectedPosition(position);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void listenPausePlaySong() {
        changeImagePlayPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_play_pause:
                changePlayPause();
                break;
            case R.id.image_next:
                mSongService.nextSong();
                break;
            case R.id.image_previous:
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

    private void changeImagePlayPause() {
        if (mSongService.isPlaying()) mPlayPause.setImageResource(R.drawable.ic_pause_24dp);
        else mPlayPause.setImageResource(R.drawable.ic_play_24dp);

    }
}
