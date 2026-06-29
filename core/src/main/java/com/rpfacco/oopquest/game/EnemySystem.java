package com.rpfacco.oopquest.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.rpfacco.oopquest.game.data.ProjectileEntity;

public class EnemySystem {

    private Array<EnemyEntity> enemies;

    public void setEnemies(Array<EnemyEntity> enemies) {
        this.enemies = enemies;
    }

    public void update(float delta) {
        if (enemies == null) return;

        for (EnemyEntity enemy : enemies) {
            if (enemy.getStrategy() == null) continue;
            enemy.getStrategy().update(enemy, delta);
        }
    }

    public void updateShooting(Player player, float delta, ProjectileSystem projectileSystem) {
        if (enemies == null) return;

        for (EnemyEntity enemy : enemies) {
            if (enemy.getShootPattern() == null) continue;
            Array<ProjectileEntity> projs = enemy.getShootPattern().generate(enemy, player, delta);
            for (ProjectileEntity p : projs) {
                if (p != null) projectileSystem.add(p);
            }
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (enemies == null) return;

        shapeRenderer.setColor(1, 0, 0, 1);
        for (EnemyEntity enemy : enemies) {
            shapeRenderer.rect(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
        }
    }

    public EnemyEntity findNearest(Player player) {
        if (enemies == null || enemies.size == 0) return null;
        EnemyEntity nearest = null;
        float minDist = Float.MAX_VALUE;
        for (EnemyEntity enemy : enemies) {
            float dx = enemy.getCenterX() - player.getCenterX();
            float dy = enemy.getCenterY() - player.getCenterY();
            float dist = dx * dx + dy * dy;
            if (dist < minDist) {
                minDist = dist;
                nearest = enemy;
            }
        }
        return nearest;
    }

    public Array<EnemyEntity> getEnemies() {
        return enemies;
    }
}
