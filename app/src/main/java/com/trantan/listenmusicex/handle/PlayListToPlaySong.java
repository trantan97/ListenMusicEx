package com.trantan.listenmusicex.handle;

import com.trantan.listenmusicex.service.PlaySongService;

public interface PlayListToPlaySong {
    void sendSongService(PlaySongService songService);
    void sendDuration(int duration);
    void sendCurrentTime(int currentTime);
    void sendChangePlayPause();
}
