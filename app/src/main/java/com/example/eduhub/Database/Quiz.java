package com.example.eduhub.Database;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class Quiz implements Parcelable {

    private String quizId;
    private String quiz;
    private String ans;
    private String level;
    private String title;
    private String description;
    private int currectAns;
    private Map<String,String>allQuestions;

    protected Quiz(Parcel in) {
        quizId = in.readString();
        quiz = in.readString();
        ans = in.readString();
        level = in.readString();
        title = in.readString();
        description = in.readString();
        currectAns = in.readInt();
    }

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    public Map<String, String> getAllQuestions() {
        return allQuestions;
    }

    public void setAllQuestions(Map<String, String> allQuestions) {
        this.allQuestions = allQuestions;
    }

    public Quiz() {

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCurrectAns() {
        return currectAns;
    }

    public void setCurrectAns(int currectAns) {
        this.currectAns = currectAns;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuiz() {
        return quiz;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{
                this.quizId,
                this.quiz,
                String.valueOf(this.allQuestions),
                this.description,
                this.title,
                String.valueOf(this.currectAns),
                this.level

        });
    }
}
