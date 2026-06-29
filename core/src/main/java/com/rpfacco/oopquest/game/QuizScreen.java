package com.rpfacco.oopquest.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rpfacco.oopquest.game.data.model.QuizData;
import com.rpfacco.oopquest.game.data.loader.QuizLoader;
import com.rpfacco.oopquest.game.OopQuest;

public class QuizScreen implements Screen {

    private final OopQuest app;
    private final Screen gameplayScreen;
    private final String quizId;
    private final QuizData quiz;

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    private Rectangle[] choiceRects;
    private Vector3 touchPos;
    private GlyphLayout glyphLayout;
    private boolean leaving;
    private boolean gameOver;

    public QuizScreen(OopQuest app, Screen gameplayScreen, String quizId, QuizData quiz) {
        this.app = app;
        this.gameplayScreen = gameplayScreen;
        this.quizId = quizId;
        this.quiz = quiz;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.MAP_WIDTH, GameConfig.MAP_HEIGHT, camera);
        camera.position.set(GameConfig.MAP_WIDTH / 2f, GameConfig.MAP_HEIGHT / 2f, 0);
        camera.update();

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2);
        shapeRenderer = new ShapeRenderer();
        touchPos = new Vector3();
        glyphLayout = new GlyphLayout();

        choiceRects = new Rectangle[quiz.getChoices().length];
        float boxWidth = 800;
        float boxHeight = 60;
        float startY = 500;
        for (int i = 0; i < quiz.getChoices().length; i++) {
            choiceRects[i] = new Rectangle(
                (GameConfig.MAP_WIDTH - boxWidth) / 2f,
                startY - i * (boxHeight + 20),
                boxWidth,
                boxHeight
            );
        }
    }

    @Override
    public void render(float delta) {
        handleInput();
        if (leaving) {
            dispose();
            return;
        }
        if (gameOver) {
            Gdx.app.log("QuizScreen", "gameOver, resetting state");
            app.getGameState().reset();
            dispose();
            app.setScreen(new GameOverScreen(app));
            return;
        }

        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float questionY = GameConfig.MAP_HEIGHT - 100;
        glyphLayout.setText(font, quiz.getQuestion());
        float questionX = (GameConfig.MAP_WIDTH - glyphLayout.width) / 2f;
        font.draw(batch, quiz.getQuestion(), questionX, questionY);

        for (int i = 0; i < quiz.getChoices().length; i++) {
            Rectangle rect = choiceRects[i];
            float textX = rect.x + 20;
            float textY = rect.y + rect.height / 2f + font.getCapHeight() / 2f;
            font.draw(batch, (i + 1) + ". " + quiz.getChoices()[i], textX, textY);
        }

        GameState gameState = app.getGameState();
        font.draw(batch, "Lives: " + gameState.getLives(), 30, GameConfig.MAP_HEIGHT - 30);

        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.6f, 0.6f, 0.6f, 1);
        for (Rectangle rect : choiceRects) {
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        shapeRenderer.end();
    }

    private void handleInput() {
        if (!Gdx.input.justTouched()) return;

        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(touchPos);

        for (int i = 0; i < choiceRects.length; i++) {
            if (choiceRects[i].contains(touchPos.x, touchPos.y)) {
                GameState gameState = app.getGameState();

                if (i == quiz.getCorrect()) {
                    gameState.markCompleted(quizId);
                    leaving = true;
                    if (gameState.getCompletedCount() >= QuizLoader.load().size()) {
                        Gdx.app.log("QuizScreen", "all quizzes completed, resetting");
                        gameState.reset();
                        app.setScreen(new VictoryScreen(app));
                    } else {
                        app.setScreen(gameplayScreen);
                    }
                } else {
                    gameState.takeDamage();
                    if (gameState.getLives() <= 0) {
                        gameOver = true;
                    }
                }
                return;
            }
        }
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
        shapeRenderer.dispose();
    }
}
