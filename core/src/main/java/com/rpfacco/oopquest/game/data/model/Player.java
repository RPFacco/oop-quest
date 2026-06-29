package com.rpfacco.oopquest.game.data.model;

import com.rpfacco.oopquest.game.GameConfig;

public class Player {

    private float x;
    private float y;
    private float width = 24;
    private float height = 24;
    private float speed = 320f;
    private float invincibleTimer;

    private float targetX;
    private float targetY;
    private boolean moving;

    private static final float ARRIVAL_DISTANCE = 2f;

    public Player(float startX, float startY) {
        this.x = startX;
        this.y = startY;
        this.targetX = startX;
        this.targetY = startY;
        this.moving = false;
    }

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public float getSpeed() { return speed; }
    public float getInvincibleTimer() { return invincibleTimer; }
    public void setInvincibleTimer(float invincibleTimer) { this.invincibleTimer = invincibleTimer; }

    public void setTarget(float tx, float ty) {
        this.targetX = tx;
        this.targetY = ty;
        this.moving = true;
    }

    public void update(float delta) {
        if (invincibleTimer > 0) invincibleTimer = Math.max(0, invincibleTimer - delta);
        if (!moving) return;

        float dx = targetX - x;
        float dy = targetY - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance <= ARRIVAL_DISTANCE) {
            x = targetX;
            y = targetY;
            moving = false;
            return;
        }

        float step = speed * delta;
        x += (dx / distance) * step;
        y += (dy / distance) * step;
    }

    public boolean isMoving() {
        return moving;
    }

    public float getCenterX() {
        return x + width / 2f;
    }

    public float getCenterY() {
        return y + height / 2f;
    }

    public void clampToBounds() {
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x + width > GameConfig.MAP_WIDTH) x = GameConfig.MAP_WIDTH - width;
        if (y + height > GameConfig.MAP_HEIGHT) y = GameConfig.MAP_HEIGHT - height;
    }
}
