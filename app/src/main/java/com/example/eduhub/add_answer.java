package com.example.eduhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add_answer extends AppCompatActivity {

    EditText txtAnswer;
    Button submitBtn , discardBtn;
    DatabaseReference def;
    answers ans;
    String userID ;
    String questionID;
    String AnswerID;
    String userEmail;
    private void clearControls(){
        txtAnswer.setText("");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer);
        Intent myIntent = new Intent();
        questionID = getIntent().getStringExtra("QuestionID");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID=currentUser.getUid();
        userEmail=currentUser.getEmail();
        AnswerID=userID+questionID;
        Log.d("asd","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        Log.d("asd",AnswerID);

        // Log.d("as",questionID);
        txtAnswer = findViewById(R.id.Textanswer);
        submitBtn = findViewById(R.id.submit);
        discardBtn = findViewById(R.id.cancel);

        ans = new answers();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                def = FirebaseDatabase.getInstance().getReference().child("Answers");
                try{
                    ans.setDescription(txtAnswer.getText().toString().trim());
                    ans.setUserID(userID);
                    ans.setQuestionID(questionID);
                    ans.setAnswerID(AnswerID);
                    ans.setUseremail(userEmail);
                    def.child(AnswerID).setValue(ans);
                    Toast.makeText(getApplicationContext(), "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                }catch(NumberFormatException e){
                    Toast.makeText(getApplicationContext(), "Failed to upload answer", Toast.LENGTH_SHORT).show();
                }
                Intent ii=new Intent(add_answer.this, view_answer.class);
                ii.putExtra("QuestionID", questionID);
                startActivity(ii);


            }});

        discardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ii=new Intent(add_answer.this, view_answer.class);
                ii.putExtra("QuestionID", questionID);
                startActivity(ii);

            }});



    }
}