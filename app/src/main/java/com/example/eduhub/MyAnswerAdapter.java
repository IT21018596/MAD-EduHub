package com.example.eduhub;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MyAnswerAdapter extends RecyclerView.Adapter<MyAnswerAdapter.MyViewHolder> {
    String userID,userEmail;
    Context context;
    ArrayList<com.example.eduhub.answers> list;

    public MyAnswerAdapter(Context context, ArrayList<com.example.eduhub.answers> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.answer,parent,false);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID=currentUser.getUid();
        userEmail = currentUser.getEmail();
        return new MyViewHolder(v);
    }
    public boolean check(String chk){
        if(chk.equals(userID)){
            return  true;
        }else
            return false;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        com.example.eduhub.answers ans = list.get(position);

        holder.email.setText(ans.getUseremail());
        holder.answer.setText(ans.getDescription());
        if(check(ans.getUserID())) {
            holder.edit.setText("Tap to edit");
            holder.itemView.setOnClickListener((v)->{
                Intent intent = new Intent(context, com.example.eduhub.edit_answer.class);
                intent.putExtra("questionID",ans.getQuestionID());
                intent.putExtra("userID",userID);
                intent.putExtra("answer",ans.getDescription());
                intent.putExtra("answerID",ans.getAnswerID());
                intent.putExtra("email",ans.getUseremail());
                context.startActivity(intent);
            });
        }else{
            holder.edit.setText("");
        }


    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView email,answer,edit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            email=itemView.findViewById(R.id.email);
            answer=itemView.findViewById(R.id.Textanswer);
            edit = itemView.findViewById(R.id.edit);

        }
    }
}
