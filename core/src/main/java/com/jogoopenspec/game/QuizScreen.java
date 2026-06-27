package com.jogoopenspec.game;

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
import com.jogoopenspec.game.data.QuizData;
import com.jogoopenspec.game.data.QuizLoader;
import com.jogoopenspec.game.JogoOpenSpec;

public class QuizScreen implements Screen {

    private static final float SCREEN_WIDTH = 1920;
    private static final float SCREEN_HEIGHT = 1080;

    private final JogoOpenSpec jogoGame;
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
    private boolean gameOver;

    public QuizScreen(JogoOpenSpec jogoGame, Screen gameplayScreen, String quizId, QuizData quiz) {
        this.jogoGame = jogoGame;
        this.gameplayScreen = gameplayScreen;
        this.quizId = quizId;
        this.quiz = quiz;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        camera.position.set(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f, 0);
        camera.update();

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2);
        shapeRenderer = new ShapeRenderer();
        touchPos = new Vector3();
        glyphLayout = new GlyphLayout();

        choiceRects = new Rectangle[quiz.choices.length];
        float boxWidth = 800;
        float boxHeight = 60;
        float startY = 500;
        for (int i = 0; i < quiz.choices.length; i++) {
            choiceRects[i] = new Rectangle(
                (SCREEN_WIDTH - boxWidth) / 2f,
                startY - i * (boxHeight + 20),
                boxWidth,
                boxHeight
            );
        }
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float questionY = SCREEN_HEIGHT - 100;
        glyphLayout.setText(font, quiz.question);
        float questionX = (SCREEN_WIDTH - glyphLayout.width) / 2f;
        font.draw(batch, quiz.question, questionX, questionY);

        for (int i = 0; i < quiz.choices.length; i++) {
            Rectangle rect = choiceRects[i];
            float textX = rect.x + 20;
            float textY = rect.y + rect.height / 2f + font.getCapHeight() / 2f;
            font.draw(batch, (i + 1) + ". " + quiz.choices[i], textX, textY);
        }

        GameState gameState = jogoGame.getGameState();
        font.draw(batch, "Lives: " + gameState.lives, 30, SCREEN_HEIGHT - 30);

        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.6f, 0.6f, 0.6f, 1);
        for (Rectangle rect : choiceRects) {
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        shapeRenderer.end();

        if (gameOver) {
            GameState gs = jogoGame.getGameState();
            Gdx.app.log("QuizScreen", "gameOver, resetting state");
            gs.reset();
            Gdx.app.log("QuizScreen", "after reset -> lives=" + gs.lives + " quizzes=" + gs.completedQuizzes.size());
            dispose();
            jogoGame.setScreen(new MainMenuScreen(jogoGame));
        }
    }

    private void handleInput() {
        if (!Gdx.input.justTouched()) return;

        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(touchPos);

        for (int i = 0; i < choiceRects.length; i++) {
            if (choiceRects[i].contains(touchPos.x, touchPos.y)) {
                GameState gameState = jogoGame.getGameState();

                if (i == quiz.correct) {
                    gameState.completedQuizzes.add(quizId);
                    if (gameState.completedQuizzes.size() >= QuizLoader.load().size()) {
                        Gdx.app.log("QuizScreen", "all quizzes completed, resetting");
                        gameState.reset();
                        jogoGame.setScreen(new MainMenuScreen(jogoGame));
                    } else {
                        jogoGame.setScreen(gameplayScreen);
                    }
                } else {
                    gameState.lives--;
                    if (gameState.lives <= 0) {
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
