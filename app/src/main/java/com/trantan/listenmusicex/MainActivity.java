package com.trantan.listenmusicex;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.trantan.listenmusicex.adapter.ViewPagerAdapter;
import com.trantan.listenmusicex.model.Song;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpUi();
    }

    private void setUpUi() {
        mViewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
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
}
