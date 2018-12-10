package com.trantan.listenmusicex.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.trantan.listenmusicex.MainActivity;
import com.trantan.listenmusicex.R;
import com.trantan.listenmusicex.model.Song;
import com.trantan.listenmusicex.service.PlaySongService;

public class MusicNotification {
    private static RemoteViews sRemoteViews;
    private static NotificationCompat.Builder sBuilder;
    private static NotificationManager sManager;

    private static final int NOTIFICATION_ID = 111;

    public static void setUpNotification(Context context, Song song) {
        sRemoteViews = new RemoteViews(context.getPackageName(),
                R.layout.notification_layout);
        sRemoteViews.setTextViewText(R.id.text_song, song.getNameSong());
        sRemoteViews.setTextViewText(R.id.text_singer, song.getSinger());
        sRemoteViews.setImageViewResource(R.id.image_play, R.drawable.ic_pause_24dp);
        sRemoteViews.setImageViewResource(R.id.image_song, R.drawable.ic_default);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent
                = PendingIntent.getActivity(context, 0, intent, 0);

        sBuilder = new NotificationCompat.Builder(context, PlaySongService.ID_CHANNEL);
        sBuilder.setSmallIcon(R.drawable.ic_music_black_24dp)
                .setContent(sRemoteViews)
                .setContentIntent(pendingIntent)
                .setOngoing(true);
        sManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        setOnClickPlayPause(context);

        sManager.notify(NOTIFICATION_ID, sBuilder.build());

    }

    public static void updateNotification(boolean isPlaying) {
        if (isPlaying) {
            sRemoteViews.setImageViewResource(R.id.image_play, R.drawable.ic_pause_24dp);
            sBuilder.setOngoing(false);
        } else {
            sRemoteViews.setImageViewResource(R.id.image_play, R.drawable.ic_play_24dp);
            sBuilder.setOngoing(true);
        }
        sManager.notify(NOTIFICATION_ID, sBuilder.build());
    }

    private static void setOnClickPlayPause(Context context) {
        Intent intent = new Intent(context, PlaySongService.class);
        intent.setAction(PlaySongService.ACTION_PLAY_PAUSE);
        PendingIntent pendingIntent
                = PendingIntent.getService(context, 0, intent, 0);

        sRemoteViews.setOnClickPendingIntent(R.id.image_play, pendingIntent);
    }

    public static void deleteNotification() {
        sBuilder.setOngoing(false);
        sManager.cancel(NOTIFICATION_ID);
    }
}
