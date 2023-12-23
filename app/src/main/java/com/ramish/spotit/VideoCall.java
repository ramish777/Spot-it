package com.ramish.spotit;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class VideoCall extends AppCompatActivity {

    ImageView close_video_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        close_video_call = findViewById(R.id.image_close_video_call);

        close_video_call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(VideoCall.this, DirectMessage.class);
                startActivity(intent);
            }
        });
    }
}