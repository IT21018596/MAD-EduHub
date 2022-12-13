package com.example.eduhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class view_answer extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<answers>list;
    ArrayList<answers>list2;
    DatabaseReference databaseReference;
    MyAnswerAdapter adapter;
    FloatingActionButton btn;
    TextView txt;
    String Ans;
    String Question_id;
    String userID,userName;
    int count;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(view_answer.this,MainActivity.class));
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answer);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID=currentUser.getUid();
        userName=currentUser.getDisplayName();
        Intent myIntent = new Intent();
        Question_id = getIntent().getStringExtra("QuestionID");
        Log.d("SAd",Question_id);
        Log.d("SAd","dxlsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");

        txt=findViewById(R.id.NoOfAnswers);
        recyclerView = findViewById(R.id.recycleView);
        btn = findViewById(R.id.sub);
        databaseReference = FirebaseDatabase.getInstance().getReference("Answers");
        list = new ArrayList<>();
        list2=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter= new MyAnswerAdapter(this,list);
        recyclerView.setAdapter(adapter);

        // String answer = FirebaseAuthException.getInstance().getCurrentUser().getUid();

        //  DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        Query chk  = databaseReference.orderByChild("questionID").equalTo(Question_id);



        chk.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    answers ans = dataSnapshot.getValue(answers.class);
                    if((ans.getUserID()).equals(userID)){
                        Ans=ans.getAnswerID();
                        Log.d("aa",Ans);
                    }Log.d("aa",ans.getUserID());
                    list.add(ans);
                }
                count= list.size();
                txt.setText("Number of Answers : "+count);
                adapter.notifyDataSetChanged();

                Query chk2  = databaseReference.orderByChild("answerID").equalTo(Ans);
                chk2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            answers ans = dataSnapshot.getValue(answers.class);

                            list2.add(ans);
                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //  count= list.size();




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(list2.size()>0){
                    Toast.makeText(getApplicationContext(),"Already Submited answer",Toast.LENGTH_LONG).show();
                    Log.d("sd","AAAAAAAAAAAAAAAAAAAAAA");
                }else{
                    Log.d("sd","ppppppppppppppppppppppppppppppppppp");
                    Log.d("sd",Question_id);

                    Intent ii=new Intent(view_answer.this, add_answer.class);
                    ii.putExtra("QuestionID", Question_id);
                    startActivity(ii);

                }
            }

        });
    }
}