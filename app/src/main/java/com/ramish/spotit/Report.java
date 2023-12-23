package com.ramish.spotit;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Report extends AppCompatActivity {

    ImageView back_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        back_report = findViewById(R.id.image_back_report);

        back_report.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Report.this, ItemDetails.class);
                startActivity(intent);
            }
        });
    }
}