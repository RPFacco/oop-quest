package com.rpfacco.oopquest.game;

import com.badlogic.gdx.math.Vector3;
import com.rpfacco.oopquest.game.data.model.EnemyEntity;
import com.rpfacco.oopquest.game.data.model.Player;
import com.rpfacco.oopquest.game.data.model.ProjectileEntity;

public class InputController {

    private final InputHandler inputHandler;
    private final Player player;
    private final EnemySystem enemySystem;
    private final ProjectileSystem projectileSystem;
    private float homingCooldown;

    public InputController(InputHandler inputHandler, Player player,
                           EnemySystem enemySystem, ProjectileSystem projectileSystem) {
        this.inputHandler = inputHandler;
        this.player = player;
        this.enemySystem = enemySystem;
        this.projectileSystem = projectileSystem;
        this.homingCooldown = 0;
    }

    public InputResult handleInput(float delta) {
        homingCooldown = Math.max(0, homingCooldown - delta);

        if (inputHandler.isEscPressed()) {
            return new InputResult(true, false, null);
        }

        boolean homingFired = false;
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
                homingFired = true;
            }
        }

        Vector3 touchPos = inputHandler.handleTouch();
        if (touchPos != null) {
            if (touchPos.x >= 0 && touchPos.x <= GameConfig.MAP_WIDTH
                    && touchPos.y >= 0 && touchPos.y <= GameConfig.MAP_HEIGHT) {
                player.setTarget(touchPos.x, touchPos.y);
            }
        }

        return new InputResult(false, homingFired, touchPos);
    }
}
