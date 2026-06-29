package com.rpfacco.oopquest.game.data.model;

public class NpcEntity {

    private float x;
    private float y;
    private float width;
    private float height;
    private String quizId;

    public NpcEntity() {}

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    public float getWidth() { return width; }
    public void setWidth(float width) { this.width = width; }
    public float getHeight() { return height; }
    public void setHeight(float height) { this.height = height; }
    public String getQuizId() { return quizId; }
    public void setQuizId(String quizId) { this.quizId = quizId; }
}
