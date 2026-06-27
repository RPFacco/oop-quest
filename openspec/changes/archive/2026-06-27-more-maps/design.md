## Context

The game currently has 1 map (`map01.tmx`, 20×15 tiles, 640×480 px) with all four connections set to `null` in `maps.json`. The player is clamped to the map bounds — they cannot leave. The game design specifies 3 interconnected maps for v1. No transition code exists; the `MapData`/`MapEntry`/`MapConnections` data model and `MapLoader` already support multiple maps and directional connections.

**Current flow:** `show()` loads startMap → `render()` clamps player to `[0..MAP_WIDTH]` × `[0..MAP_HEIGHT]`

## Goals / Non-Goals

**Goals:**
- Add 2 new Tiled map files (`map02.tmx`, `map03.tmx`) with distinct visual appearance
- Expand `tileset.png` with 2-3 additional colored rectangle tiles for visual variety
- Implement map-to-map transitions when the player reaches a connected border
- Spawn the player on the opposite border of the destination map, preserving relative position on the perpendicular axis
- Support bidirectional travel (player can return to the previous map)
- Connections use direction priority order: north > south > east > west (resolves corner cases)
- Transition is instantaneous (no loading screen or fade)
- Player always starts on `map01`

**Non-Goals:**
- NPCs, enemies, or quizzes on the new maps
- Obstacles or collision layers
- Camera scrolling (map fits screen)
- Animated transitions or loading indicators
- Persistence of player state across transitions beyond position (inventory, lives, etc.)
- Procedural map generation

## Decisions

### Decision 1: Transition detection at map border

Instead of checking click targets or pre-empting movement, transitions trigger when the player's position actually reaches a map edge during normal movement.

**Rationale:** Most natural feel — the player walks to the edge and "passes through." Avoids the disconnect of teleporting before reaching the border.

**Alternatives considered:**
- Click-target check: If the target position is beyond a connected border, transition immediately. Rejected because it feels disjointed — the player stops short.
- Pre-check before movement: Simulated. Rejected for same reason.

### Decision 2: Preserve relative perpendicular position

When transitioning horizontally (east/west), the player's Y position is carried over to the new map. When transitioning vertically (north/south), the X position is carried over. Both are clamped to the valid range `[0..MAP_HEIGHT-player.height]` or `[0..MAP_WIDTH-player.width]`.

**Rationale:** Creates spatial continuity — moving right along the top edge of map01 places you at the top of map02's left edge.

### Decision 3: Bidirectional connections

If map01.east = "map02", then map02.west = "map01". Same for map02↔map03.

**Rationale:** Player can explore freely and return. The maps.json data model already supports this — it's a configuration choice.

### Decision 4: Fixed direction priority for corners

Priority order: north → south → east → west. If the player is in a corner position triggering multiple edges, the highest-priority matching connection wins.

**Rationale:** Deterministic behavior in the rare case of corner triggering. The specific order is arbitrary but consistent.

### Decision 5: Visual variety via expanded tileset

The current `tileset.png` has 1 tile (32×32). We add 2-3 additional colored rectangles (e.g., green, yellow, purple) as new tile IDs. Each map uses a different tile ID for its ground layer to visually distinguish maps.

**Rationale:** Zero code changes for visuals — just update the tileset image and the .tmx CSV data. The rendering pipeline is unchanged.

**Alternatives considered:**
- Procedural tinting of the single tile: Requires shader code change, more complex.
- Multiple tileset images per map: More files, needs .tmx tileset path changes per map.

### Decision 6: Instant transition

No fade, loading bar, or screen flash. The map switches in a single frame.

**Rationale:** Maps are small (20×15 tiles), TmxMapLoader is synchronous, and the load time is negligible (<1 frame). A fade would add complexity with no gameplay value at this stage.

## Risks / Trade-offs

| Risk | Mitigation |
|------|-----------|
| Player target persists after transition → player walks back through the border they just crossed | Clear `player.moving` (or set target to current position) on transition |
| Player appears inside a wall/obstacle on the new map | Non-goal for now (no obstacles in this change); future consideration |
| Simultaneous border contact at diagonals could flicker | Fixed priority ensures exactly one transition per frame; the new position moves the player off the border edge immediately |
| Loading the same map repeatedly creates garbage objects | Acceptable at this scale; optimize with caching if profiling shows issues later |
