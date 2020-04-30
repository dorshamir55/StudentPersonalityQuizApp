package com.example.studentpersonalityquizapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class QuestionsDB implements Serializable {
    /**
     *  use map as data structure for questions and answers
     */
    private static final long serialVersionUID = 1L;
    private Map<Integer, String> question;
    private Map<Integer, ArrayList<String>> answers;

    public QuestionsDB(Map<Integer, String> question, Map<Integer, ArrayList<String>> answers) {
        this.question = question;
        this.answers = answers;
    }

    public Map<Integer, String> getQuestion() {
        return question;
    }

    public void setQuestion(Map<Integer, String> question) {
        this.question = question;
    }

    public Map<Integer, ArrayList<String>> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Integer, ArrayList<String>> answers) {
        this.answers = answers;
    }


}
