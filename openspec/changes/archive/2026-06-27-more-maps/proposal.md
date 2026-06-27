## Why

The game design specifies 3 interconnected maps for v1, but currently only 1 exists. Adding the remaining 2 maps with a working transition system is the next step toward a playable game world.

## What Changes

- 2 new Tiled maps (`map02.tmx`, `map03.tmx`) with visually distinct tile layouts
- Expanded tileset (`tileset.png`) with additional colored tiles for visual variety
- Map transition system: when the player reaches a map border that has a connection, the game loads the connected map and spawns the player on the opposite side
- `maps.json` updated with 3 maps and bidirectional connections (map01↔map02↔map03)
- Player clamping replaced with transition checks (border with no connection still clamps)
- `GameplayScreen` refactored to support runtime map switching (load/dispose maps, track current map ID, store `MapData`)

## Capabilities

### New Capabilities
- `map-transitions`: The ability to transition between maps when the player reaches a map border. Includes border detection, map switching, and player spawning on the opposite border of the destination map while preserving relative position on the perpendicular axis.

### Modified Capabilities
- `map-rendering-and-movement`: Requirements change — the player is no longer strictly clamped to the map bounds; reaching a connected border triggers a map transition instead. The map can now change at runtime (not just load-once on startup).

## Impact

- `core/src/main/java/com/jogoopenspec/game/GameplayScreen.java` — major changes: add transition detection, map switching, state tracking
- `assets/data/maps.json` — updated with all 3 maps and bidirectional connections
- `assets/maps/map02.tmx` — new file
- `assets/maps/map03.tmx` — new file
- `assets/tileset.png` — expanded with additional tiles
- No changes to build files, dependencies, or external systems
