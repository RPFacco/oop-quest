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
            if (!enemy.isAlive()) continue;
            if (enemy.getStrategy() == null) continue;
            enemy.getStrategy().update(enemy, delta);
        }
    }

    public void updateShooting(Player player, float delta, ProjectileSystem projectileSystem) {
        if (enemies == null) return;

        for (EnemyEntity enemy : enemies) {
            if (!enemy.isAlive()) continue;
            if (enemy.getShootPattern() == null) continue;
            Array<ProjectileEntity> projs = enemy.getShootPattern().generate(enemy, player, delta);
            for (ProjectileEntity p : projs) {
                if (p != null) projectileSystem.add(p);
            }
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (enemies == null) return;

        for (EnemyEntity enemy : enemies) {
            if (enemy.isAlive()) {
                shapeRenderer.setColor(1, 0, 0, 1);
            } else {
                shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1);
            }
            shapeRenderer.rect(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
        }
    }

    public void renderHealthBars(ShapeRenderer shapeRenderer) {
        if (enemies == null) return;

        for (EnemyEntity enemy : enemies) {
            if (!enemy.isAlive()) continue;
            float ratio = enemy.getHp() / (float) enemy.getMaxHp();
            float barWidth = enemy.getWidth() * ratio;
            float barX = enemy.getCenterX() - barWidth / 2f;
            float barY = enemy.getY() - 8f;
            shapeRenderer.setColor(0, 1, 0, 1);
            shapeRenderer.rect(barX, barY, barWidth, 8);
        }
    }

    public EnemyEntity findNearest(Player player) {
        if (enemies == null || enemies.size == 0) return null;
        EnemyEntity nearest = null;
        float minDist = Float.MAX_VALUE;
        for (EnemyEntity enemy : enemies) {
            if (!enemy.isAlive()) continue;
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

    public Array<EnemyEntity> getAliveEnemies() {
        if (enemies == null) return null;
        Array<EnemyEntity> alive = new Array<>();
        for (EnemyEntity enemy : enemies) {
            if (enemy.isAlive()) {
                alive.add(enemy);
            }
        }
        return alive;
    }
}
