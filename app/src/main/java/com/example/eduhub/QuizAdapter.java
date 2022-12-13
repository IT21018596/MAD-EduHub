package com.example.eduhub;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eduhub.Database.Quiz;

import java.util.ArrayList;
import java.util.Map;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.MyViewHolder> {
    public static final String SINGLE_PRODUCT_TAG = "com.example.eduhub.quizList";
    Context context;
    String level;
    ArrayList<Quiz> list;

    public QuizAdapter(Context context, ArrayList<Quiz> list,String level) {
        this.context = context;
        this.list = list;
        this.level = level;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_question_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Quiz quiz = list.get(position);
        holder.txtTitle.setText(quiz.getTitle());
        holder.txtDesc.setText(quiz.getDescription());
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context,RuntimeActivity.class);
                i.putExtra("id", quiz.getQuizId().toString());

                for (Map.Entry<String,String>set:quiz.getAllQuestions().entrySet()){
                    i.putExtra(set.getKey().toString(),set.getValue().toString());
                }
                i.putExtra("allKeys",quiz.getAllQuestions().keySet().toString());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtDesc;
        Button btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.mainQuestionCardTitle);
            txtDesc = itemView.findViewById(R.id.mainQuestionCardDesc);
            btn = itemView.findViewById(R.id.mainQuestionCardBtn);
        }
    }

}

