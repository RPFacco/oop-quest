package com.rpfacco.oopquest.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonValue;
import com.rpfacco.oopquest.game.data.model.MovementStrategy;
import com.rpfacco.oopquest.game.data.model.ShootPattern;

public class StrategyFactory {

    public static MovementStrategy createMovement(String type, JsonValue config) {
        switch (type) {
            case "waypoint":
                return WaypointMovement.fromJson(config);
            default:
                Gdx.app.error("StrategyFactory", "Unknown movement type: " + type);
                return null;
        }
    }

    public static ShootPattern createShootPattern(String type, JsonValue config) {
        switch (type) {
            case "burst":
                return BurstPattern.fromJson(config);
            default:
                Gdx.app.error("StrategyFactory", "Unknown shoot type: " + type);
                return null;
        }
    }
}
