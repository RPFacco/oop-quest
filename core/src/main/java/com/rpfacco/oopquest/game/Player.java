package com.rpfacco.oopquest.game;

public class Player {

    public float x;
    public float y;
    public float width = 24;
    public float height = 24;
    public float speed = 320f;
    public float invincibleTimer;

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
}
