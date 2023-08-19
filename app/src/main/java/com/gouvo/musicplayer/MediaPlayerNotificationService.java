package com.gouvo.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class MediaPlayerNotificationService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null) {
            if (action.equals("play_pause_action")) {
                // Handle play/pause action here
                // You can use mediaPlayer.isPlaying() to toggle playback
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
