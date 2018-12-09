package com.trantan.listenmusicex.model;

import java.io.Serializable;

public class Song implements Serializable {
    private String mNameSong;
    private String mSinger;
    private String mLinkSong;

    public Song(String nameSong, String singer, String linkSong) {
        mNameSong = nameSong;
        mSinger = singer;
        mLinkSong = linkSong;
    }

    public String getNameSong() {
        return mNameSong;
    }

    public String getSinger() {
        return mSinger;
    }

    public String getLinkSong() {
        return mLinkSong;
    }

}