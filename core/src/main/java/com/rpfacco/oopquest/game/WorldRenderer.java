package com.rpfacco.oopquest.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.rpfacco.oopquest.game.data.model.MoveEntity;

public class WorldRenderer {

    private final ShapeRenderer shapeRenderer;

    public WorldRenderer() {
        this.shapeRenderer = new ShapeRenderer();
    }

    public void renderMap(OrthographicCamera camera, MapManager mapManager) {
        mapManager.getMapRenderer().setView(camera);
        mapManager.getMapRenderer().render();
    }

    public void renderHud(SpriteBatch batch, OrthographicCamera camera, BitmapFont font, GameState gameState) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "Lives: " + gameState.getLives(), 20, GameConfig.MAP_HEIGHT - 20);
        batch.end();
    }

    public void renderEntities(OrthographicCamera camera, MapManager mapManager, Player player,
                               GameState gameState, NpcSystem npcSystem, EnemySystem enemySystem,
                               ProjectileSystem projectileSystem) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        Array<MoveEntity> entities = mapManager.getMoveEntities();
        if (entities != null) {
            shapeRenderer.setColor(1, 215f / 255, 0, 1);
            for (MoveEntity me : entities) {
                shapeRenderer.rect(me.getX(), me.getY(), me.getWidth(), me.getHeight());
            }
        }

        npcSystem.render(shapeRenderer, gameState);
        enemySystem.render(shapeRenderer);
        enemySystem.renderHealthBars(shapeRenderer);
        projectileSystem.render(shapeRenderer);

        if (!(player.getInvincibleTimer() > 0 && (int)(player.getInvincibleTimer() * 10) % 2 == 0)) {
            shapeRenderer.setColor(0.6f, 0.2f, 0.8f, 1);
            shapeRenderer.rect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        }

        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
