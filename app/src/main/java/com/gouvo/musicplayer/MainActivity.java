package com.gouvo.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.view.View;
import android.os.Bundle;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button select;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        select = (Button) findViewById (R.id.select);

        select.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick (View v)
            {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                //startActivityForResult(chooseFile, );
            }
        });
    }

    @Override
    public void onClick (View v)
    {
        System.out.println("Mprabo malaka");
    }
}