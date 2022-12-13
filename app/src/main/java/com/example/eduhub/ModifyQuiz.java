package com.example.eduhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eduhub.Database.Quiz;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModifyQuiz extends AppCompatActivity {

    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;

    ArrayList<Quiz> quizList =new ArrayList<>();
    Button closeQ,btnAddQ,btnDelete,btnApply;
    EditText inpQTitle,inpQdesc;
    DatabaseReference dbRef;
    Map<String,String> allQuestions;
    RadioGroup rg;
    RadioButton rb1,rb2,rb3,rb;
    String[] keys;
    String title,desc,level,id;
    RecyclerView container;
    Quiz quiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_quiz);

        inpQTitle = (EditText) findViewById(R.id.inpAddQuizTitle);
        inpQdesc = (EditText) findViewById(R.id.inpAddDesc);
        rg = (RadioGroup) findViewById(R.id.radioGroupLevel);
        rb1 = (RadioButton) findViewById(R.id.radioBtnB);
        rb2 = (RadioButton) findViewById(R.id.radioBtnI);
        rb3 = (RadioButton) findViewById(R.id.radioBtnA);
        container = (RecyclerView) findViewById(R.id.qRecycler);
        btnAddQ = (Button) findViewById(R.id.btnAddQuestion);
        btnDelete = (Button) findViewById(R.id.btnCancel);
        btnApply = (Button) findViewById(R.id.btnAdd);

        dbRef = FirebaseDatabase.getInstance().getReference("Quiz");

        allQuestions = new HashMap<>();

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            id = extras.getString("id");
            title = extras.getString("title");
            desc = extras.getString("desc");
            level = extras.getString("level");
            String allKeys = extras.getString("allKeys");
            keys = allKeys.split(",");
            keys[0] = keys[0].replace("[","");
            keys[keys.length - 1] = keys[keys.length - 1].replace("]","");
            //           Toast.makeText(this, "length of the keys :"+keys.length, Toast.LENGTH_SHORT).show();

            for (String key:keys) {
//                Toast.makeText(this, "pure keys :"+key.replace(" ",""), Toast.LENGTH_SHORT).show();
                String temp = extras.getString(key.trim());
                //               Toast.makeText(this, "keys :"+temp, Toast.LENGTH_SHORT).show();
                Quiz quiz = new Quiz();
                quiz.setQuiz(key);
                quiz.setAns(temp);
                //               Toast.makeText(this, "temp is:"+temp, Toast.LENGTH_SHORT).show();
                String[] arr = temp.split("/");
                quiz.setCurrectAns(Integer.parseInt(arr[3]));
                quizList.add(quiz);
            }
            displayRecycler();
            btnAddQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder = new AlertDialog.Builder(ModifyQuiz.this);

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
                            displayRecycler();
                            quizList.add(quiz);

                        }
                    });

                }
            });
            displayData();
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbRef.child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChildren()){
                                snapshot.getRef().removeValue();

                                Intent intent = new Intent(ModifyQuiz.this,AllQuizActivity.class);
                                startActivity(intent);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
            btnApply.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    quiz  = new Quiz();
                    quiz.setTitle(inpQTitle.getText().toString().trim());
                    quiz.setDescription(inpQdesc.getText().toString().trim());
                    quiz.setQuizId(id);
                    int selection = rg.getCheckedRadioButtonId();

                    rb = findViewById(selection);

                    quiz.setLevel(rb.getText().toString().trim());

                    Map<String,String>q_and_a = new HashMap<>();
                    for(Quiz q :quizList){
                        String[] temp = q.getAns().split("/");
                        String ans= temp[0]+"/"+temp[1]+"/"+temp[2] +"/" + q.getCurrectAns();
                        q_and_a.put(q.getQuiz(),ans);
                    }

                    quiz.setAllQuestions(q_and_a);
                    dbRef = dbRef.child(id);

                    dbRef.setValue(quiz).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //                           Toast.makeText(ModifyQuiz.this, "Update Successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ModifyQuiz.this,AllQuizActivity.class);
                            startActivity(intent);
                        }
                    });

                }
            });
        }



    }
    public void displayData(){
        inpQTitle.setText(title);
        inpQdesc.setText(desc);

        switch (level){
            case "Beginner":
                rg.check(rb1.getId());
                break;
            case "Intermediate":
                rg.check(rb2.getId());
                break;
            case "Advanced":
                rg.check(rb3.getId());
                break;
        }

        displayRecycler();

    }

    private void displayRecycler() {
        questionAdapter adapter = new questionAdapter(quizList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        container.setLayoutManager(layoutManager);
        container.setItemAnimator(new DefaultItemAnimator());
        container.setAdapter(adapter);
    }

}