//https://www.youtube.com/watch?v=1D1Jo1sLBMo
package com.gouvo.musicplayer;
import android.provider.MediaStore;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.activity.result.ActivityResultLauncher;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private List<String> loadedFiles = new ArrayList<>();
    ArrayList<Song> songList = new ArrayList<>();

    RecyclerView mediaView;
    TextView noMusicTextView;
    ImageButton next, prev,pausePlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaView = findViewById(R.id.media_viewer);
        noMusicTextView = findViewById(R.id.no_songs_text);

        next = findViewById(R.id.now_next_track);
        prev = findViewById(R.id.now_previous_track);
        pausePlay = findViewById(R.id.now_pause_play_track);

        if (checkPermission() == false){
            requestPermission();
            return;
        }

        String[] projection ={
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,null);
        while (cursor.moveToNext()){
            Song song = new Song(cursor.getString(1),cursor.getString(0),cursor.getString(2));
            if (new File(song.getPath()).exists())
                songList.add(song);
        }

        if (songList.size() == 0){
            noMusicTextView.setVisibility(View.VISIBLE);
        }else{
            mediaView.setLayoutManager(new LinearLayoutManager(this));
            mediaView.setAdapter(new MusicListAdapter(songList, getApplicationContext()));
        }
        String[] songData = {
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION
        };
    }

    boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            return false;
        }
    }

    void requestPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(MainActivity.this, "Please allow the app to read through your music files in order to continue.",Toast.LENGTH_SHORT);
        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
    }

    @Override
    public void onClick(View view) {

    }
}