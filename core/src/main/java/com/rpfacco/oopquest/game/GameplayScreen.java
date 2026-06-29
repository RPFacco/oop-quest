package com.rpfacco.oopquest.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rpfacco.oopquest.game.data.MapData;
import com.rpfacco.oopquest.game.data.MapEntry;
import com.rpfacco.oopquest.game.data.MapLoader;
import com.rpfacco.oopquest.game.data.MoveEntity;
import com.rpfacco.oopquest.game.data.EnemyLoader;
import com.rpfacco.oopquest.game.data.ProjectileEntity;
import com.rpfacco.oopquest.game.data.NpcLoader;
import com.rpfacco.oopquest.game.data.QuizData;
import com.rpfacco.oopquest.game.OopQuest;

public class GameplayScreen implements Screen {

    private final OopQuest jogoGame;
    private OrthographicCamera camera;
    private Viewport viewport;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Player player;
    private ShapeRenderer shapeRenderer;
    private MapData mapData;
    private String currentMapId;
    private Rectangle playerRect;
    private Rectangle entityRect;
    private NpcSystem npcSystem;
    private EnemySystem enemySystem;
    private ProjectileSystem projectileSystem;
    private SpriteBatch batch;
    private BitmapFont font;
    private InputHandler inputHandler;
    private HudRenderer hudRenderer;
    private float homingCooldown;
    private boolean initialized;
    private boolean leaving;
    private boolean gameOver;

    public GameplayScreen(OopQuest jogoGame) {
        this.jogoGame = jogoGame;
    }

    @Override
    public void show() {
        if (initialized) return;
        initialized = true;

        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.MAP_WIDTH, GameConfig.MAP_HEIGHT, camera);
        camera.position.set(GameConfig.MAP_WIDTH / 2f, GameConfig.MAP_HEIGHT / 2f, 0);
        camera.update();

        npcSystem = new NpcSystem();
        enemySystem = new EnemySystem();
        projectileSystem = new ProjectileSystem();

        mapData = MapLoader.load();
        currentMapId = mapData.getStartMap();
        loadMap(currentMapId);

        float playerX = GameConfig.MAP_WIDTH / 2f - 12;
        float playerY = GameConfig.MAP_HEIGHT / 2f - 12;
        player = new Player(playerX, playerY);

