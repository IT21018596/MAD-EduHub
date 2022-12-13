package com.example.eduhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnQuiz;
    Button btnProfile;
    Button btnMyNotes;
    Button btnResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnQuiz = findViewById(R.id.btnQuiz);
        btnProfile = findViewById(R.id.btnPofile);
        btnMyNotes = findViewById(R.id.btnMyNotes);
        btnResources = findViewById(R.id.btnResources);

        //Go into Add Quiz
        btnQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),QuizList.class);
                startActivity(i);

            }
        });

        //Go into Profile
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),user_profile.class);
                startActivity(i);

            }
        });

        //Go into notes
        btnResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), allResources.class);
                startActivity(i);

            }
        });

        //Go into notes
        btnMyNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),NoteListActivity.class);
                startActivity(i);

            }
        });

    }
}