package com.jogoopenspec.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jogoopenspec.game.JogoOpenSpec;

public class MainMenuScreen implements Screen {

    private final JogoOpenSpec jogoGame;
    private SpriteBatch batch;
    private BitmapFont font;

    public MainMenuScreen(JogoOpenSpec jogoGame) {
        this.jogoGame = jogoGame;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            GameState gs = jogoGame.getGameState();
            gs.reset();
            Gdx.app.log("MainMenuScreen", "reset -> lives=" + gs.lives + " quizzes=" + gs.completedQuizzes.size());
            jogoGame.setScreen(new GameplayScreen(jogoGame));
            dispose();
            return;
        }

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.draw(batch, "JogoOpenSpec", 100, 200);
        font.draw(batch, "Click to Start", 100, 160);
        batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
