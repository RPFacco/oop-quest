## Why

Enemies currently have no health — player projectiles collide but nothing happens. Adding 8 HP, a visual health bar, and a death state (gray, stopped) gives enemies substance and makes combat feel meaningful.

## What Changes

- `EnemyEntity` gains `hp`, `maxHp`, `alive` fields and `takeDamage()` method
- `EnemySystem` skips dead enemies in update/shooting, renders them gray, renders health bars below alive ones
- `ProjectileSystem` applies 1 damage per hit instead of just removing the projectile
- `EnemySystem.getAliveEnemies()` filters dead enemies for projectile collision/homing
- `GameplayScreen` passes only alive enemies to projectile system; renders health bars

## Capabilities

### New Capabilities

- `enemy-health`: Enemies have 8 HP, display a health bar, die and turn gray on reaching 0

## Impact

- `game/EnemyEntity.java`: +hp, maxHp, alive, takeDamage()
- `game/EnemySystem.java`: skip dead in update/shooting/findNearest, render dead gray, renderHealthBars()
- `game/ProjectileSystem.java`: apply damage on player projectile hit
- `game/GameplayScreen.java`: pass alive-only enemies, render health bars
