## 1. Data Layer Cleanup

- [x] 1.1 Delete `core/src/main/java/com/rpfacco/oopquest/game/HudRenderer.java`
- [x] 1.2 Build and confirm no compilation errors

## 2. Entity Package Consolidation

- [x] 2.1 Move `MovementStrategy` interface to `com.rpfacco.oopquest.game.data.model`, update imports
- [x] 2.2 Move `ShootPattern` interface to `com.rpfacco.oopquest.game.data.model`, update imports
- [x] 2.3 Verify `EnemyEntity.java` no longer imports from `com.rpfacco.oopquest.game`
- [x] 2.4 Move `Player.java` to `com.rpfacco.oopquest.game.data.model`, update all 8 importing files
- [x] 2.5 Build and confirm no compilation errors

## 3. Strategy Factory

- [x] 3.1 Create `StrategyFactory.java` in `com.rpfacco.oopquest.game` with methods `createMovement(String type, JsonValue config)` and `createShootPattern(String type, JsonValue config)`
- [x] 3.2 Refactor `EnemyLoader` to use `StrategyFactory` instead of direct imports of `WaypointMovement`, `BurstPattern`, etc.
- [x] 3.3 Build and confirm no compilation errors

## 4. Enemy Config

- [x] 4.1 Add `quizId` field to `EnemyEntity` (optional `String`, defaults to `null`)
- [x] 4.2 Update `EnemyLoader.fromJson()` to read `"quizId"` if present
- [x] 4.3 Fix `WaypointMovement.fromJson()` to read `"startWaypoint"` from config when present, default to 0
- [x] 4.4 Edit `assets/data/enemies.json` to add `"quizId": "3"` to the map03 enemy
- [x] 4.5 Update `GameplayScreen.onEnemyDeath` to use `e.getQuizId()` instead of hardcoded `"3"`
- [x] 4.6 Build and confirm no compilation errors

## 5. Game Layer Cleanup

- [x] 5.1 Make `QuizScreen` extend `BaseScreen`, remove duplicated camera/viewport/batch/font fields and initialization, call `super.show()` and set font scale
- [x] 5.2 Remove `EnemyLoader.clearCache()` call from `GameState.reset()`
- [x] 5.3 Add `resetGame()` method to `OopQuest` that calls `GameState.reset()` then `EnemyLoader.clearCache()`
- [x] 5.4 Split `ProjectileSystem.update()` — extract `checkEnemyCollision`, `checkPlayerCollision`, `checkBounds` private methods
- [x] 5.5 Build and confirm no compilation errors

## 6. Build & Verify

- [x] 6.1 Run full gradle build: `./gradlew build` or `gradlew.bat build`
- [x] 6.2 Run lint checks: `./gradlew spotlessApply` or similar (no linter configured; build passed)
- [x] 6.3 Run `openspec archive` to save all tasks
