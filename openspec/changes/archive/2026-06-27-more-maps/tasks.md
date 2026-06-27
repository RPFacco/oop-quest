## 1. Asset Setup

- [x] 1.1 Expand `assets/tileset.png` — add 2 additional colored rectangle tiles (32×32 px each) so the tileset has 3 tiles total (tile IDs 1, 2, 3)
- [x] 1.2 Create `assets/maps/map02.tmx` — clone `map01.tmx` but fill the ground layer with tile ID 2 (different color)
- [x] 1.3 Create `assets/maps/map03.tmx` — clone `map01.tmx` but fill the ground layer with tile ID 3 (different color)
- [x] 1.4 Update `assets/data/maps.json` — register all 3 maps with bidirectional connections (map01↔map02 via east/west, map02↔map03 via north/south)

## 2. GameplayScreen Refactoring

- [x] 2.1 Add `mapData` and `currentMapId` fields to `GameplayScreen` — store parsed `MapData` and current map ID for runtime lookups
- [x] 2.2 Modify `show()` — load `MapData` via `MapLoader.load()`, resolve startMap entry, and store both `mapData` and `currentMapId`
- [x] 2.3 Extract map loading into a reusable method `loadMap(String mapId)` — handles disposing old resources, loading new `.tmx`, creating renderer, and updating `currentMapId`

## 3. Transition Logic

- [x] 3.1 Implement border contact detection — after `player.update(delta)`, check if player position touches any map edge (accounting for player width/height)
- [x] 3.2 For each touched edge, look up the connection from the current `MapEntry` — if non-null, trigger a transition to that map
- [x] 3.3 Apply direction priority order (north > south > east > west) when multiple edges are contacted simultaneously
- [x] 3.4 Implement `transitionTo(String targetMapId, String enteredFrom)` — call `loadMap(targetMapId)`, position the player on the opposite edge preserving relative perpendicular position, clear player target
- [x] 3.5 For dead-end borders (null connection), keep existing clamp behavior — player stops at the edge

## 4. Verification

- [x] 4.1 Run the game and verify player starts on map01 with the correct tile appearance
- [x] 4.2 Click right of map01 → player walks to east border → transitions to map02 → appears on left edge at same Y position
- [x] 4.3 Click west on map02 → player returns to map01 → appears on right edge
- [x] 4.4 Click north on map02 → player transitions to map03 → appears on bottom edge at same X position
- [x] 4.5 Click south on map03 → player returns to map02 → appears on top edge
- [x] 4.6 Verify dead-end borders (e.g., north of map01, south of map03) still clamp the player
- [x] 4.7 Verify no memory leaks by rapid back-and-forth transitions (check for steady memory usage)
