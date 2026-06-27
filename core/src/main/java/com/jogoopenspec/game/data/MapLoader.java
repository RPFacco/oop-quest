package com.jogoopenspec.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class MapLoader {

    public static MapData load() {
        FileHandle file = Gdx.files.internal("data/maps.json");
        JsonReader reader = new JsonReader();
        JsonValue root = reader.parse(file);

        MapData data = new MapData();
        data.startMap = root.getString("startMap");

        JsonValue mapsObj = root.get("maps");
        for (JsonValue entry = mapsObj.child; entry != null; entry = entry.next) {
            MapEntry mapEntry = new MapEntry();
            mapEntry.id = entry.name;
            mapEntry.file = entry.getString("file");

            JsonValue entities = entry.get("moveEntities");
            if (entities != null) {
                mapEntry.moveEntities = new Array<>();
                for (JsonValue me = entities.child; me != null; me = me.next) {
                    MoveEntity move = new MoveEntity();
                    move.x = me.getFloat("x");
                    move.y = me.getFloat("y");
                    move.width = me.getFloat("width");
                    move.height = me.getFloat("height");
                    move.targetMap = me.getString("targetMap");
                    move.spawnX = me.getFloat("spawnX");
                    move.spawnY = me.getFloat("spawnY");
                    mapEntry.moveEntities.add(move);
                }
            }

            data.maps.put(mapEntry.id, mapEntry);
        }

        return data;
    }
}
