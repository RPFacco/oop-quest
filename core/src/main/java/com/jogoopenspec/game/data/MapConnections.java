package com.jogoopenspec.game.data;

public class MapConnections {
    public String north;
    public String south;
    public String east;
    public String west;

    public MapConnections() {}

    public MapConnections(String north, String south, String east, String west) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
    }
}
