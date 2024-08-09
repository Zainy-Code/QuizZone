package com.example.logoquizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;



public class startActivity extends AppCompatActivity {



    private VideoView videoBG;
    private Button btnStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);



        videoBG = findViewById(R.id.videoBg);
        btnStart = findViewById(R.id.btnStart);

        Uri url = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.zainab_gif_rev);

        videoBG.setVideoURI(url);
        videoBG.start();

        videoBG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        videoBG.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        videoBG.stopPlayback();
    }


}