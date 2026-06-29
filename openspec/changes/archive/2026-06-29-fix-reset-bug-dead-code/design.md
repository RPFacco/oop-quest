## Context

The `polish-codebase` change created `OopQuest.resetGame()` to coordinate `GameState.reset()` + `EnemyLoader.clearCache()`, but never updated the callers. All 4 restart paths still call `gameState.reset()` directly, skipping cache clearing. This causes enemies to not respawn after death. Separately, 4 methods are dead code from earlier iterations.

## Goals / Non-Goals

**Goals:**
- Wire `app.resetGame()` into all restart paths
- Remove 4 unused methods
- No behavioral changes aside from cache now being cleared

**Non-Goals:**
- No new features
- No refactoring beyond the stated scope
- No UI changes

## Decisions

### 1. Escape path drops redundant reset
**Decision**: Remove `gameState.reset()` from `GameplayScreen` escape handler. When the user presses escape, the screen transitions to `MainMenuScreen`. The reset happens when they click to start a new game. Double-reset was redundant even in the old code.
**Risk**: If the player escapes mid-game and returns to menu, the game state is NOT reset until they click. This is actually correct behavior — they could theoretically resume (not implemented yet, but the path is open).

### 2. QuizScreen game-over uses `app.resetGame()`
**Decision**: Replace `app.getGameState().reset()` with `app.resetGame()` in QuizScreen game-over path (line 64).
**Rationale**: Player died in a quiz, needs full reset including enemy cache for next attempt.

### 3. MainMenuScreen uses `app.resetGame()`
**Decision**: Replace `gs.reset()` with `app.resetGame()` on new game click.
**Rationale**: Starting fresh means fresh enemies.

## Risks / Trade-offs

- **Risk**: `resetGame()` now clears enemy cache in all restart paths → Mitigation: This was the original behavior before `polish-codebase` split it. No regression.
