## Why

The codebase has three small issues: a missing null-safety check in `MapLoader` (unlike its peer `EnemyLoader`), a bug in `WaypointMovement` that always skips the first waypoint, and a hardcoded magic number for starting lives in `GameState`.

## What Changes

- `MapLoader.load()`: add file-existence guard before JSON parsing
- `WaypointMovement.fromJson()`: fix start waypoint to index 0
- `GameState`: extract starting lives into `GameConfig.LIVES`

## Capabilities

### New Capabilities

- `null-safe-map-loader`: MapLoader handles missing files gracefully
- `waypoint-start-fix`: Enemies start at the first waypoint
- `configurable-starting-lives`: Starting lives configurable via GameConfig

### Modified Capabilities

*(none)*

## Impact

- `core/.../data/MapLoader.java`: adds null check
- `core/.../game/WaypointMovement.java`: one-line fix
- `core/.../game/GameConfig.java`: new constant
- `core/.../game/GameState.java`: use new constant
