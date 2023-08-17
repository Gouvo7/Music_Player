package com.gouvo.musicplayer;

import android.media.MediaPlayer;
import android.provider.MediaStore;

public class MyMediaPlayer {
    static MediaPlayer instance;
    public static int currIndex = -1;
    public static MediaPlayer getInstance(){
        if (instance == null){
            instance = new MediaPlayer();
        }
        return instance;
    }
}
