package com.gouvo.musicplayer;

import java.io.Serializable;

public class Song implements Serializable {
    String path;
    String title;
    String duration;

    public Song(String path, String title, String duration) {
        setPath(path);
        setTitle(title);
        setDuration(duration);
    }
    public void setPath(String path) {
        this.path = path;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public String getPath() {
        return path;
    }
    public String getTitle() {
        return title;
    }
    public String getDuration() {
        return duration;
    }
}
