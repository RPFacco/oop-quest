package com.rpfacco.oopquest.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.rpfacco.oopquest.game.data.EnemyEntity;

public class EnemySystem {

    private static final float ARRIVAL_DISTANCE = 2f;

    private Array<EnemyEntity> enemies;

    public void setEnemies(Array<EnemyEntity> enemies) {
        this.enemies = enemies;
    }

    public void update(float delta) {
        if (enemies == null) return;

        for (EnemyEntity enemy : enemies) {
            if (!enemy.moving) continue;

            float targetX = enemy.waypointX[enemy.currentWaypoint];
            float targetY = enemy.waypointY[enemy.currentWaypoint];

            float dx = targetX - enemy.x;
            float dy = targetY - enemy.y;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance <= ARRIVAL_DISTANCE) {
                enemy.x = targetX;
                enemy.y = targetY;
                enemy.currentWaypoint = (enemy.currentWaypoint + 1) % enemy.waypointX.length;
                continue;
            }

            float step = enemy.speed * delta;
            enemy.x += (dx / distance) * step;
            enemy.y += (dy / distance) * step;
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (enemies == null) return;

        shapeRenderer.setColor(1, 0, 0, 1);
        for (EnemyEntity enemy : enemies) {
            shapeRenderer.rect(enemy.x, enemy.y, enemy.width, enemy.height);
        }
    }
}
