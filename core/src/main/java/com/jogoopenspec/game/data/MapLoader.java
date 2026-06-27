package com.jogoopenspec.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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

            JsonValue conn = entry.get("connections");
            MapConnections connections = new MapConnections();
            connections.north = readConnection(conn, "north");
            connections.south = readConnection(conn, "south");
            connections.east = readConnection(conn, "east");
            connections.west = readConnection(conn, "west");
            mapEntry.connections = connections;

            data.maps.put(mapEntry.id, mapEntry);
        }

        return data;
    }

    private static String readConnection(JsonValue conn, String direction) {
        if (!conn.has(direction)) return null;
        JsonValue val = conn.get(direction);
        if (val == null || val.isNull()) return null;
        return val.asString();
    }
}
