package com.example.eduhub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.HashMap;
import java.util.Map;

public class RuntimeActivity extends AppCompatActivity {

    TextView txtQuestion,txtCorrectAns,txtCountdown;
    ConstraintLayout container;
    Button btnA1,btnA2,btnA3,btnNext, btnAnswer;
    ProgressBar progressBar;
    Map<String,String>allQuestions;
    boolean clicked;
    String[] keys,ans;
    int score=0;
    int i = 0,counter = 1;
    CountDownTimer mCountDownTimer;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime);

        container = (ConstraintLayout) findViewById(R.id.runtimeContainer);

        txtQuestion = (TextView) findViewById(R.id.runtimeQuestion);
        txtCorrectAns = (TextView) findViewById(R.id.runtimeCorrectAns);

        btnNext = (Button) findViewById(R.id.btnnextQ);
        btnA1 = (Button) findViewById(R.id.runtimeAns1);
        btnA2 = (Button) findViewById(R.id.runtimeAns2);
        btnA3 = (Button) findViewById(R.id.runtimeAns3);
        btnAnswer = (Button) findViewById(R.id.btnViewAns);
        progressBar = (ProgressBar) findViewById(R.id.runtimeProgress);

        progressBar.setVisibility(View.INVISIBLE);

        allQuestions = new HashMap<>();

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {

            id = extras.getString("id");

            String allKeys = extras.getString("allKeys");
            keys = allKeys.split(",");
            keys[0] = keys[0].replace("[","");
            keys[keys.length - 1] = keys[keys.length - 1].replace("]","");

            for (String key:keys) {
                String temp = extras.getString(key.trim());
                allQuestions.put(key,temp);
            }

            displayQuest(0);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    displayQuest(counter);
                    counter++;
                }
            });
        }
    }


    private void displayQuest(int i){
        enablebuttons();

        if (i >= keys.length){
            Intent intent = new Intent(this,SummaryActivity.class);
            intent.putExtra("allQ",allQuestions.size());
            intent.putExtra("score",score);
            startActivity(intent);
            return;
        }

        String key = keys[i];
        txtQuestion.setText(key);

        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String questionID=id+key;
                Intent i = new Intent(RuntimeActivity.this, view_answer.class);
                i.putExtra("QuestionID",questionID);
                //  i.putExtra("Question", key);
                startActivity(i);
            }
        });

        String tempAns = allQuestions.get(key);
        if (tempAns != null){
            ans = tempAns.split("/");
            btnA1.setText(ans[0]);
            btnA2.setText(ans[1]);
            btnA3.setText(ans[2]);

            if (ans[3].equals("1")){
                txtCorrectAns.setText(ans[0]);
            }else if(ans[3].equals("2")){
                txtCorrectAns.setText(ans[1]);
            }else {
                txtCorrectAns.setText(ans[2]);
            }

        }

        btnA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disablebuttons();
                if (ans[3].equals("1")){
                    score++;
                    txtCorrectAns.setTextColor(Color.rgb(0,230,118));
                }else {
                    txtCorrectAns.setTextColor(Color.rgb(230,9,0));
                }
                txtCorrectAns.setVisibility(View.VISIBLE);
            }
        });

        btnA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disablebuttons();
                if (ans[3].equals("2")){
                    score++;
                    txtCorrectAns.setTextColor(Color.rgb(0,230,118));
                }else {
                    txtCorrectAns.setTextColor(Color.rgb(230,9,0));
                }
                txtCorrectAns.setVisibility(View.VISIBLE);
            }
        });

        btnA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disablebuttons();
                if (ans[3].equals("3")){
                    score++;
                    txtCorrectAns.setTextColor(Color.rgb(0,230,118));
                }else {
                    txtCorrectAns.setTextColor(Color.rgb(230,9,0));
                }
                txtCorrectAns.setVisibility(View.VISIBLE);
            }
        });

        //Go into Profile
       /* btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),view_answer.class);
                startActivity(i);
                setContentView(R.layout.activity_view_answer);

            }
        });
*/
        txtCorrectAns.setVisibility(View.INVISIBLE);

    }
    public void disablebuttons(){
        btnA1.setEnabled(false);
        btnA2.setEnabled(false);
        btnA3.setEnabled(false);
        btnAnswer.setEnabled(true);
    }
    public  void enablebuttons(){
        btnA1.setEnabled(true);
        btnA2.setEnabled(true);
        btnA3.setEnabled(true);
        btnAnswer.setEnabled(false);
    }
}