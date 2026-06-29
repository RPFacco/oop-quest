package com.rpfacco.oopquest.game.data.model;

import com.badlogic.gdx.utils.ObjectMap;

public class MapData {

    private String startMap;
    private ObjectMap<String, MapEntry> maps;

    public MapData() {
        this.maps = new ObjectMap<>();
    }

    public String getStartMap() { return startMap; }
    public void setStartMap(String startMap) { this.startMap = startMap; }
    public MapEntry getMap(String id) { return maps.get(id); }
    public ObjectMap<String, MapEntry> getMaps() { return maps; }
}
