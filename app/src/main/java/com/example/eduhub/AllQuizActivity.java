package com.example.eduhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eduhub.Database.Quiz;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllQuizActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference dbRef;
    ArrayList<Quiz>quizList;
    ProgressBar progressBar;
    FloatingActionButton btnAddQuizMod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_quiz);

        recyclerView = (RecyclerView) findViewById(R.id.modifyAllQuizRecycler);

        dbRef = FirebaseDatabase.getInstance().getReference("Quiz");

        progressBar = findViewById(R.id.allQuizProgress);
        progressBar.setVisibility(View.VISIBLE);

        quizList = new ArrayList<>();

        //        Go into Add Quiz
        btnAddQuizMod = findViewById(R.id.btnAddQuizMod);
        btnAddQuizMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),AddQuiz.class);
                startActivity(i);
                setContentView(R.layout.activity_add_quiz);

            }

        });

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recyclerView.setVisibility(View.INVISIBLE);
                if (snapshot.hasChildren()){
                    //             Toast.makeText(AllQuizActivity.this, "No of childs :"+snapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                    recyclerView.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                        Quiz quiz = new Quiz();
                        quiz.setQuizId(dataSnapshot.child("quizId").getValue().toString().trim());
                        quiz.setTitle(dataSnapshot.child("title").getValue().toString().trim());
                        quiz.setDescription(dataSnapshot.child("description").getValue().toString().trim());
                        quiz.setLevel(dataSnapshot.child("level").getValue().toString().trim());

                        Map<String,String> allQ = new HashMap<>();
                        for (DataSnapshot sp :dataSnapshot.child("allQuestions").getChildren()){
                            String key = sp.getKey().trim();
                            String value = sp.getValue().toString();
                            allQ.put(key,value);
                            //                           Toast.makeText(AllQuizActivity.this, "Values :"+key.trim(), Toast.LENGTH_SHORT).show();
                        }
                        //             Toast.makeText(AllQuizActivity.this, "length :"+allQ.size(), Toast.LENGTH_SHORT).show();
                        quiz.setAllQuestions(allQ);
                        quizList.add(quiz);
                    }
                    progressBar.setVisibility(View.GONE);
                    ModifyAllQuizAdapter modifyAllQuizAdapter = new ModifyAllQuizAdapter(AllQuizActivity.this,quizList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(modifyAllQuizAdapter);
                    modifyAllQuizAdapter.notifyDataSetChanged();
                }else {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}