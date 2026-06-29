## Why

Four small encapsulation and code quality issues remain after the `refactor-code-quality` change: two API surfaces leak mutable internals (MapData, InputHandler), QuizScreen follows an inconsistent game-over pattern, and EnemyEntity carries dead code.

## What Changes

- **MapData API hardening**: Replace `getMaps()` with `getMap(String id)`, make the raw collection access package-private
- **InputHandler defensive copy**: Return `touchPos.cpy()` from `handleTouch()` instead of the internal reference
- **QuizScreen gameOver position**: Move game-over check before rendering (matching GameplayScreen pattern)
- **Dead code removal**: Remove `EnemyEntity.moving` field and `isMoving()`/`setMoving()`, simplify EnemySystem guard to `getStrategy() == null`

## Capabilities

### New Capabilities
None — pure refactoring, no new requirements.

### Modified Capabilities
None — no behavior changes.

## Impact

- 4 files touched across the codebase (MapData, InputHandler, QuizScreen, EnemyEntity + EnemySystem)
- No behavior changes — all refactoring is mechanically equivalent
- MapData API changes will require updating 3 call sites in GameplayScreen and 1 in MapLoader
