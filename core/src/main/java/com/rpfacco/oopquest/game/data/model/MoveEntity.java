package com.rpfacco.oopquest.game.data.model;

public class MoveEntity {

    private float x;
    private float y;
    private float width;
    private float height;
    private String targetMap;
    private float spawnX;
    private float spawnY;

    public MoveEntity() {}

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    public float getWidth() { return width; }
    public void setWidth(float width) { this.width = width; }
    public float getHeight() { return height; }
    public void setHeight(float height) { this.height = height; }
    public String getTargetMap() { return targetMap; }
    public void setTargetMap(String targetMap) { this.targetMap = targetMap; }
    public float getSpawnX() { return spawnX; }
    public void setSpawnX(float spawnX) { this.spawnX = spawnX; }
    public float getSpawnY() { return spawnY; }
    public void setSpawnY(float spawnY) { this.spawnY = spawnY; }
}
