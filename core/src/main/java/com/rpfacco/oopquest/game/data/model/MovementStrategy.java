package com.rpfacco.oopquest.game.data.model;

public interface MovementStrategy {
    void update(EnemyEntity entity, float delta);
}
