package com.rpfacco.oopquest.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

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
                enemy.x = enemyVal.getFloat("x");
                enemy.y = enemyVal.getFloat("y");
                enemy.width = enemyVal.getFloat("width");
                enemy.height = enemyVal.getFloat("height");
                enemy.speed = enemyVal.getFloat("speed");

                JsonValue waypoints = enemyVal.get("waypoints");
                enemy.waypointX = new float[waypoints.size];
                enemy.waypointY = new float[waypoints.size];
                for (int i = 0; i < waypoints.size; i++) {
                    enemy.waypointX[i] = waypoints.get(i).getFloat("x");
                    enemy.waypointY[i] = waypoints.get(i).getFloat("y");
                }

                enemy.currentWaypoint = 1;
                enemy.moving = true;
                enemies.add(enemy);
            }
            cache.put(mapId, enemies);
        }
        return cache;
    }
}
