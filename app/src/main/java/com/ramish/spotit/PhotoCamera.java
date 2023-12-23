package com.ramish.spotit;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoCamera extends AppCompatActivity {

    ImageView back_photo_camera;
    TextView video_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_camera);

        back_photo_camera = findViewById(R.id.image_back_photo_camera);
        video_switch = findViewById(R.id.text_video_switch);

        back_photo_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PhotoCamera.this, DirectMessage.class);
                startActivity(intent);
            }
        });

        video_switch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PhotoCamera.this, VideoCamera.class);
                startActivity(intent);
            }
        });
    }
}