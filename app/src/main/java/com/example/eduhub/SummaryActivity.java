package com.example.eduhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SummaryActivity extends AppCompatActivity {
    TextView txtAllQ,txtCorrQ,txtOverall;
    Button btnDone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        txtAllQ = findViewById(R.id.summaryAllq);
        txtCorrQ = findViewById(R.id.summaryCorrectq);
        txtOverall = findViewById(R.id.summaryOverall);
        btnDone = findViewById(R.id.summaryDone);


        Bundle extras = getIntent().getExtras();

        int allQ = extras.getInt("allQ");
        int score = extras.getInt("score");

        int avg = (score*100)/allQ;


        txtAllQ.setText(String.valueOf(allQ));
        txtCorrQ.setText(String.valueOf(score));
//        Toast.makeText(this, "avg is :"+avg, Toast.LENGTH_SHORT).show();
        txtOverall.setText(String.valueOf(avg)+"%");

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SummaryActivity.this,QuizList.class);
                startActivity(intent);
            }
        });

    }
}