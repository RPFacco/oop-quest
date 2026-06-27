## Context

The game currently uses border-based map transitions (from the more-maps change). When the player reaches a map edge that has a connection, they transition to the connected map and spawn on the opposite edge. This causes a ping-pong bug: spawning at x=0 on map02 immediately triggers the west connection back to map01.

The current code has `checkMapTransitions()` in `GameplayScreen` with direction priority logic, and `MapConnections.java` as the data model.

## Goals / Non-Goals

**Goals:**
- Replace `MapConnections` with a `MoveEntity` data model
- Replace `maps.json` `connections` with `moveEntities` array
- Render move entities as colored rectangles on the map
- Transition on player overlap with a move entity (AABB collision)
- Spawn player at the coordinates defined by the move entity, away from return entities
- Support multiple move entities per map
- Remove all border transition code
- Delete `MapConnections.java`
- Update all 3 maps' `maps.json` entries with proper move entities

**Non-Goals:**
- Visual variety between move entities (all same color)
- Animated transitions or feedback
- NPC interaction with move entities
- Any change to the tile rendering pipeline

## Decisions

### Decision 1: Move entity defined fully in maps.json
Each move entity contains `x`, `y`, `width`, `height`, `targetMap`, `spawnX`, `spawnY`. No `.tmx` objects needed.

**Rationale:** Single source of truth for map/transition data. No need to edit Tiled files for transitions.

### Decision 2: AABB overlap detection
Transition triggers when the player's bounding box intersects the move entity's rectangle. Uses libGDX's `Rectangle.overlaps()`.

**Rationale:** Simple, reliable, handles partial overlap. Player walks "into" the entity.

### Decision 3: All move entities render in the same color
Golden yellow `(255, 215, 0)` — visible on all 3 tile colors (green, blue, gold).

**Rationale:** Player just needs to know "this is a transport zone." Color distinction per destination would add complexity with no gameplay value at this stage.

### Decision 4: Spawn coordinates are explicit
Each move entity defines exactly where the player appears on the target map. No relative position math.

**Rationale:** Complete control to avoid re-triggering. The developer places the spawn far from any return move entities.

### Decision 5: Delete MapConnections.java
The class is no longer referenced — `connections` field removed from `MapEntry`, and border transition logic removed from `GameplayScreen`.

**Rationale:** Dead code elimination. The user explicitly requested no useless files.

## Risks / Trade-offs

| Risk | Mitigation |
|------|-----------|
| Player overlaps two move entities simultaneously | Deterministic: check in list order, first overlap wins (like priority) |
| Player target persists after transition → walks back into move entity | Clear target on transition |
| Move entity placed off-screen or partially | Developer responsibility to place within map bounds; no code guard |
