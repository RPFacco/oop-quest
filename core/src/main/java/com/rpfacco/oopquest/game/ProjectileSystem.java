package com.rpfacco.oopquest.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.rpfacco.oopquest.game.data.ProjectileEntity;

import java.util.function.Consumer;

public class ProjectileSystem {

    private static final float MAP_WIDTH = 1920;
    private static final float MAP_HEIGHT = 1080;

    private Array<ProjectileEntity> projectiles;

    public ProjectileSystem() {
        this.projectiles = new Array<>();
    }

    public void update(Player player, float delta, Consumer<ProjectileEntity> onHit) {
        for (int i = projectiles.size - 1; i >= 0; i--) {
            ProjectileEntity p = projectiles.get(i);
            if (!p.alive) {
                projectiles.removeIndex(i);
                continue;
            }

            p.x += p.vx * p.speed * delta;
            p.y += p.vy * p.speed * delta;

            if (p.x < -p.size || p.x > MAP_WIDTH + p.size
                    || p.y < -p.size || p.y > MAP_HEIGHT + p.size) {
                p.alive = false;
                projectiles.removeIndex(i);
                continue;
            }

            if (circleRectCollision(p, player)) {
                onHit.accept(p);
                p.alive = false;
                projectiles.removeIndex(i);
            }
        }
    }

    private boolean circleRectCollision(ProjectileEntity p, Player player) {
        float cx = p.x;
        float cy = p.y;
        float r = p.size / 2f;

        float closestX = Math.max(player.x, Math.min(cx, player.x + player.width));
        float closestY = Math.max(player.y, Math.min(cy, player.y + player.height));

        float dx = cx - closestX;
        float dy = cy - closestY;
        return dx * dx + dy * dy <= r * r;
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(1, 0, 0, 1);
        for (ProjectileEntity p : projectiles) {
            if (p.alive) {
                shapeRenderer.circle(p.x, p.y, p.size / 2f);
            }
        }
    }

    public void add(ProjectileEntity projectile) {
        projectiles.add(projectile);
    }

    public void clear() {
        projectiles.clear();
    }
}
