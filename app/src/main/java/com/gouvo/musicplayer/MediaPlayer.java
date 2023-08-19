package com.gouvo.musicplayer;

import android.media.MediaPlayer;

public class MediaPlayer {
    static MediaPlayer instance;

    public static MediaPlayer getInstance(){
        if(instance == null){
            instance = new MediaPlayer();
        }
        return instance;
    }
}
