package com.gouvo.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;
import android.content.ClipData;
import android.net.Uri;
import android.widget.LinearLayout;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button selectFilesButton;
    Button clearButton;
    private ActivityResultLauncher<Intent> fileSelectorLauncher;
    private List<String> loadedFiles = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        clearButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LinearLayout imageContainer = findViewById(R.id.image_container);
                imageContainer.removeAllViews();
                loadedFiles.clear();
            }
        });
    }
    private void loadFileWithThumbnail(Uri uri) {
        String filePath = uri.getPath();
        File file = new File(filePath);
        String fileName = file.getName();
        if (loadedFiles.contains(fileName)) {
            //Log.d("MyApp", "File already loaded: " + fileName);
            return;
        }

        loadedFiles.add(file.getName());
        System.out.println("File path is:" + filePath);
        Bitmap img = coverpicture(filePath);
        LinearLayout imageContainer = findViewById(R.id.image_container);
        imageContainer.removeAllViews();

        for (String loadedFile : loadedFiles) {
            TextView textView = new TextView(this);
            textView.setText(loadedFile);
            ImageView img_tst = findViewById(R.id.thumbnail);
            img_tst.setImageBitmap(img);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.addView(textView);
            linearLayout.addView(img_tst);

            imageContainer.addView(linearLayout);
        }
    }
    public Bitmap coverpicture(String path) {

        System.out.println("Path is!: "+ path);
        MediaMetadataRetriever mr = new MediaMetadataRetriever();
        System.out.println(path);
        mr.setDataSource(path);

        byte[] byte1 = mr.getEmbeddedPicture();
        try {
            mr.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(byte1 != null)
            return BitmapFactory.decodeByteArray(byte1, 0, byte1.length);
        else
            return  null;

    }
    @Override
    public void onClick (View v)
    {

    }
}