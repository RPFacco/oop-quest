package com.rpfacco.oopquest.game;

import com.badlogic.gdx.Game;

public class OopQuest extends Game {
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