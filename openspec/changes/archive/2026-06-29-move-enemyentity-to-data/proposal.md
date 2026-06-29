## Why

`EnemyEntity.java` lives in `game/` while `NpcEntity`, `ProjectileEntity`, and `MoveEntity` are in `data/`. This inconsistency makes the package structure confusing — entity classes should be co-located.

## What Changes

- Move `EnemyEntity.java` from `game/` to `data/` package
- Update package declaration and all imports across the project

## Capabilities

### New Capabilities

- `move-enemyentity-to-data`: Move EnemyEntity to the data package for consistency

### Modified Capabilities

None.

## Impact

- 1 file relocated: `EnemyEntity.java`
- 4 files with import changes: `EnemySystem.java`, `EnemyLoader.java`, `GameplayScreen.java`, `MovementStrategy.java`
- No behavioral changes
