package com.example.eduhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eduhub.Database.Quiz;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizList extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    QuizAdapter quizAdapter;
    ArrayList<Quiz> list;
    RadioGroup radioGroup;
    RadioButton radioBtnB, radioBtnI, radioBtnA,rb;
    int selectedRadioBtn;
    ProgressBar progressBar;

    FloatingActionButton btnAddQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        progressBar = findViewById(R.id.quizListProgress);
        radioGroup = findViewById(R.id.radioGroup);
        selectedRadioBtn = radioGroup.getCheckedRadioButtonId();

        rb = (RadioButton) findViewById(selectedRadioBtn);
        progressBar.setVisibility(View.VISIBLE);
        displayQuiz();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selectedRadioBtn = radioGroup.getCheckedRadioButtonId();

                rb = (RadioButton) findViewById(selectedRadioBtn);
                displayQuiz();
                //        Go into Add Quiz
                btnAddQuiz = findViewById(R.id.btnAddQuiz);
                btnAddQuiz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent i = new Intent(getApplicationContext(),AddQuiz.class);
                        startActivity(i);
                        setContentView(R.layout.activity_add_quiz);

                    }

                });
            }
        });

    }

    public void displayQuiz(){
        recyclerView = findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Quiz");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        String level = rb.getText().toString().trim();
        Query query = databaseReference.orderByChild("level").equalTo(level);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    recyclerView.setVisibility(View.VISIBLE);

                    for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                        Map<String,String>allQ = new HashMap<>();
                        Quiz quiz = new Quiz();
                        quiz.setQuizId(dataSnapshot.child("quizId").getValue().toString());
                        quiz.setTitle(dataSnapshot.child("title").getValue().toString());

                        for (DataSnapshot sp :dataSnapshot.child("allQuestions").getChildren()){
                            String key = sp.getKey();
                            String value = sp.getValue().toString();
                            allQ.put(key,value);
                        }

                        quiz.setAllQuestions(allQ);
                        quiz.setDescription(dataSnapshot.child("description").getValue().toString());
                        list.add(quiz);
                    }
                    progressBar.setVisibility(View.GONE);
                    quizAdapter = new QuizAdapter(QuizList.this, list,rb.getText().toString().trim());
                    recyclerView.setAdapter(quizAdapter);
                    quizAdapter.notifyDataSetChanged();
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}