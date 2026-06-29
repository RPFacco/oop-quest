package com.rpfacco.oopquest.game.data.model;

import com.badlogic.gdx.utils.Array;

public class MapEntry {

    private String id;
    private String file;
    private Array<MoveEntity> moveEntities;

    public MapEntry() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFile() { return file; }
    public void setFile(String file) { this.file = file; }
    public Array<MoveEntity> getMoveEntities() { return moveEntities; }
    public void setMoveEntities(Array<MoveEntity> moveEntities) { this.moveEntities = moveEntities; }
}
