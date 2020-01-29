package com.example.studentpersonalityquizapp;

import java.util.Map;

public class Questions {
    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private int score;
    private Map<String,Integer> answerMap;

    public Questions(String question, String answer1, String answer2, String answer3, String answer4, int score, Map<String, Integer> answerMap) {
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.score = score;
        this.answerMap = answerMap;
    }
}
