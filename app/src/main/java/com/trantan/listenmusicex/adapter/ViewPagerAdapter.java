package com.trantan.listenmusicex.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.trantan.listenmusicex.PlayListFragment;
import com.trantan.listenmusicex.PlaySongFragment;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static final int COUNT_FRAGMENT = 2;
    private static final int PLAY_SONG_FRAGMENT = 0;
    private static final int PLAY_LIST_FRAGMENT = 1;
    private PlaySongFragment mPlaySongFragment;
    private PlayListFragment mPlayListFragment;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mPlayListFragment = new PlayListFragment();
        mPlaySongFragment = new PlaySongFragment();
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case PLAY_SONG_FRAGMENT:
                return mPlaySongFragment;
            case PLAY_LIST_FRAGMENT:
                return mPlayListFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return COUNT_FRAGMENT;
    }
}
