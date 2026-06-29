package com.rpfacco.oopquest.game;

import java.util.HashSet;
import java.util.Set;

public class GameState {

    private int lives;
    private Set<String> completedQuizzes;

    public GameState() {
        this.lives = GameConfig.LIVES;
        this.completedQuizzes = new HashSet<>();
    }

    public int getLives() {
        return lives;
    }

    public void takeDamage() {
        lives--;
    }

    public boolean isCompleted(String quizId) {
        return completedQuizzes.contains(quizId);
    }

    public void markCompleted(String quizId) {
        completedQuizzes.add(quizId);
    }

    public int getCompletedCount() {
        return completedQuizzes.size();
    }

    public void reset() {
        this.lives = GameConfig.LIVES;
        this.completedQuizzes.clear();
    }
}
