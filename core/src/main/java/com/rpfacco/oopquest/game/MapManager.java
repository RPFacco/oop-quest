package com.rpfacco.oopquest.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.rpfacco.oopquest.game.data.model.MapData;
import com.rpfacco.oopquest.game.data.model.MapEntry;
import com.rpfacco.oopquest.game.data.model.MoveEntity;
import com.rpfacco.oopquest.game.data.loader.MapLoader;
import com.rpfacco.oopquest.game.data.loader.EnemyLoader;
import com.rpfacco.oopquest.game.data.loader.NpcLoader;
import com.rpfacco.oopquest.game.data.model.Player;

public class MapManager {

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private final MapData mapData;
    private String currentMapId;
    private final NpcSystem npcSystem;
    private final EnemySystem enemySystem;
    private final ProjectileSystem projectileSystem;

    public MapManager(NpcSystem npcSystem, EnemySystem enemySystem, ProjectileSystem projectileSystem) {
        this.mapData = MapLoader.load();
        this.npcSystem = npcSystem;
        this.enemySystem = enemySystem;
        this.projectileSystem = projectileSystem;
    }

    public String getStartMap() {
        return mapData.getStartMap();
    }

    public void loadMap(String mapId) {
        if (tiledMap != null) tiledMap.dispose();
        if (mapRenderer != null) mapRenderer.dispose();

        MapEntry mapEntry = mapData.getMap(mapId);
        tiledMap = new TmxMapLoader().load(mapEntry.getFile());
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        currentMapId = mapId;

        npcSystem.setNpcs(NpcLoader.load().get(mapId));
        enemySystem.setEnemies(EnemyLoader.load().get(mapId));
        projectileSystem.clear();
    }

    public void transitionTo(MoveEntity me, Player player) {
        loadMap(me.getTargetMap());
        player.setX(me.getSpawnX());
        player.setY(me.getSpawnY());
        player.setTarget(player.getX(), player.getY());
    }

    public OrthogonalTiledMapRenderer getMapRenderer() {
        return mapRenderer;
    }

    public String getCurrentMapId() {
        return currentMapId;
    }

    public Array<MoveEntity> getMoveEntities() {
        MapEntry entry = mapData.getMap(currentMapId);
        if (entry == null) return null;
        return entry.getMoveEntities();
    }

    public void dispose() {
        if (tiledMap != null) tiledMap.dispose();
        if (mapRenderer != null) mapRenderer.dispose();
    }
}
