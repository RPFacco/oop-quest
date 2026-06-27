package com.jogoopenspec.game.data;

import com.badlogic.gdx.utils.ObjectMap;

public class MapData {
    public String startMap;
    public ObjectMap<String, MapEntry> maps;

    public MapData() {
        this.maps = new ObjectMap<>();
    }
}
