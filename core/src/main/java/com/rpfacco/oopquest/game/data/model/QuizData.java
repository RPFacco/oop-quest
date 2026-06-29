package com.rpfacco.oopquest.game.data.model;

public class QuizData {

    private String question;
    private String[] choices;
    private int correct;

    public QuizData() {}

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public String[] getChoices() { return choices; }
    public void setChoices(String[] choices) { this.choices = choices; }
    public int getCorrect() { return correct; }
    public void setCorrect(int correct) { this.correct = correct; }
}
