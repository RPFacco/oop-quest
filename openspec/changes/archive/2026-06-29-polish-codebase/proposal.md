## Why

Six small issues remain after previous refactoring rounds: dead code, duplicated Screen lifecycle, cross-package dependencies, inconsistent entity placement, hardcoded values, and a monolithic update method. Each is minor, but together they hurt maintainability and make future changes harder.

## What Changes

- **Remove HudRenderer** — dead code since WorldRenderer handles HUD
- **Fix WaypointMovement.fromJson** — read `startWaypoint` from JSON instead of hardcoding 0
- **Desacoplar GameState de EnemyLoader** — `GameState.reset()` no longer calls `EnemyLoader.clearCache()`. `OopQuest.resetGame()` coordinates state reset and cache clearing
- **QuizScreen extends BaseScreen** — eliminate duplicated camera/viewport/batch/font lifecycle
- **Move MovementStrategy + ShootPattern → data/model/** — resolve data layer importing from game layer
- **Create StrategyFactory** — EnemyLoader uses factory instead of importing concrete strategy classes
- **Move Player → data/model/** — consistency: all entity classes in the same package
- **Split ProjectileSystem.update()** — extract collision methods for readability
- **Add quizId to EnemyEntity** — replace hardcoded `"3"` with per-enemy quiz ID from JSON config

All changes are pure refactoring — no behavioral changes.

## Capabilities

### New Capabilities

- `polish-codebase`: Consolidate all remaining code quality issues in one pass — dead code removal, duplicated lifecycle elimination, cross-package dependency resolution, entity package consistency, and targeted readability improvements.

### Modified Capabilities

None.

## Impact

- **Removed**: `HudRenderer.java`
- **New**: `data/model/Player.java`, `StrategyFactory.java` (in game or data package)
- **Modified**: `QuizScreen.java`, `GameState.java`, `OopQuest.java`, `EnemyEntity.java`, `EnemyLoader.java`, `WaypointMovement.java`, `ProjectileSystem.java`, `GameplayScreen.java`
- **Imports updated**: ~8 files referencing Player after move
- **Assets**: `enemies.json` — add `"quizId"` to existing enemy entry
