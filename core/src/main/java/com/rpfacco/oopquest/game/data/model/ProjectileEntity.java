package com.rpfacco.oopquest.game.data.model;

public class ProjectileEntity {

    private float x;
    private float y;
    private float vx;
    private float vy;
    private float speed;
    private float size;
    private boolean alive;

    public ProjectileEntity() {}

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    public float getVx() { return vx; }
    public void setVx(float vx) { this.vx = vx; }
    public float getVy() { return vy; }
    public void setVy(float vy) { this.vy = vy; }
    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }
    public float getSize() { return size; }
    public void setSize(float size) { this.size = size; }
    public boolean isAlive() { return alive; }
    public void setAlive(boolean alive) { this.alive = alive; }
}
