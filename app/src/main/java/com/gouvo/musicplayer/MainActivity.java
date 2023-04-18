package com.gouvo.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.view.View;
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



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button selectFilesButton;
    private ActivityResultLauncher<Intent> fileSelectorLauncher;

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
        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MINI_KIND);
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(thumbnail);
        LinearLayout imageContainer = findViewById(R.id.image_container);
        imageContainer.addView(imageView);
    }

    @Override
    public void onClick (View v)
    {
        System.out.println("Mprabo malaka");
    }
}