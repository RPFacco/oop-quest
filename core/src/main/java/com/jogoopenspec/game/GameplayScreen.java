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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jogoopenspec.game.data.MapData;
import com.jogoopenspec.game.data.MapEntry;
import com.jogoopenspec.game.data.MapLoader;

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

    public GameplayScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(MAP_WIDTH, MAP_HEIGHT, camera);
        camera.position.set(MAP_WIDTH / 2f, MAP_HEIGHT / 2f, 0);
        camera.update();

        MapData mapData = MapLoader.load();
        MapEntry mapEntry = mapData.maps.get(mapData.startMap);
        tiledMap = new TmxMapLoader().load(mapEntry.file);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        float playerX = MAP_WIDTH / 2f - 12;
        float playerY = MAP_HEIGHT / 2f - 12;
        player = new Player(playerX, playerY);

        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        handleInput();

        player.update(delta);
        clampPlayerToBounds();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
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
            camera.unproject(touchPos);

            if (touchPos.x >= 0 && touchPos.x <= MAP_WIDTH
                    && touchPos.y >= 0 && touchPos.y <= MAP_HEIGHT) {
                player.setTarget(touchPos.x, touchPos.y);
            }
        }
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
