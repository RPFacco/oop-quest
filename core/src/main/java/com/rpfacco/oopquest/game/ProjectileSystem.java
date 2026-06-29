package com.rpfacco.oopquest.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.rpfacco.oopquest.game.data.model.EnemyEntity;
import com.rpfacco.oopquest.game.data.model.ProjectileEntity;

import java.util.function.Consumer;

public class ProjectileSystem {

    private static class ProjectileEntry {
        final ProjectileEntity entity;
        final ProjectileBehavior behavior;

        ProjectileEntry(ProjectileEntity entity, ProjectileBehavior behavior) {
            this.entity = entity;
            this.behavior = behavior;
        }
    }

    private Array<ProjectileEntry> entries;

    public ProjectileSystem() {
        this.entries = new Array<>();
    }

    public void update(Player player, float delta, Array<EnemyEntity> enemies, Consumer<ProjectileEntity> onHit, Consumer<EnemyEntity> onEnemyDeath) {
        for (int i = entries.size - 1; i >= 0; i--) {
            ProjectileEntry entry = entries.get(i);
            ProjectileEntity p = entry.entity;
            if (!p.isAlive()) {
                entries.removeIndex(i);
                continue;
            }

            if (entry.behavior != null) {
                entry.behavior.update(p, delta, enemies);
            }

            p.setX(p.getX() + p.getVx() * p.getSpeed() * delta);
            p.setY(p.getY() + p.getVy() * p.getSpeed() * delta);

            if (p.getX() < -p.getSize() || p.getX() > GameConfig.MAP_WIDTH + p.getSize()
                    || p.getY() < -p.getSize() || p.getY() > GameConfig.MAP_HEIGHT + p.getSize()) {
                p.setAlive(false);
                entries.removeIndex(i);
                continue;
            }

            if (entry.behavior != null) {
                float cx = p.getX();
                float cy = p.getY();
                float r = p.getSize() / 2f;
                for (int j = enemies.size - 1; j >= 0; j--) {
                    EnemyEntity e = enemies.get(j);
                    float closestX = Math.max(e.getX(), Math.min(cx, e.getX() + e.getWidth()));
                    float closestY = Math.max(e.getY(), Math.min(cy, e.getY() + e.getHeight()));
                    float dx = cx - closestX;
                    float dy = cy - closestY;
                    if (dx * dx + dy * dy <= r * r) {
                        e.takeDamage(1);
                        if (!e.isAlive() && onEnemyDeath != null) {
                            onEnemyDeath.accept(e);
                        }
                        p.setAlive(false);
                        entries.removeIndex(i);
                        break;
                    }
                }
            } else if (circleRectCollision(p, player)) {
                onHit.accept(p);
                p.setAlive(false);
                entries.removeIndex(i);
            }
        }
    }

    private boolean circleRectCollision(ProjectileEntity p, Player player) {
        float cx = p.getX();
        float cy = p.getY();
        float r = p.getSize() / 2f;

        float closestX = Math.max(player.getX(), Math.min(cx, player.getX() + player.getWidth()));
        float closestY = Math.max(player.getY(), Math.min(cy, player.getY() + player.getHeight()));

        float dx = cx - closestX;
        float dy = cy - closestY;
        return dx * dx + dy * dy <= r * r;
    }

    public void render(ShapeRenderer shapeRenderer) {
        for (ProjectileEntry entry : entries) {
            ProjectileEntity p = entry.entity;
            if (!p.isAlive()) continue;
            if (entry.behavior != null) {
                shapeRenderer.setColor(0, 0.5f, 1, 1);
                float tailLen = 100f;
                float tailX = p.getX() - p.getVx() * tailLen;
                float tailY = p.getY() - p.getVy() * tailLen;
                shapeRenderer.rectLine(tailX, tailY, p.getX(), p.getY(), 3);
            } else {
                shapeRenderer.setColor(1, 0, 0, 1);
                shapeRenderer.circle(p.getX(), p.getY(), p.getSize() / 2f);
            }
        }
    }

    public void add(ProjectileEntity projectile) {
        entries.add(new ProjectileEntry(projectile, null));
    }

    public void add(ProjectileEntity projectile, ProjectileBehavior behavior) {
        entries.add(new ProjectileEntry(projectile, behavior));
    }

    public void clear() {
        entries.clear();
    }
}
