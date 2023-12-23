package com.ramish.spotit;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoCamera extends AppCompatActivity {

    TextView photo_switch;
    ImageView back_video_camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_camera);

        back_video_camera = findViewById(R.id.image_back_video_camera);
        photo_switch = findViewById(R.id.text_photo_switch);

        back_video_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(VideoCamera.this, DirectMessage.class);
                startActivity(intent);
            }
        });

        photo_switch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(VideoCamera.this, PhotoCamera.class);
                startActivity(intent);
            }
        });
    }
}