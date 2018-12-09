package com.trantan.listenmusicex.handle;


public interface PlayerHandler {
    void createPlayer(int position);
    void startPlayer();
    void pausePlayer();
    int getDuration();
    int getCurrrentTime();
    boolean isPlaying();
    void seekSong(int time);
    void loopSong(boolean isLoop);
    void stopService();
    void changeSong(int position);
    void nextSong();
    void previosSong();
}
