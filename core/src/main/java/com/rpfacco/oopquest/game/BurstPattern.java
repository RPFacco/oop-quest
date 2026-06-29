package com.rpfacco.oopquest.game;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.rpfacco.oopquest.game.data.model.EnemyEntity;
import com.rpfacco.oopquest.game.data.model.Player;
import com.rpfacco.oopquest.game.data.model.ProjectileEntity;
import com.rpfacco.oopquest.game.data.model.ShootPattern;

public class BurstPattern implements ShootPattern {

    private static final float BURST_INTERVAL = 1f / 4f;

    private int burstSize;
    private float cooldown;
    private float timer;
    private int burstIndex;
    private boolean onCooldown;
    private final Array<ProjectileEntity> resultBuffer = new Array<>();

    public BurstPattern(int burstSize, float cooldown) {
        this.burstSize = burstSize;
        this.cooldown = cooldown;
        this.timer = 0;
        this.burstIndex = 0;
        this.onCooldown = false;
    }

    @Override
    public Array<ProjectileEntity> generate(EnemyEntity enemy, Player player, float delta) {
        timer += delta;

        if (onCooldown) {
            if (timer >= cooldown) {
                onCooldown = false;
                burstIndex = 0;
                timer = 0;
            }
            resultBuffer.clear();
            return resultBuffer;
        }

        if (burstIndex >= burstSize) {
            onCooldown = true;
            timer = 0;
            resultBuffer.clear();
            return resultBuffer;
        }

        float fireTime = (burstIndex == 0) ? 0 : BURST_INTERVAL;
        if (timer < fireTime) {
            resultBuffer.clear();
            return resultBuffer;
        }

        timer -= fireTime;
        burstIndex++;

        ProjectileEntity p = createProjectile(enemy, player);
        resultBuffer.clear();
        resultBuffer.add(p);
        return resultBuffer;
    }

    private ProjectileEntity createProjectile(EnemyEntity enemy, Player player) {
        float cx = enemy.getCenterX();
        float cy = enemy.getCenterY();
        float px = player.getCenterX();
        float py = player.getCenterY();

        float dx = px - cx;
        float dy = py - cy;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist == 0) dist = 1f;
        float vx = dx / dist;
        float vy = dy / dist;

        ProjectileEntity p = new ProjectileEntity();
        p.setX(cx);
        p.setY(cy);
        p.setVx(vx);
        p.setVy(vy);
        p.setSpeed(enemy.getBulletSpeed());
        p.setSize(16);
        p.setAlive(true);
        return p;
    }

    public static BurstPattern fromJson(JsonValue config) {
        int burstSize = config.getInt("burstSize");
        float cooldown = config.getFloat("cooldown");
        return new BurstPattern(burstSize, cooldown);
    }
}
