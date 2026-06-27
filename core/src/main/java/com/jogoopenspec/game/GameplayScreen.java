package com.jogoopenspec.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jogoopenspec.game.data.MapData;
import com.jogoopenspec.game.data.MapEntry;
import com.jogoopenspec.game.data.MapLoader;
import com.jogoopenspec.game.data.MoveEntity;

public class GameplayScreen implements Screen {

    private static final float MAP_WIDTH = 640;
    private static final float MAP_HEIGHT = 480;

    private final Game game;
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

    public GameplayScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(MAP_WIDTH, MAP_HEIGHT, camera);
        camera.position.set(MAP_WIDTH / 2f, MAP_HEIGHT / 2f, 0);
        camera.update();

        mapData = MapLoader.load();
        currentMapId = mapData.startMap;
        loadMap(currentMapId);

        float playerX = MAP_WIDTH / 2f - 12;
        float playerY = MAP_HEIGHT / 2f - 12;
        player = new Player(playerX, playerY);

        shapeRenderer = new ShapeRenderer();
        playerRect = new Rectangle();
        entityRect = new Rectangle();
    }

    @Override
    public void render(float delta) {
        handleInput();

        player.update(delta);
        checkMoveEntityOverlap();
        clampPlayerToBounds();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        Array<MoveEntity> entities = mapData.maps.get(currentMapId).moveEntities;
        if (entities != null) {
            shapeRenderer.setColor(1, 215f / 255, 0, 1);
            for (MoveEntity me : entities) {
                shapeRenderer.rect(me.x, me.y, me.width, me.height);
            }
        }

        shapeRenderer.setColor(0.2f, 0.6f, 1f, 1);
        shapeRenderer.rect(player.x, player.y, player.width, player.height);
        shapeRenderer.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            dispose();
            game.setScreen(new MainMenuScreen(game));
            return;
        }

        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPos);

            if (touchPos.x >= 0 && touchPos.x <= MAP_WIDTH
                    && touchPos.y >= 0 && touchPos.y <= MAP_HEIGHT) {
                player.setTarget(touchPos.x, touchPos.y);
            }
        }
    }

    private void checkMoveEntityOverlap() {
        Array<MoveEntity> entities = mapData.maps.get(currentMapId).moveEntities;
        if (entities == null) return;

        playerRect.set(player.x, player.y, player.width, player.height);

        for (MoveEntity me : entities) {
            entityRect.set(me.x, me.y, me.width, me.height);
            if (playerRect.overlaps(entityRect)) {
                transitionTo(me);
                return;
            }
        }
    }

    private void transitionTo(MoveEntity me) {
        loadMap(me.targetMap);
        player.x = me.spawnX;
        player.y = me.spawnY;
        player.setTarget(player.x, player.y);
    }

    private void loadMap(String mapId) {
        if (tiledMap != null) tiledMap.dispose();
        if (mapRenderer != null) mapRenderer.dispose();

        MapEntry mapEntry = mapData.maps.get(mapId);
        tiledMap = new TmxMapLoader().load(mapEntry.file);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        currentMapId = mapId;
    }

    private void clampPlayerToBounds() {
        if (player.x < 0) player.x = 0;
        if (player.y < 0) player.y = 0;
        if (player.x + player.width > MAP_WIDTH) player.x = MAP_WIDTH - player.width;
        if (player.y + player.height > MAP_HEIGHT) player.y = MAP_HEIGHT - player.height;
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
    }
}
