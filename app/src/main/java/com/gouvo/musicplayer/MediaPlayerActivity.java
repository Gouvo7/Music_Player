package com.gouvo.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MediaPlayerActivity extends AppCompatActivity {

    TextView titleView,currentTimeView,totalTimeView;
    SeekBar seekbar;
    ImageView pausePlayBtn,nextBtn,prevBtn,songIcon;
    ArrayList<Song> songList;
    Song currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);

        titleView = findViewById(R.id.song_title);
        currentTimeView = findViewById(R.id.current_time);
        totalTimeView = findViewById(R.id.total_time);
        seekbar = findViewById(R.id.seek_bar);
        pausePlayBtn = findViewById(R.id.pause_play_track);
        nextBtn = findViewById(R.id.next_track);
        prevBtn = findViewById(R.id.previous_track);
        songIcon = findViewById(R.id.curr_song_icon);

        songList = (ArrayList<Song>) getIntent().getSerializableExtra("LIST");

        setResources();

        MediaPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer!=null){
                    seekbar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTimeView.setText(StrToTime(mediaPlayer.getCurrentPosition()+""));
                }
                if (mediaPlayer.isPlaying())
                    pausePlayBtn.setImageResource(R.drawable.pause_icon);
                else
                    pausePlayBtn.setImageResource(R.drawable.play_icon);
                new Handler().postDelayed(this,100);
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer!=null && b)
                    mediaPlayer.seekTo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    void setResources(){
        currentSong = songList.get(MyMediaPlayer.currIndex);
        titleView.setText(currentSong.getTitle());
        totalTimeView.setText(StrToTime(currentSong.getDuration()));
        pausePlayBtn.setOnClickListener(v->pausePlay());
        nextBtn.setOnClickListener(v->playNext());
        prevBtn.setOnClickListener(v->playPrev());
        titleView.setSelected(true);
        startMusic();
    }

    private void startMusic(){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(currentSong.path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekbar.setProgress(0);
            seekbar.setMax(mediaPlayer.getDuration());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playNext() {
        if (MyMediaPlayer.currIndex == songList.size() - 1)
            MyMediaPlayer.currIndex = 0;
        else
            MyMediaPlayer.currIndex += 1;
        mediaPlayer.reset();
        setResources();
    }

    private void playPrev(){
        if (MyMediaPlayer.currIndex == 0)
            MyMediaPlayer.currIndex = songList.size() -1;
        else
            MyMediaPlayer.currIndex -= 1;
        mediaPlayer.reset();
        setResources();
    }

    private void pausePlay(){
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }

    public static String StrToTime(String duration){
        Long mill = Long.parseLong(duration);
        String tmp = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(mill) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(mill) % TimeUnit.MINUTES.toSeconds(1));
        return tmp;
    }
}