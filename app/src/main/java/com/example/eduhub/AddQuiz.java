package com.example.eduhub;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eduhub.Database.Quiz;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddQuiz extends AppCompatActivity {

    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;

    EditText editTxtQuizTitle, editTxtQuizDesc;
    RadioGroup radioGroupLevel,radioGroupAnswer;
    RadioButton radioBtnB, radioBtnI, radioBtnA, rb,rbCorrect;
    Button btnAdd, btnCancel,btnAddQ,closeQ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef,dbQuestionRef;
    Quiz quiz;
    RecyclerView container;

    List<String> qList = new ArrayList<>();
    List<String> aList = new ArrayList<>();
    ArrayList<Quiz>quizList =new ArrayList<>();
    //Clear user inputs
    private void clearControls() {
        editTxtQuizTitle.setText("");
        editTxtQuizDesc.setText("");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz);

        editTxtQuizTitle = findViewById(R.id.inpAddQuizTitle);
        editTxtQuizDesc = findViewById(R.id.inpAddDesc);


        container = (RecyclerView) findViewById(R.id.qRecycler);

        radioGroupLevel = findViewById(R.id.radioGroupLevel);

        radioBtnB = findViewById(R.id.radioBtnB);
        radioBtnI = findViewById(R.id.radioBtnI);
        radioBtnA = findViewById(R.id.radioBtnA);

        btnAdd = findViewById(R.id.btnAdd);
        btnAddQ = findViewById(R.id.btnAddQuestion);
        btnCancel = findViewById(R.id.btnCancel);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbRef = firebaseDatabase.getReference("Quiz");
        dbQuestionRef = firebaseDatabase.getReference("Quiz");

        quiz = new Quiz();

        //Add more Quiz

        btnAddQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

        //CREATE
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dbRef = FirebaseDatabase.getInstance().getReference().child("Quiz");
                dbQuestionRef = dbRef.child("Questions");



                //Checking whether fields are empty
                if(TextUtils.isEmpty(editTxtQuizTitle.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please enter a Question Title", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(editTxtQuizDesc.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please enter a Description", Toast.LENGTH_SHORT).show();
                }   else {

                    // get selected radio button from radioGroup
                    int selectedRadioBtn = radioGroupLevel.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    rb = (RadioButton) findViewById(selectedRadioBtn);

                    //Take inputs and assign them to std instance
                    quiz.setTitle(editTxtQuizTitle.getText().toString().trim());
                    quiz.setDescription(editTxtQuizDesc.getText().toString().trim());
                    quiz.setLevel(rb.getText().toString().trim());

                    Map<String,String>q_and_a = new HashMap<>();
                    for(Quiz q :quizList){
                        String ans= q.getAns() +"/" + q.getCurrectAns();
                        q_and_a.put(q.getQuiz(),ans);
                    }

                    quiz.setAllQuestions(q_and_a);
                    quiz.setQuizId(dbRef.push().getKey());
//                        dbRef = dbRef.child(quiz.getQuizId());
                    dbRef.child(quiz.getQuizId()).setValue(quiz).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AddQuiz.this, "Question inserted successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddQuiz.this, "Question insertion failed" + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                    //clear all text fields
                    clearControls();
                }

            }
        });


        //Go Back to Quiz List
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),QuizList.class);
                startActivity(i);
                setContentView(R.layout.activity_quiz_list);

            }

        });
    }
    public void createDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contentPage = getLayoutInflater().inflate(R.layout.row_add_ticker,null);


        EditText qTitle = (EditText) contentPage.findViewById(R.id.addQCardTitle);
        EditText q1 = (EditText) contentPage.findViewById(R.id.addQ1);
        EditText q2 = (EditText) contentPage.findViewById(R.id.addQ2);
        EditText q3 = (EditText) contentPage.findViewById(R.id.addQ3);
        closeQ = (Button) contentPage.findViewById(R.id.btnpopupClose);
        Button addQ = (Button) contentPage.findViewById(R.id.btnpopupAdd);
        EditText ca = (EditText) contentPage.findViewById(R.id.inpCorrectAns);

        dialogBuilder.setView(contentPage);
        dialog = dialogBuilder.create();
        dialog.show();

        closeQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        addQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Quiz quiz = new Quiz();
                quiz.setQuiz(qTitle.getText().toString().trim());
                String totalAns = q1.getText().toString().trim() + "/" +q2.getText().toString().trim() +"/" + q3.getText().toString().trim();
                quiz.setAns(totalAns);
                quiz.setCurrectAns(Integer.parseInt(ca.getText().toString().trim()));

                quizList.add(quiz);
                //               Toast.makeText(AddQuiz.this, quizList.toString(), Toast.LENGTH_SHORT).show();
                //              Toast.makeText(AddQuiz.this, "size :"+quizList.size(), Toast.LENGTH_SHORT).show();
                questionAdapter adapter = new questionAdapter(quizList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                container.setLayoutManager(layoutManager);
                container.setItemAnimator(new DefaultItemAnimator());
                container.setAdapter(adapter);
            }
        });
    }

}