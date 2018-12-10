package com.trantan.listenmusicex;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.trantan.listenmusicex.adapter.ViewPagerAdapter;
import com.trantan.listenmusicex.model.Song;
import com.trantan.listenmusicex.notification.MusicNotification;
import com.trantan.listenmusicex.service.PlaySongService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String PLAY_LIST = "PLAY_LIST";
    public static final String BUNDLE_PLAY_LIST = "BUNDLE_PLAY_LIST";
    private ViewPager mViewPager;
    private ServiceConnection mConnection;
    private PlaySongService mSongService;
    private boolean mIsBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpUi();
    }

    private void setUpUi() {
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected: ");
                mSongService = ((PlaySongService.MyBinder) service).getService();
                mIsBound = true;
                mViewPager = findViewById(R.id.view_pager);
                ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                mViewPager.setAdapter(pagerAdapter);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "onServiceDisconnected: ");
                mIsBound = false;
                unbindService(mConnection);
            }
        };
        myBindService();
    }

    public ArrayList<Song> loadSongs() {
        ArrayList<Song> songs = new ArrayList<>();
        String nameSongs[] = getResources().getStringArray(R.array.name_songs);
        String nameSingers[] = getResources().getStringArray(R.array.name_singers);
        String linkSongs[] = getResources().getStringArray(R.array.link_songs);
        for (int i = 0; i < nameSongs.length; i++) {
            Song song = new Song(nameSongs[i], nameSingers[i], linkSongs[i]);
            songs.add(song);
        }
        return songs;
    }

    public void myBindService() {
        Intent intent = new Intent(this, PlaySongService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PLAY_LIST, loadSongs());
        intent.putExtra(BUNDLE_PLAY_LIST, bundle);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public PlaySongService getSongService() {
        return mSongService;
    }

    @Override
    protected void onDestroy() {
        MusicNotification.deleteNotification();
        super.onDestroy();
    }
}
