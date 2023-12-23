package com.ramish.spotit;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class VoiceCall extends AppCompatActivity {

    ImageView close_voice_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_call);

        close_voice_call = findViewById(R.id.image_close_voice_call);

        close_voice_call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(VoiceCall.this, DirectMessage.class);
                startActivity(intent);
            }
        });
    }
}