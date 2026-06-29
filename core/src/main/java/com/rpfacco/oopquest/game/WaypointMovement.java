package com.rpfacco.oopquest.game;

import com.badlogic.gdx.utils.JsonValue;

public class WaypointMovement implements MovementStrategy {

    private static final float ARRIVAL_DISTANCE = 2f;

    private float[] waypointX;
    private float[] waypointY;
    private int currentWaypoint;

    public WaypointMovement(float[] waypointX, float[] waypointY, int startWaypoint) {
        this.waypointX = waypointX;
        this.waypointY = waypointY;
        this.currentWaypoint = startWaypoint;
    }

    @Override
    public void update(EnemyEntity entity, float delta) {
        float targetX = waypointX[currentWaypoint];
        float targetY = waypointY[currentWaypoint];

        float dx = targetX - entity.getX();
        float dy = targetY - entity.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance <= ARRIVAL_DISTANCE) {
            entity.setX(targetX);
            entity.setY(targetY);
            currentWaypoint = (currentWaypoint + 1) % waypointX.length;
            return;
        }

        float step = entity.getSpeed() * delta;
        entity.setX(entity.getX() + (dx / distance) * step);
        entity.setY(entity.getY() + (dy / distance) * step);
    }

    public static WaypointMovement fromJson(JsonValue config) {
        JsonValue waypoints = config.get("waypoints");
        float[] wpx = new float[waypoints.size];
        float[] wpy = new float[waypoints.size];
        for (int i = 0; i < waypoints.size; i++) {
            wpx[i] = waypoints.get(i).getFloat("x");
            wpy[i] = waypoints.get(i).getFloat("y");
        }
        int start = 0;
        return new WaypointMovement(wpx, wpy, start);
    }
}
