package com.example.eduhub;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eduhub.Database.Quiz;

import java.util.ArrayList;

public class questionAdapter extends RecyclerView.Adapter<questionAdapter.ViewHolder> {
    private ArrayList<Quiz> questionlist;
    int pos;
    public questionAdapter(ArrayList<Quiz> questionlist) {
        this.questionlist = questionlist;
    }

    @NonNull
    @Override
    public questionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_ticker,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull questionAdapter.ViewHolder holder, int position) {
        holder.layout.setVisibility(View.VISIBLE);
        holder.btnClose.setText("Remove");
        holder.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionlist.remove(position);
                holder.layout.setVisibility(View.INVISIBLE);
            }
        });
        holder.btnAdd.setVisibility(View.INVISIBLE);
        holder.question.setText(questionlist.get(position).getQuiz());
        String[] anslist = questionlist.get(position).getAns().split("/");
        holder.a1.setText(anslist[0]);
        holder.a2.setText(anslist[1]);
        holder.a3.setText(anslist[2]);
        String correctAns = Integer.toString(questionlist.get(position).getCurrectAns());
        holder.ca.setText(correctAns);

    }

    @Override
    public int getItemCount() {
        return questionlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText question,a1,a2,a3,ca;
        Button btnAdd,btnClose;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.qlayoutContainer);
            question = itemView.findViewById(R.id.addQCardTitle);
            a1 = itemView.findViewById(R.id.addQ1);
            a2 = itemView.findViewById(R.id.addQ2);
            a3 = itemView.findViewById(R.id.addQ3);
            ca = itemView.findViewById(R.id.inpCorrectAns);

            btnAdd = itemView.findViewById(R.id.btnpopupAdd);
            btnClose = itemView.findViewById(R.id.btnpopupClose);
        }
    }
}
