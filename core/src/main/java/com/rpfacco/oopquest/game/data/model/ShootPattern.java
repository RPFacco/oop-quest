package com.rpfacco.oopquest.game.data.model;

import com.badlogic.gdx.utils.Array;

public interface ShootPattern {
    Array<ProjectileEntity> generate(EnemyEntity enemy, Player player, float delta);
}
