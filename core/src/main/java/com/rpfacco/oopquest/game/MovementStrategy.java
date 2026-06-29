package com.rpfacco.oopquest.game;

import com.rpfacco.oopquest.game.data.model.EnemyEntity;

public interface MovementStrategy {
    void update(EnemyEntity entity, float delta);
}
