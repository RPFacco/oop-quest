package com.rpfacco.oopquest.game;

import com.badlogic.gdx.utils.JsonValue;
import com.rpfacco.oopquest.game.data.EnemyEntity;

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

        float dx = targetX - entity.x;
        float dy = targetY - entity.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance <= ARRIVAL_DISTANCE) {
            entity.x = targetX;
            entity.y = targetY;
            currentWaypoint = (currentWaypoint + 1) % waypointX.length;
            return;
        }

        float step = entity.speed * delta;
        entity.x += (dx / distance) * step;
        entity.y += (dy / distance) * step;
    }

    public static WaypointMovement fromJson(JsonValue config) {
        JsonValue waypoints = config.get("waypoints");
        float[] wpx = new float[waypoints.size];
        float[] wpy = new float[waypoints.size];
        for (int i = 0; i < waypoints.size; i++) {
            wpx[i] = waypoints.get(i).getFloat("x");
            wpy[i] = waypoints.get(i).getFloat("y");
        }
        int start = Math.min(1, Math.max(0, wpx.length - 1));
        return new WaypointMovement(wpx, wpy, start);
    }
}
