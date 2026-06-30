## Why

Three small issues found during gameplay review: `getAliveEnemies()` allocates a new `Array` every frame causing GC pressure; `QuizScreen` handles input inline instead of using the established `InputHandler` abstraction; and the homing projectile's initial direction is rotated 90° perpendicular to the target (corrected instantly by the homing behavior, but technically wrong).

## What Changes

- **getAliveEnemies**: Replace per-frame allocation with a pre-allocated `Array` that is cleared and repopulated
- **QuizScreen input**: Pass `InputHandler` to `QuizScreen` and use `viewport.unproject()` through it instead of direct `Gdx.input` calls
- **Homing projectile direction**: Fix initial velocity vector from perpendicular `(-dy, dx)` to correct `(dx, dy)` direction toward target

## Capabilities

### New Capabilities

None — these are internal refactors with no new user-facing capabilities.

### Modified Capabilities

None — no spec-level behavior changes.

## Impact

- `EnemySystem.java`: replace `getAliveEnemies()` implementation with pre-allocated array
- `GameplayScreen.java`: pass `InputHandler` to `QuizScreen`
- `QuizScreen.java`: use `InputHandler` instead of inline `Gdx.input` + `viewport.unproject()`
- `InputController.java`: fix `p.setVx/p.setVy` to use `(dx/dist, dy/dist)` instead of `(-dy/dist, dx/dist)`
