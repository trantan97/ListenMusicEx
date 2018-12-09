package com.trantan.listenmusicex.handle;

public interface PlayerListener {
    void listenCurrentTime(int currentTime);
    void listenTotalTime(int totalTime);
    void listenChangeSong(int position);
}
