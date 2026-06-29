package com.rpfacco.oopquest.game;

import com.badlogic.gdx.utils.Array;
import com.rpfacco.oopquest.game.data.model.EnemyEntity;
import com.rpfacco.oopquest.game.data.model.ProjectileEntity;

public interface ShootPattern {
    Array<ProjectileEntity> generate(EnemyEntity enemy, Player player, float delta);
}
