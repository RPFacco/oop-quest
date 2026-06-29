## Why

The player has no offensive capability — only movement and quiz interactions. Adding a homing projectile fired with E gives the player an interactive tool to engage with enemies, making the game feel more responsive and alive.

## What Changes

- New `ProjectileBehavior` interface for projectile movement logic
- New `HomingBehavior` implementation with configurable turn rate
- `ProjectileSystem` extended to manage behavior per projectile via internal entries
- `InputHandler` gains `isEPressed()` method
- `EnemySystem` gains `findNearest(Player)` for targeting
- `GameplayScreen` wires E key → nearest enemy → homing projectile fire
- Projectile is blue, disappears on enemy contact, deals no damage

## Capabilities

### New Capabilities

- `homing-projectile`: Player fires a blue homing projectile at the nearest enemy with E key
- `projectile-behavior`: Reusable interface for defining projectile movement patterns
- `nearest-enemy-targeting`: System to find the closest enemy to the player

### Modified Capabilities

*(none)*

## Impact

- `game/ProjectileSystem.java`: internal entry management + behavior-aware update
- `game/InputHandler.java`: add isEPressed()
- `game/EnemySystem.java`: add findNearest()
- `game/GameplayScreen.java`: E key handling in handleInput()
- `game/ProjectileBehavior.java` (new): interface
- `game/HomingBehavior.java` (new): implementation
