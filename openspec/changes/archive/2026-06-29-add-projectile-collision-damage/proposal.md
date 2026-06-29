## Why

Projectiles currently fly through the player with no effect. The enemy shooting mechanic is purely visual — there's no consequence for being hit. This change adds collision detection between enemy projectiles and the player, reducing lives on hit with invincibility frames and visual feedback.

## What Changes

- `ProjectileSystem.update()` gains `Player` parameter and a hit callback (`Consumer<ProjectileEntity>`)
- Circle-vs-rectangle collision detection between each projectile and the player
- On hit: projectile removed, callback invoked
- `GameplayScreen` passes a callback that decrements `GameState.lives` (respecting invincibility)
- `Player` gains `invincibleTimer` field (1 second of invulnerability after hit)
- Player visually blinks (alternates visibility) while invincible
- When `lives ≤ 0`: game state resets and returns to `MainMenuScreen`

## Capabilities

### New Capabilities
- `projectile-player-damage`: Enemy projectiles collide with the player, dealing damage with invincibility frames and visual blink feedback.

### Modified Capabilities
*(none)*

## Impact

- **Files to modify:** `ProjectileSystem.java`, `Player.java`, `GameplayScreen.java`
- **No new files needed**
- **No impact on:** EnemySystem, EnemyEntity, BurstPattern, ShootPattern, maps, JSON data
