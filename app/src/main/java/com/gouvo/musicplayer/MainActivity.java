package com.gouvo.musicplayer;
import android.provider.MediaStore;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.widget.Button;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;
import android.content.ClipData;
import android.net.Uri;
import android.widget.LinearLayout;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button selectFilesButton;
    Button clearButton;
    private ActivityResultLauncher<Intent> fileSelectorLauncher;
    private List<String> loadedFiles = new ArrayList<>();
    ArrayList<Song> songList = new ArrayList<>();

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("1");
        // Check if permission is already granted
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    PERMISSION_REQUEST_CODE);
        }

        selectFilesButton = findViewById(R.id.select);
        clearButton = findViewById(R.id.clear);

        fileSelectorLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();

                if (data != null) {
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {

                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            Uri uri = clipData.getItemAt(i).getUri();
                            loadFileWithThumbnail(uri);
                        }
                    } else {
                        Uri uri = data.getData();
                        loadFileWithThumbnail(uri);
                    }
                }
            }
        });
        selectFilesButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("audio/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            fileSelectorLauncher.launch(intent);
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout imageContainer = findViewById(R.id.image_container);
                imageContainer.removeAllViews();
                loadedFiles.clear();
            }
        });
        String[] songData = {
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        // gets all audio related files
        // for each file content resolver gets audio file data based on sondData array(path, title and duration)
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songData, selection, null, null);

        while(cursor.moveToNext()) {
            Song song = new Song(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            if(new File(song.getPath()).exists())
                songList.add(song);
        }
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, you can now proceed with your app logic
            } else {
                // Permissions denied, handle this case (e.g., show a message)
            }
        }
    }

    /*
    private void loadFileWithThumbnail(Uri uri) {
        String filePath = uri.getPath();
        File file = new File(filePath);
        String fileName = file.getName();
        if (loadedFiles.contains(fileName)) {
            return;
        }

        loadedFiles.add(file.getName());
        Bitmap thumbnailBitmap = coverpicture(filePath);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView = new TextView(this);
        textView.setText(fileName);

        ImageView imageView = new ImageView(this);
        if (thumbnailBitmap != null) {
            imageView.setImageBitmap(thumbnailBitmap);
        } else {
            //imageView.setImageResource(R.drawable.ic_launcher_background); // Set a default image if thumbnail is not available
        }

        linearLayout.addView(textView);
        linearLayout.addView(imageView);

        LinearLayout imageContainer = findViewById(R.id.image_container);
        imageContainer.addView(linearLayout);
    }
    */

    private void loadFileWithThumbnail(Uri uri) {
        String filePath = uri.getPath();
        File file = new File(filePath);
        String fileName = file.getName();
        if (loadedFiles.contains(fileName)) {
            return;
        }

        loadedFiles.add(file.getName());
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);
        // Create a new LinearLayout to hold both the text and the image
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView = new TextView(this);
        textView.setText(fileName);

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(img);

        if (img != null) {
            imageView.setImageBitmap(img);
        } else {
            imageView.setImageResource(R.drawable.default_thumbnail); // Set a default image if thumbnail is not available
        }

        linearLayout.addView(textView);
        linearLayout.addView(imageView);

        LinearLayout imageContainer = findViewById(R.id.image_container);
        imageContainer.addView(linearLayout);
    }
    public Bitmap coverpicture(String path) {

        System.out.println("Path is!: "+ path);
        MediaMetadataRetriever mr = new MediaMetadataRetriever();
        mr.setDataSource(path);

        byte[] byte1 = mr.getEmbeddedPicture();
        // Don't release the MediaMetadataRetriever here
        // try {
        //     mr.release();
        // } catch (IOException e) {
        //     throw new RuntimeException(e);
        // }
        if (byte1 != null) {
            return BitmapFactory.decodeByteArray(byte1, 0, byte1.length);
        } else {
            return null;
        }

    }
    @Override
    public void onClick (View v)
    {

    }
}