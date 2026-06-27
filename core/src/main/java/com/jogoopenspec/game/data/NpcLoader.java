package com.jogoopenspec.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.Map;

public class NpcLoader {

    private static Map<String, Array<NpcEntity>> cache;

    public static Map<String, Array<NpcEntity>> load() {
        if (cache != null) return cache;

        FileHandle file = Gdx.files.internal("data/npcs.json");
        if (!file.exists()) {
            cache = new HashMap<>();
            return cache;
        }

        JsonReader reader = new JsonReader();
        JsonValue root = reader.parse(file);

        cache = new HashMap<>();
        for (JsonValue entry = root.child; entry != null; entry = entry.next) {
            String mapId = entry.name;
            Array<NpcEntity> npcs = new Array<>();
            for (JsonValue npcVal = entry.child; npcVal != null; npcVal = npcVal.next) {
                NpcEntity npc = new NpcEntity();
                npc.x = npcVal.getFloat("x");
                npc.y = npcVal.getFloat("y");
                npc.width = npcVal.getFloat("width");
                npc.height = npcVal.getFloat("height");
                npc.quizId = npcVal.getString("quizId");
                npcs.add(npc);
            }
            cache.put(mapId, npcs);
        }
        return cache;
    }
}
