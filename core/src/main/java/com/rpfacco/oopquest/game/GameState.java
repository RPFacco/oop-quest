package com.rpfacco.oopquest.game;

import java.util.HashSet;
import java.util.Set;

public class GameState {
    public int lives;
    public Set<String> completedQuizzes;

    public GameState() {
        this.lives = 5;
        this.completedQuizzes = new HashSet<>();
    }

    public void reset() {
        this.lives = 5;
        this.completedQuizzes.clear();
    }
}
