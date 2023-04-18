package com.gouvo.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.os.Bundle;
import android.content.ClipData;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button selectFilesButton;
    private ActivityResultLauncher<Intent> fileSelectorLauncher;
    private List<String> loadedFiles = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectFilesButton = findViewById(R.id.select);
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
    }
    private void loadFileWithThumbnail(Uri uri) {
        String filePath = uri.getPath();
        File file = new File(filePath);
        String fileName = file.getName();

        if (loadedFiles.contains(fileName)) {
            Log.d("MyApp", "File already loaded: " + fileName);
            return;
        }


        loadedFiles.add(file.getName());

        System.out.println(file.getName());

        Log.d("MyApp", "Loaded file: " + file.getName());
        Log.d("MyApp", "Loaded files so far: " + loadedFiles);

        LinearLayout imageContainer = findViewById(R.id.image_container);
        for (String loadedFile : loadedFiles) {
            TextView textView = new TextView(this);
            textView.setText(loadedFile);

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.addView(textView);

            imageContainer.addView(linearLayout);
        }
    }

    @Override
    public void onClick (View v)
    {
        System.out.println("Mprabo malaka");
    }
}