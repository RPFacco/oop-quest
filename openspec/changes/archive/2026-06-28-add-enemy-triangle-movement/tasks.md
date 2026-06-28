## 1. Data Layer

- [x] 1.1 Create `EnemyEntity.java` in `com.rpfacco.oopquest.game.data` with fields: `x`, `y`, `width`, `height`, `speed`, `float[] waypointX`, `float[] waypointY`, `int currentWaypoint`, `boolean moving`
- [x] 1.2 Create `EnemyLoader.java` in `com.rpfacco.oopquest.game.data` with static cache, parsing `assets/data/enemies.json` (same pattern as `NpcLoader`)
- [x] 1.3 Create `assets/data/enemies.json` with one enemy on map03: speed 240, size 32×32, position at first waypoint, 3 waypoints forming a triangle centered at (960, 540) within a ~400×400 area

## 2. System Layer

- [x] 2.1 Create `EnemySystem.java` in `com.rpfacco.oopquest.game` with:
  - `setEnemies(Array<EnemyEntity> enemies)` method
  - `update(float delta)` that moves each enemy toward its current waypoint using the same algorithm as `Player.update()` — advance to next waypoint on arrival, wrap around after last
  - `render(ShapeRenderer)` that draws each enemy as a filled red (1, 0, 0) rectangle

## 3. Integration

- [x] 3.1 In `GameplayScreen.java`:
  - Add `EnemySystem` field
  - In `show()` (initialization block): create `EnemySystem` instance
  - In `loadMap()`: call `enemySystem.setEnemies(EnemyLoader.load().get(mapId))`
  - In `render()`: call `enemySystem.update(delta)` after `player.update(delta)`, call `enemySystem.render(shapeRenderer)` before rendering the player

## 4. Verification

- [x] 4.1 Build and run the game
- [x] 4.2 Navigate to map03 and confirm the enemy is visible as a red rectangle moving in a triangle pattern (manual)
- [x] 4.3 Confirm no crashes or warnings introduced
