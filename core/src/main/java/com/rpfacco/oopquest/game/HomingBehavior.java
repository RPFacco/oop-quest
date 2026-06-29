package com.rpfacco.oopquest.game;

import com.badlogic.gdx.utils.Array;
import com.rpfacco.oopquest.game.data.model.EnemyEntity;
import com.rpfacco.oopquest.game.data.model.ProjectileEntity;

public class HomingBehavior implements ProjectileBehavior {

    private final EnemyEntity target;
    private final float turnRate;
    private boolean targetAlive;

    public HomingBehavior(EnemyEntity target, float speed, float distance) {
        this.target = target;
        this.targetAlive = true;
        this.turnRate = 2f * 2f * speed / distance;
    }

    @Override
    public void update(ProjectileEntity p, float delta, Array<EnemyEntity> enemies) {
        if (!targetAlive) return;

        if (!enemies.contains(target, true)) {
            targetAlive = false;
            return;
        }

        float dx = target.getCenterX() - p.getX();
        float dy = target.getCenterY() - p.getY();
        float targetAngle = (float) Math.atan2(dy, dx);

        float currentAngle = (float) Math.atan2(p.getVy(), p.getVx());

        float diff = targetAngle - currentAngle;
        while (diff > Math.PI) diff -= 2 * Math.PI;
        while (diff < -Math.PI) diff += 2 * Math.PI;

        float maxTurn = turnRate * delta;
        if (Math.abs(diff) > maxTurn) {
            diff = Math.signum(diff) * maxTurn;
        }

        float newAngle = currentAngle + diff;
        p.setVx((float) Math.cos(newAngle));
        p.setVy((float) Math.sin(newAngle));
    }
}
