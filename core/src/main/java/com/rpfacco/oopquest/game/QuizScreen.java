package com.rpfacco.oopquest.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.rpfacco.oopquest.game.data.loader.DataManager;
import com.rpfacco.oopquest.game.data.model.QuizData;

public class QuizScreen extends BaseScreen {

    private final Screen gameplayScreen;
    private final String quizId;
    private final QuizData quiz;
    private final DataManager dataManager;
    private final InputHandler inputHandler;

    private ShapeRenderer shapeRenderer;
    private Rectangle[] choiceRects;
    private GlyphLayout glyphLayout;
    private float questionY;
    private boolean leaving;
    private boolean gameOver;

    public QuizScreen(OopQuest app, Screen gameplayScreen, String quizId, QuizData quiz, DataManager dataManager, InputHandler inputHandler) {
        super(app);
        this.gameplayScreen = gameplayScreen;
        this.quizId = quizId;
        this.quiz = quiz;
        this.dataManager = dataManager;
        this.inputHandler = inputHandler;
    }

    @Override
    public void show() {
        super.show();
        font.getData().setScale(4);
        shapeRenderer = new ShapeRenderer();
        glyphLayout = new GlyphLayout();

        int n = quiz.getChoices().length;
        choiceRects = new Rectangle[n];

        float pad = 20;
        float lineH = font.getLineHeight();
        float boxH = lineH + pad * 2;
        float gap = 15;

        float maxW = 0;
        for (int i = 0; i < n; i++) {
            glyphLayout.setText(font, (i + 1) + ". " + quiz.getChoices()[i]);
            if (glyphLayout.width > maxW) maxW = glyphLayout.width;
        }
        float boxW = Math.min(maxW + pad * 2, GameConfig.MAP_WIDTH - 160);

        glyphLayout.setText(font, quiz.getQuestion());
        float questionH = glyphLayout.height;
        float questionToChoicesGap = 80;
        float choicesH = n * boxH + (n - 1) * gap;
        float totalGroupH = questionH + questionToChoicesGap + choicesH;
        float groupTopY = GameConfig.MAP_HEIGHT / 2f + totalGroupH / 2f;

        questionY = groupTopY - font.getAscent();
        float boxTopY = groupTopY - questionH - questionToChoicesGap;
        float firstBoxY = boxTopY - boxH;

        for (int i = 0; i < n; i++) {
            choiceRects[i] = new Rectangle(
                (GameConfig.MAP_WIDTH - boxW) / 2f,
                firstBoxY - i * (boxH + gap),
                boxW,
                boxH
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
            app.resetGame();
            dispose();
            app.setScreen(new GameOverScreen(app));
            return;
        }

        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        GameState gameState = app.getGameState();

        font.draw(batch, "Lives: " + gameState.getLives(), 20, GameConfig.MAP_HEIGHT - 20);
        glyphLayout.setText(font, "ESC: Reset Game");
        font.draw(batch, "ESC: Reset Game", GameConfig.MAP_WIDTH - glyphLayout.width - 20, GameConfig.MAP_HEIGHT - 20);

        glyphLayout.setText(font, quiz.getQuestion());
        font.draw(batch, quiz.getQuestion(),
            (GameConfig.MAP_WIDTH - glyphLayout.width) / 2f,
            questionY);

        for (int i = 0; i < quiz.getChoices().length; i++) {
            Rectangle rect = choiceRects[i];
            String label = (i + 1) + ". " + quiz.getChoices()[i];
            glyphLayout.setText(font, label);
            float textX = rect.x + (rect.width - glyphLayout.width) / 2f;
            float textY = rect.y + rect.height / 2f + font.getCapHeight() / 2f;
            font.draw(batch, label, textX, textY);
        }

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
        Vector3 touchPos = inputHandler.handleTouch();
        if (touchPos == null) return;

        for (int i = 0; i < choiceRects.length; i++) {
            if (choiceRects[i].contains(touchPos.x, touchPos.y)) {
                GameState gameState = app.getGameState();

                if (i == quiz.getCorrect()) {
                    gameState.markCompleted(quizId);
                    leaving = true;
                    if (gameState.getCompletedCount() >= dataManager.getQuizCount()) {
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
    public void dispose() {
        shapeRenderer.dispose();
        super.dispose();
    }
}
