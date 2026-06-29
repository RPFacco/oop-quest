package com.rpfacco.oopquest.game;

import com.badlogic.gdx.utils.Array;
import com.rpfacco.oopquest.game.data.model.EnemyEntity;
import com.rpfacco.oopquest.game.data.model.ProjectileEntity;

public interface ProjectileBehavior {
    void update(ProjectileEntity entity, float delta, Array<EnemyEntity> enemies);
}
