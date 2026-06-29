package com.rpfacco.oopquest.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class MapLoader {

    private static MapData cache;

    public static MapData load() {
        if (cache != null) return cache;

        FileHandle file = Gdx.files.internal("data/maps.json");
        if (!file.exists()) {
            return new MapData();
        }

        JsonReader reader = new JsonReader();
        JsonValue root = reader.parse(file);

        MapData data = new MapData();
        data.setStartMap(root.getString("startMap"));

        JsonValue mapsObj = root.get("maps");
        for (JsonValue entry = mapsObj.child; entry != null; entry = entry.next) {
            MapEntry mapEntry = new MapEntry();
            mapEntry.setId(entry.name);
            mapEntry.setFile(entry.getString("file"));

            JsonValue entities = entry.get("moveEntities");
            if (entities != null) {
                Array<MoveEntity> moveEntities = new Array<>();
                for (JsonValue me = entities.child; me != null; me = me.next) {
                    MoveEntity move = new MoveEntity();
                    move.setX(me.getFloat("x"));
                    move.setY(me.getFloat("y"));
                    move.setWidth(me.getFloat("width"));
                    move.setHeight(me.getFloat("height"));
                    move.setTargetMap(me.getString("targetMap"));
                    move.setSpawnX(me.getFloat("spawnX"));
                    move.setSpawnY(me.getFloat("spawnY"));
                    moveEntities.add(move);
                }
                mapEntry.setMoveEntities(moveEntities);
            }

            data.getMaps().put(mapEntry.getId(), mapEntry);
        }

        cache = data;
        return data;
    }
}