        shapeRenderer = new ShapeRenderer();
        playerRect = new Rectangle();
        entityRect = new Rectangle();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2);

        inputHandler = new InputHandler(viewport);
        hudRenderer = new HudRenderer(batch, font);
        homingCooldown = 0f;
    }

    @Override
    public void render(float delta) {
        handleInput();
        if (leaving) {
            jogoGame.getGameState().reset();
            dispose();
            jogoGame.setScreen(new MainMenuScreen(jogoGame));
            return;
        }

        homingCooldown = Math.max(0, homingCooldown - delta);
        player.update(delta);
        enemySystem.update(delta);
        enemySystem.updateShooting(player, delta, projectileSystem);
        projectileSystem.update(player, delta, enemySystem.getAliveEnemies(), this::onProjectileHit);
        if (gameOver) {
            jogoGame.getGameState().reset();
            dispose();
            jogoGame.setScreen(new MainMenuScreen(jogoGame));
            return;
        }
        checkMoveEntityOverlap();
        playerRect.set(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        npcSystem.checkProximity(playerRect, jogoGame.getGameState(), this::onNpcTrigger);
        clampPlayerToBounds();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        hudRenderer.render(jogoGame.getGameState());
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        Array<MoveEntity> entities = mapData.getMap(currentMapId).getMoveEntities();
        if (entities != null) {
            shapeRenderer.setColor(1, 215f / 255, 0, 1);
            for (MoveEntity me : entities) {
                shapeRenderer.rect(me.getX(), me.getY(), me.getWidth(), me.getHeight());
            }
        }

        npcSystem.render(shapeRenderer, jogoGame.getGameState());
        enemySystem.render(shapeRenderer);
        enemySystem.renderHealthBars(shapeRenderer);
        projectileSystem.render(shapeRenderer);

        if (!(player.getInvincibleTimer() > 0 && (int)(player.getInvincibleTimer() * 10) % 2 == 0)) {
            shapeRenderer.setColor(0.6f, 0.2f, 0.8f, 1);
            shapeRenderer.rect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        }
        shapeRenderer.end();
    }

    private void handleInput() {
        if (inputHandler.isEscPressed()) {
            leaving = true;
            return;
        }

        if (inputHandler.isEPressed() && homingCooldown <= 0) {
            EnemyEntity target = enemySystem.findNearest(player);
            if (target != null) {
                ProjectileEntity p = new ProjectileEntity();
                p.setX(player.getCenterX());
                p.setY(player.getCenterY());
                float dx = target.getCenterX() - player.getCenterX();
                float dy = target.getCenterY() - player.getCenterY();
                float dist = (float) Math.sqrt(dx * dx + dy * dy);
                if (dist == 0) dist = 1f;
                p.setVx(-dy / dist);
                p.setVy(dx / dist);
                p.setSpeed(1800f);
                p.setSize(8);
                p.setAlive(true);
                projectileSystem.add(p, new HomingBehavior(target, 1800f, dist));
                homingCooldown = 0.5f;
            }
        }

        Vector3 touchPos = inputHandler.handleTouch();
        if (touchPos != null) {
            if (touchPos.x >= 0 && touchPos.x <= GameConfig.MAP_WIDTH
                    && touchPos.y >= 0 && touchPos.y <= GameConfig.MAP_HEIGHT) {
                player.setTarget(touchPos.x, touchPos.y);
            }
        }
    }

    private void checkMoveEntityOverlap() {
        Array<MoveEntity> entities = mapData.getMap(currentMapId).getMoveEntities();
        if (entities == null) return;

        playerRect.set(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        for (MoveEntity me : entities) {
            entityRect.set(me.getX(), me.getY(), me.getWidth(), me.getHeight());
            if (playerRect.overlaps(entityRect)) {
                transitionTo(me);
                return;
            }
        }
    }

    private void transitionTo(MoveEntity me) {
        loadMap(me.getTargetMap());
        player.setX(me.getSpawnX());
        player.setY(me.getSpawnY());
        player.setTarget(player.getX(), player.getY());
    }

    private void loadMap(String mapId) {
        if (tiledMap != null) tiledMap.dispose();
        if (mapRenderer != null) mapRenderer.dispose();

        MapEntry mapEntry = mapData.getMap(mapId);
        tiledMap = new TmxMapLoader().load(mapEntry.getFile());
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        currentMapId = mapId;

        npcSystem.setNpcs(NpcLoader.load().get(mapId));
        enemySystem.setEnemies(EnemyLoader.load().get(mapId));
        projectileSystem.clear();
    }

    private void onNpcTrigger(String quizId, QuizData quiz) {
        player.setTarget(player.getX(), player.getY());
        jogoGame.setScreen(new QuizScreen(jogoGame, this, quizId, quiz));
    }

    private void onProjectileHit(ProjectileEntity p) {
        if (player.getInvincibleTimer() > 0) return;
        jogoGame.getGameState().takeDamage();
        player.setInvincibleTimer(1f);
        if (jogoGame.getGameState().getLives() <= 0) {
            gameOver = true;
        }
    }

    private void clampPlayerToBounds() {
        if (player.getX() < 0) player.setX(0);
        if (player.getY() < 0) player.setY(0);
        if (player.getX() + player.getWidth() > GameConfig.MAP_WIDTH) player.setX(GameConfig.MAP_WIDTH - player.getWidth());
        if (player.getY() + player.getHeight() > GameConfig.MAP_HEIGHT) player.setY(GameConfig.MAP_HEIGHT - player.getHeight());
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
        if (tiledMap != null) tiledMap.dispose();
        if (mapRenderer != null) mapRenderer.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (batch != null) batch.dispose();
        if (font != null) font.dispose();
    }
}
