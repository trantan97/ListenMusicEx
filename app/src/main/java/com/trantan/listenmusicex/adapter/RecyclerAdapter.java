package com.trantan.listenmusicex.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.trantan.listenmusicex.R;
import com.trantan.listenmusicex.handle.OnClickSong;
import com.trantan.listenmusicex.model.Song;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private static final String TAG = "RecyclerAdapter";
    private ArrayList<Song> mSongs;
    private OnClickSong mOnClickSong;
    private int selectedPosition = -1;

    public RecyclerAdapter(ArrayList<Song> songs, OnClickSong onClickSong) {
        mSongs = songs;
        mOnClickSong = onClickSong;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_recycler_songs, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.setData(mSongs.get(i));
        if (selectedPosition == i) viewHolder.setStylePlaying();
        else viewHolder.setStyleNoPlaying();

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = i;
                notifyDataSetChanged();
                mOnClickSong.onClickSong(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSongs == null ? 0 : mSongs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final String COLOR_PLAYING = "#14DDFF";
        private final String COLOR_BLACK = "#000000";
        private final String COLOR_GRAY = "#979797";
        private ImageView mImagePlaying;
        private ImageView mImageDelete;
        private TextView mNameSong;
        private TextView mSinger;
        private View mView;
        private Song mSong;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            setUpUi(itemView);
        }

        private void setUpUi(View view) {
            mNameSong = view.findViewById(R.id.text_name_song);
            mSinger = view.findViewById(R.id.text_singer);
            mImagePlaying = view.findViewById(R.id.image_playing);
            mImageDelete = view.findViewById(R.id.image_delete_song);
        }

        public void setData(Song song) {
            mSong = song;
            mNameSong.setText(mSong.getNameSong());
            mSinger.setText(mSong.getSinger());
        }

        public void setStylePlaying() {
            mImagePlaying.setVisibility(View.VISIBLE);
            mNameSong.setTextColor(Color.parseColor(COLOR_PLAYING));
            mSinger.setTextColor(Color.parseColor(COLOR_PLAYING));
            mImageDelete.setImageResource(R.drawable.ic_delete_playing_24dp);
        }

        public void setStyleNoPlaying() {
            mImagePlaying.setVisibility(View.INVISIBLE);
            mNameSong.setTextColor(Color.parseColor(COLOR_BLACK));
            mSinger.setTextColor(Color.parseColor(COLOR_GRAY));
            mImageDelete.setImageResource(R.drawable.ic_delete_24dp);

        }
    }
}