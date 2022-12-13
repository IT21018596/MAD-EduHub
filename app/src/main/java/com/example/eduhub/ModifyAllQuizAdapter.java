package com.example.eduhub;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eduhub.Database.Quiz;

import java.util.ArrayList;
import java.util.Map;

public class ModifyAllQuizAdapter extends RecyclerView.Adapter<ModifyAllQuizAdapter.ViewHolder> {
    Context context;
    ArrayList<Quiz>quizList;

    public ModifyAllQuizAdapter(Context context, ArrayList<Quiz> quizList) {
        this.context = context;
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public ModifyAllQuizAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_question_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModifyAllQuizAdapter.ViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.title.setText(quiz.getTitle());
        holder.desc.setText(quiz.getDescription());
        holder.btn.setText("Modify");
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ModifyQuiz.class);
                for (Map.Entry<String,String>set:quiz.getAllQuestions().entrySet()){
                    intent.putExtra(set.getKey().toString(),set.getValue().toString());
                }
                intent.putExtra("id",quiz.getQuizId());
                intent.putExtra("title",quiz.getTitle());
                intent.putExtra("desc",quiz.getDescription());
                intent.putExtra("level",quiz.getLevel());
                intent.putExtra("allKeys",quiz.getAllQuestions().keySet().toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,desc;
        Button btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.mainQuestionCardTitle);
            desc = itemView.findViewById(R.id.mainQuestionCardDesc);
            btn = itemView.findViewById(R.id.mainQuestionCardBtn);
        }
    }
}
