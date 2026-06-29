package com.rpfacco.oopquest.game.data.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.rpfacco.oopquest.game.StrategyFactory;
import com.rpfacco.oopquest.game.data.model.EnemyEntity;

import java.util.HashMap;
import java.util.Map;

public class EnemyLoader {

    private static Map<String, Array<EnemyEntity>> cache;

    public static void clearCache() {
        cache = null;
    }

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
                int hp = enemyVal.has("hp") ? enemyVal.getInt("hp") : 8;
                enemy.setHp(hp);
                enemy.setMaxHp(hp);
                enemy.setQuizId(enemyVal.has("quizId") ? enemyVal.getString("quizId") : null);

                JsonValue movement = enemyVal.get("movement");
                if (movement != null) {
                    String type = movement.getString("type");
                    enemy.setStrategy(StrategyFactory.createMovement(type, movement));
                }

                JsonValue shoot = enemyVal.get("shoot");
                if (shoot != null) {
                    String type = shoot.getString("type");
                    enemy.setShootPattern(StrategyFactory.createShootPattern(type, shoot));
                }

                enemies.add(enemy);
            }
            cache.put(mapId, enemies);
        }
        return cache;
    }
}
