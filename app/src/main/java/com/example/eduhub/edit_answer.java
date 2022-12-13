package com.example.eduhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class edit_answer extends AppCompatActivity {

    EditText txtAns;
    Button update,cancel,delete;
    String text,questionID,UserID,AnsID,email;
    answers ans;
    DatabaseReference upRef1,dbRef,delRef;
    private void clearControls(){
        txtAns.setText("");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_answer);
        ans= new answers();
        txtAns=findViewById(R.id.Textanswer);
        update=findViewById(R.id.update);
        cancel=findViewById(R.id.cancel);
        delete=findViewById(R.id.delete);

        Intent myIntent = new Intent();
        text = getIntent().getStringExtra("answer");
        questionID=getIntent().getStringExtra("questionID");
        UserID=getIntent().getStringExtra("userID");
        AnsID=getIntent().getStringExtra("answerID");
        email=getIntent().getStringExtra("email");
        txtAns.setText(text);
        Log.d("aa","Dddddddddddddddddddddddddddddddddddddddddddddd");
        Log.d("aa",AnsID);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upRef1 = FirebaseDatabase.getInstance().getReference().child("Answers");

                upRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(AnsID)){
                            ans.setDescription(txtAns.getText().toString().trim());
                            ans.setAnswerID(AnsID);
                            ans.setUserID(UserID);
                            ans.setQuestionID(questionID);
                            ans.setUseremail(email);

                            dbRef = FirebaseDatabase.getInstance().getReference().child("Answers").child(AnsID);
                            dbRef.setValue(ans);
                            clearControls();

                            Toast.makeText(getApplicationContext(),"Data Updated Successfully",Toast.LENGTH_LONG).show();
                            Intent ii=new Intent(edit_answer.this, view_answer.class);
                            ii.putExtra("QuestionID", questionID);
                            startActivity(ii);
                        }else{
                            Toast.makeText(getApplicationContext(),"No source to update",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }});

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delRef = FirebaseDatabase.getInstance().getReference().child("Answers");
                delRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(AnsID)){
                            dbRef = FirebaseDatabase.getInstance().getReference().child("Answers").child(AnsID);
                            dbRef.removeValue();
                            clearControls();

                            Toast.makeText(getApplicationContext(),"Data Deleted Successfully",Toast.LENGTH_LONG).show();
                            Intent ii=new Intent(edit_answer.this, view_answer.class);
                            ii.putExtra("QuestionID", questionID);
                            startActivity(ii);
                        }else{
                            Toast.makeText(getApplicationContext(),"No source to delete",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }});

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent ii=new Intent(edit_answer.this, view_answer.class);
                ii.putExtra("QuestionID", questionID);
                startActivity(ii);
            }});




    }
}