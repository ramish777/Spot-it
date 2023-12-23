package com.ramish.spotit;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class EmailVerification extends AppCompatActivity {

    ImageView back_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        back_email = findViewById(R.id.image_back_email);

        back_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmailVerification.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
}