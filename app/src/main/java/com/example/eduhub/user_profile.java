package com.example.eduhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class user_profile extends AppCompatActivity {

    Button btnViewQuiz;
    Button btnAddResource;
    Button btnLogout;
    Button btnViewNotes;
    TextView txtViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = currentUser.getEmail();

        btnViewQuiz = findViewById(R.id.btnViewQuiz);
        btnLogout = findViewById(R.id.btnLogOut);
        btnAddResource = findViewById(R.id.btnAddResource);
        btnViewNotes = findViewById(R.id.btnViewNotes);
        txtViewEmail = findViewById(R.id.txtEmail);

        txtViewEmail.setText(email);

        //Go into Profile
        btnViewQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AllQuizActivity.class);
                startActivity(i);
            }
        });

        btnViewNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), NoteListActivity.class);
                startActivity(i);
            }
        });

        btnAddResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddResource.class);
                startActivity(i);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}