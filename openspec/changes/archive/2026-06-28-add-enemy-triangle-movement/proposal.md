## Why

Map03 currently has no gameplay elements — it's an empty map reachable via transition from map02. Adding a patrolling enemy introduces a basic threat element, making the map feel alive and setting the foundation for future enemy interactions (collision, damage, combat).

## What Changes

- New `EnemyEntity` data class with position, size, speed, and waypoint-based movement pattern
- New `EnemySystem` system class for updating enemy positions and rendering
- New `EnemyLoader` for loading enemy definitions from `assets/data/enemies.json`
- New `assets/data/enemies.json` with one enemy on map03 patrolling a triangle pattern
- `GameplayScreen` updated to create and integrate `EnemySystem` into the render loop
- Enemy renders as a red rectangle (32×32) to visually distinguish from friendly NPCs (orange)
- Enemy moves at 240 px/s (75% of player speed of 320)

## Capabilities

### New Capabilities
- `enemy-movement`: Enemy entities that patrol predefined waypoint patterns on a map, with position updates each frame and visual rendering

### Modified Capabilities
<!-- No existing specs are changing -->

## Impact

- **New files:** `EnemyEntity.java`, `EnemySystem.java`, `EnemyLoader.java`, `assets/data/enemies.json`
- **Modified files:** `GameplayScreen.java` (integrates EnemySystem)
- No changes to existing NPC system, player movement, or game state
- No new dependencies
