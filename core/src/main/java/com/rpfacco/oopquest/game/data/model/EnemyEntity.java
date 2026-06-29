package com.rpfacco.oopquest.game.data.model;

public class EnemyEntity {

    private float x;
    private float y;
    private float width;
    private float height;
    private float speed;
    private float bulletSpeed;
    private int hp = 8;
    private int maxHp = 8;
    private boolean alive = true;
    private String quizId;
    private MovementStrategy strategy;
    private ShootPattern shootPattern;

    public EnemyEntity() {}

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    public float getWidth() { return width; }
    public void setWidth(float width) { this.width = width; }
    public float getHeight() { return height; }
    public void setHeight(float height) { this.height = height; }
    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }
    public float getBulletSpeed() { return bulletSpeed; }
    public void setBulletSpeed(float bulletSpeed) { this.bulletSpeed = bulletSpeed; }
    public String getQuizId() { return quizId; }
    public void setQuizId(String quizId) { this.quizId = quizId; }
    public MovementStrategy getStrategy() { return strategy; }
    public void setStrategy(MovementStrategy strategy) { this.strategy = strategy; }
    public ShootPattern getShootPattern() { return shootPattern; }
    public void setShootPattern(ShootPattern shootPattern) { this.shootPattern = shootPattern; }

    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public boolean isAlive() { return alive; }

    public void takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            hp = 0;
            alive = false;
        }
    }

    public float getCenterX() {
        return x + width / 2f;
    }

    public float getCenterY() {
        return y + height / 2f;
    }
}
