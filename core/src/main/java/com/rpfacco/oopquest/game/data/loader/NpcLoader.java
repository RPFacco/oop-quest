package com.rpfacco.oopquest.game.data.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.rpfacco.oopquest.game.data.model.NpcEntity;

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
                npc.setX(npcVal.getFloat("x"));
                npc.setY(npcVal.getFloat("y"));
                npc.setWidth(npcVal.getFloat("width"));
                npc.setHeight(npcVal.getFloat("height"));
                npc.setQuizId(npcVal.getString("quizId"));
                npcs.add(npc);
            }
            cache.put(mapId, npcs);
        }
        return cache;
    }
}
