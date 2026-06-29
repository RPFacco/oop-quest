package com.rpfacco.oopquest.game;

import com.badlogic.gdx.math.Vector3;

public record InputResult(boolean escape, boolean homingFired, Vector3 touchTarget) {
}
