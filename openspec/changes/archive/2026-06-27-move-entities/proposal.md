## Why

The current border-based transition system causes ping-pong bugs: the player spawns on the edge of the new map then immediately triggers the return transition. Move entities replace border detection with placed zones, giving full control over spawn positions to avoid re-triggering.

## What Changes

- **New** `MoveEntity` data model — replaces `MapConnections` entirely
- **BREAKING** `maps.json` schema — `connections` object replaced by `moveEntities` array
- **New** Move entity rendering — colored rectangles drawn on the map
- **New** Overlap detection — player touching a move entity triggers transition
- **Removed** `MapConnections.java` — no longer needed
- **Removed** Border transition logic — borders only clamp, never transition
- **Removed** Direction priority code — corners are irrelevant without border transitions
- **Updated** `GameplayScreen` — render move entities, check overlap instead of border contact
- **Updated** `maps.json` for all 3 maps — define move entities with spawn positions

## Capabilities

### New Capabilities
- `move-entities`: Transition zones placed at fixed map coordinates. When the player overlaps a move entity, the game loads the target map and spawns the player at the defined `spawnX`/`spawnY` position. Multiple move entities per map are supported for bidirectional travel.

### Modified Capabilities
- `map-rendering-and-movement`: The clamping requirement changes — borders now always clamp (dead ends). The "border with connection" scenario is removed.

## Impact

- `core/.../data/MapConnections.java` — **deleted**
- `core/.../data/MapEntry.java` — replace `connections` with `moveEntities`
- `core/.../data/MapLoader.java` — parse `moveEntities` array instead of `connections`
- `core/.../data/MoveEntity.java` — new data class
- `core/.../GameplayScreen.java` — remove border transition, add move entity rendering + overlap checks
- `assets/data/maps.json` — replace `connections` with `moveEntities`
- `docs/game-design.md` — already updated
