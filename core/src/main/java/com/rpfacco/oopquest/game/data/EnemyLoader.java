package com.rpfacco.oopquest.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.rpfacco.oopquest.game.BurstPattern;
import com.rpfacco.oopquest.game.EnemyEntity;
import com.rpfacco.oopquest.game.MovementStrategy;
import com.rpfacco.oopquest.game.ShootPattern;
import com.rpfacco.oopquest.game.WaypointMovement;

import java.util.HashMap;
import java.util.Map;

public class EnemyLoader {

    private static Map<String, Array<EnemyEntity>> cache;

    public static Map<String, Array<EnemyEntity>> load() {
        if (cache != null) return cache;

        FileHandle file = Gdx.files.internal("data/enemies.json");
        if (!file.exists()) {
            cache = new HashMap<>();
            return cache;
        }

        JsonReader reader = new JsonReader();
        JsonValue root = reader.parse(file);

        cache = new HashMap<>();
        for (JsonValue entry = root.child; entry != null; entry = entry.next) {
            String mapId = entry.name;
            Array<EnemyEntity> enemies = new Array<>();
            for (JsonValue enemyVal = entry.child; enemyVal != null; enemyVal = enemyVal.next) {
                EnemyEntity enemy = new EnemyEntity();
                enemy.setX(enemyVal.getFloat("x"));
                enemy.setY(enemyVal.getFloat("y"));
                enemy.setWidth(enemyVal.getFloat("width"));
                enemy.setHeight(enemyVal.getFloat("height"));
                enemy.setSpeed(enemyVal.getFloat("speed"));
                enemy.setBulletSpeed(enemyVal.has("bulletSpeed") ? enemyVal.getFloat("bulletSpeed") : enemy.getSpeed());

                JsonValue movement = enemyVal.get("movement");
                if (movement != null) {
                    String type = movement.getString("type");
                    switch (type) {
                        case "waypoint":
                            enemy.setStrategy(WaypointMovement.fromJson(movement));
                            break;
                        default:
                            Gdx.app.error("EnemyLoader", "Unknown movement type: " + type);
                            break;
                    }
                }

                JsonValue shoot = enemyVal.get("shoot");
                if (shoot != null) {
                    String type = shoot.getString("type");
                    switch (type) {
                        case "burst":
                            enemy.setShootPattern(BurstPattern.fromJson(shoot));
                            break;
                        default:
                            Gdx.app.error("EnemyLoader", "Unknown shoot type: " + type);
                            break;
                    }
                }

                enemies.add(enemy);
            }
            cache.put(mapId, enemies);
        }
        return cache;
    }
}
