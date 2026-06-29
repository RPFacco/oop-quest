package com.rpfacco.oopquest.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameOverScreen implements Screen {

    private final OopQuest jogoGame;
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout glyphLayout;

    public GameOverScreen(OopQuest jogoGame) {
        this.jogoGame = jogoGame;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.MAP_WIDTH, GameConfig.MAP_HEIGHT, camera);
        camera.position.set(GameConfig.MAP_WIDTH / 2f, GameConfig.MAP_HEIGHT / 2f, 0);
        camera.update();

        batch = new SpriteBatch();
        font = new BitmapFont();
        glyphLayout = new GlyphLayout();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            jogoGame.setScreen(new MainMenuScreen(jogoGame));
            dispose();
            return;
        }

        Gdx.gl.glClearColor(0.15f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        font.getData().setScale(3);
        glyphLayout.setText(font, "GAME OVER");
        float titleX = (GameConfig.MAP_WIDTH - glyphLayout.width) / 2f;
        float titleY = GameConfig.MAP_HEIGHT / 2f + 50;
        font.draw(batch, "GAME OVER", titleX, titleY);

        font.getData().setScale(1.5f);
        glyphLayout.setText(font, "Click to continue");
        float clickX = (GameConfig.MAP_WIDTH - glyphLayout.width) / 2f;
        float clickY = titleY - 100;
        font.draw(batch, "Click to continue", clickX, clickY);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
