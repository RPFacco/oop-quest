package com.rpfacco.oopquest.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class MainMenuScreen extends BaseScreen {

    public MainMenuScreen(OopQuest app) {
        super(app);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            app.resetGame();
            Gdx.app.log("MainMenuScreen", "reset -> lives=" + app.getGameState().getLives() + " quizzes=" + app.getGameState().getCompletedCount());
            app.setScreen(new GameplayScreen(app));
            dispose();
            return;
        }

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "OopQuest", 100, 200);
        font.draw(batch, "Click to Start", 100, 160);
        batch.end();
    }
}
