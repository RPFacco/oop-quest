package com.rpfacco.oopquest.game;

import com.badlogic.gdx.utils.Array;
import com.rpfacco.oopquest.game.data.ProjectileEntity;

public class HomingBehavior implements ProjectileBehavior {

    private final float turnRate;

    public HomingBehavior(float turnRate) {
        this.turnRate = turnRate;
    }

    @Override
    public void update(ProjectileEntity p, float delta, Array<EnemyEntity> enemies) {
        EnemyEntity target = findNearest(p, enemies);
        if (target == null) return;

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

    private EnemyEntity findNearest(ProjectileEntity p, Array<EnemyEntity> enemies) {
        EnemyEntity nearest = null;
        float minDist = Float.MAX_VALUE;
        for (EnemyEntity enemy : enemies) {
            float dx = enemy.getCenterX() - p.getX();
            float dy = enemy.getCenterY() - p.getY();
            float dist = dx * dx + dy * dy;
            if (dist < minDist) {
                minDist = dist;
                nearest = enemy;
            }
        }
        return nearest;
    }
}
