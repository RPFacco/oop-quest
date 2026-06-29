package com.rpfacco.oopquest.game;

import com.badlogic.gdx.Game;
import com.rpfacco.oopquest.game.data.loader.EnemyLoader;

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

    public void resetGame() {
        gameState.reset();
        EnemyLoader.clearCache();
    }
}