package com.example.eduhub;

public class answers {
    private String description;
    private String userID;
    private String questionID;
    private String answerID;
    private String useremail;

    public answers() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String des) {
        description = des;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getQuestionID() {
        return questionID;
    }

    public String getAnswerID() {
        return answerID;
    }

    public void setAnswerID(String answerID) {
        this.answerID = answerID;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }
}

