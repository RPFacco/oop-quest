package com.rpfacco.oopquest.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.rpfacco.oopquest.game.data.model.EnemyEntity;
import com.rpfacco.oopquest.game.data.model.MoveEntity;
import com.rpfacco.oopquest.game.data.model.Player;
import com.rpfacco.oopquest.game.data.model.ProjectileEntity;
import com.rpfacco.oopquest.game.data.model.QuizData;
import com.rpfacco.oopquest.game.data.loader.QuizLoader;

public class GameplayScreen extends BaseScreen {

    private MapManager mapManager;
    private WorldRenderer worldRenderer;
    private InputController inputController;
    private Player player;
    private NpcSystem npcSystem;
    private EnemySystem enemySystem;
    private ProjectileSystem projectileSystem;
    private InputHandler inputHandler;
    private Rectangle playerRect;
    private Rectangle entityRect;
    private boolean initialized;
    private boolean leaving;
    private boolean gameOver;

    public GameplayScreen(OopQuest app) {
        super(app);
    }

    @Override
    public void show() {
        if (initialized) return;
        initialized = true;
        super.show();
        font.getData().setScale(2);

        npcSystem = new NpcSystem();
        enemySystem = new EnemySystem();
        projectileSystem = new ProjectileSystem();

        mapManager = new MapManager(npcSystem, enemySystem, projectileSystem);
        mapManager.loadMap(mapManager.getStartMap());

        float playerX = GameConfig.MAP_WIDTH / 2f - 12;
        float playerY = GameConfig.MAP_HEIGHT / 2f - 12;
        player = new Player(playerX, playerY);

        playerRect = new Rectangle();
        entityRect = new Rectangle();
        worldRenderer = new WorldRenderer();
        inputHandler = new InputHandler(viewport);
        inputController = new InputController(inputHandler, player, enemySystem, projectileSystem);
    }

    @Override
    public void render(float delta) {
        InputResult input = inputController.handleInput(delta);

        if (input.escape()) {
            app.getGameState().reset();
            dispose();
            app.setScreen(new MainMenuScreen(app));
            return;
        }

        player.update(delta);
        enemySystem.update(delta);
        enemySystem.updateShooting(player, delta, projectileSystem);
        projectileSystem.update(player, delta, enemySystem.getAliveEnemies(), this::onProjectileHit, this::onEnemyDeath);

        if (gameOver) {
            app.getGameState().reset();
            dispose();
            app.setScreen(new GameOverScreen(app));
            return;
        }

        checkMoveEntityOverlap();
        playerRect.set(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        npcSystem.checkProximity(playerRect, app.getGameState(), this::onNpcTrigger);
        player.clampToBounds();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        worldRenderer.renderMap(camera, mapManager);
        worldRenderer.renderHud(batch, camera, font, app.getGameState());
        worldRenderer.renderEntities(camera, mapManager, player, app.getGameState(), npcSystem, enemySystem, projectileSystem);
    }

    private void checkMoveEntityOverlap() {
        Array<MoveEntity> entities = mapManager.getMoveEntities();
        if (entities == null) return;

        playerRect.set(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        for (MoveEntity me : entities) {
            entityRect.set(me.getX(), me.getY(), me.getWidth(), me.getHeight());
            if (playerRect.overlaps(entityRect)) {
                mapManager.transitionTo(me, player);
                return;
            }
        }
    }

    private void onNpcTrigger(String quizId, QuizData quiz) {
        player.setTarget(player.getX(), player.getY());
        app.setScreen(new QuizScreen(app, this, quizId, quiz));
    }

    private void onEnemyDeath(EnemyEntity e) {
        String quizId = e.getQuizId();
        if (quizId == null) return;
        if (app.getGameState().isCompleted(quizId)) return;
        QuizData quiz = QuizLoader.load().get(quizId);
        if (quiz != null) {
            app.setScreen(new QuizScreen(app, this, quizId, quiz));
        }
    }

    private void onProjectileHit(ProjectileEntity p) {
        if (player.getInvincibleTimer() > 0) return;
        app.getGameState().takeDamage();
        player.setInvincibleTimer(1f);
        if (app.getGameState().getLives() <= 0) {
            gameOver = true;
        }
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        mapManager.dispose();
        worldRenderer.dispose();
        super.dispose();
    }
}
