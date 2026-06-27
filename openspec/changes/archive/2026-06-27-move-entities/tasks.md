## 1. Data Model

- [x] 1.1 Create `MoveEntity.java` — data class with fields: `x`, `y`, `width`, `height`, `targetMap`, `spawnX`, `spawnY`
- [x] 1.2 Modify `MapEntry.java` — replace `connections` field with `moveEntities` (array of `MoveEntity`)
- [x] 1.3 Delete `MapConnections.java` — no longer used

## 2. Map Data

- [x] 2.1 Update `MapLoader.java` — parse `moveEntities` array instead of `connections` object
- [x] 2.2 Update `assets/data/maps.json` — replace `connections` with `moveEntities` for all 3 maps
- [x] 2.3 Remove unused `MapConnections` import from `MapEntry.java` and `MapLoader.java`

## 3. GameplayScreen

- [x] 3.1 Remove border transition code — delete `checkMapTransitions()` method and its call from `render()`
- [x] 3.2 Restore simple clamping — replace `checkMapTransitions()` with `clampPlayerToBounds()` (unconditional, no transition logic)
- [x] 3.3 Add move entity storage — store the current map's move entity list as a field
- [x] 3.4 Render move entities — draw each as a golden `(255, 215, 0)` filled rectangle via `ShapeRenderer`
- [x] 3.5 Implement overlap detection — on each frame after player update, check AABB overlap against all move entities; if overlap, trigger transition
- [x] 3.6 Implement move entity transition — load target map, place player at `(spawnX, spawnY)`, clear target

## 4. Verification

- [x] 4.1 Build and confirm compilation succeeds
- [x] 4.2 Run game — verify player starts on map01 with golden move entity visible
- [x] 4.3 Walk into move entity on map01 → transition to map02 at (40, 40)
- [x] 4.4 Walk into map02's upper move entity → transition to map03 at (40, 40)
- [x] 4.5 Walk into map03's move entity → transition back to map02 at (40, 40)
- [x] 4.6 Walk into map02's lower move entity → transition back to map01 at (40, 40)
- [x] 4.7 Verify no ping-pong — player spawns away from all return move entities
- [x] 4.8 Verify borders clamp — player cannot walk off any edge
