## Why

`GameplayScreen` (272 lines) is a god class mixing 6+ responsibilities: rendering, input handling, map management, collision detection, game rules, and lifecycle. This makes it hard to reason about, modify, or test. Extracting focused classes improves cohesion and prepares the codebase for future features (power-ups, animation, particle effects).

## What Changes

- Extract **WorldRenderer** — all ShapeRenderer/Batch rendering into a dedicated class
- Extract **MapManager** — tiled map loading, disposal, and transition logic
- Extract **InputController** — input handling + homing projectile creation
- Make **GameplayScreen extend BaseScreen** — eliminates duplicated camera/viewport/batch/font lifecycle
- Move `clampPlayerToBounds` into Player or a collision utility
- Extract game rule callbacks (onNpcTrigger, onEnemyDeath, onProjectileHit) into GameplayScreen as public API for the extracted classes

No behavioral changes. Visual output and game mechanics remain identical.

## Capabilities

### New Capabilities

- `split-gameplayscreen`: Extract rendering, map management, and input concerns from GameplayScreen into focused classes. Pure refactoring — no new features or behavioral changes.

### Modified Capabilities

None.

## Impact

- `GameplayScreen.java` rewritten from 272 lines to ~100 lines
- `WorldRenderer.java` created (~50 lines)
- `MapManager.java` created (~40 lines)
- `InputController.java` created (~40 lines)
- All existing systems (EnemySystem, NpcSystem, ProjectileSystem) remain unchanged
- No changes to data model or loader classes
- No changes to assets or configuration files
