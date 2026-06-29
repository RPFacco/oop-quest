## Why

Two issues from the `polish-codebase` refactoring: `OopQuest.resetGame()` was created but never wired to callers, so enemy cache is never cleared on restart (enemies don't respawn). Separately, 4 methods in entities and systems are dead code.

## What Changes

**Group A — resetGame() wiring (bug fix)**
- `MainMenuScreen`: use `app.resetGame()` instead of `gameState.reset()` on new game click
- `GameplayScreen` escape path: remove redundant `gameState.reset()` — MainMenuScreen will reset when user clicks
- `GameplayScreen` game-over path: use `app.resetGame()` instead of `gameState.reset()`
- `QuizScreen` game-over path: use `app.resetGame()` instead of `gameState.reset()`
- All 4 callers now properly clear state + enemy cache

**Group B — Dead code removal**
- Remove `Player.getSpeed()` — never called externally
- Remove `Player.isMoving()` — never called
- Remove `MapManager.getCurrentMapId()` — never called
- Remove `EnemySystem.getEnemies()` — never called

## Capabilities

### New Capabilities

- `fix-reset-bug-dead-code`: Wire `OopQuest.resetGame()` to all restart paths so enemy cache is reliably cleared; remove unused methods.

### Modified Capabilities

None.

## Impact

- **Modified**: `MainMenuScreen.java`, `GameplayScreen.java`, `QuizScreen.java`
- **Dead code removed**: `Player.getSpeed()`, `Player.isMoving()`, `MapManager.getCurrentMapId()`, `EnemySystem.getEnemies()`
