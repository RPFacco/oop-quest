package com.jogoopenspec.game;

import com.badlogic.gdx.Game;

public class JogoOpenSpec extends Game {
    private GameState gameState;

    @Override
    public void create() {
        gameState = new GameState();
        setScreen(new MainMenuScreen(this));
    }

    public GameState getGameState() {
        return gameState;
    }
}