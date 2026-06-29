## 1. Player Invincibility

- [x] 1.1 Add `public float invincibleTimer` field to `Player`, initialize to 0
- [x] 1.2 In `Player.update()`, decrement `invincibleTimer` by delta (clamp to 0)

## 2. ProjectileSystem Collision

- [x] 2.1 Change `ProjectileSystem.update()` signature to `void update(Player player, float delta, Consumer<ProjectileEntity> onHit)`
- [x] 2.2 After moving each projectile, implement circle-vs-AABB collision check against player
- [x] 2.3 On collision: invoke `onHit.accept(p)`, set `p.alive = false`, remove projectile

## 3. GameplayScreen Wiring

- [x] 3.1 Update `GameplayScreen.render()` to pass player + hit callback to `projectileSystem.update()`
- [x] 3.2 In hit callback: if `player.invincibleTimer > 0` return (ignore); otherwise decrement `gameState.lives`, set `player.invincibleTimer = 1f`
- [x] 3.3 If `lives ≤ 0` after decrement: call `gameState.reset()`, `dispose()`, `setScreen(new MainMenuScreen(jogoGame))`
- [x] 3.4 In player render block, skip drawing if `player.invincibleTimer > 0 && (int)(player.invincibleTimer * 10) % 2 == 0`

## 4. Verification

- [x] 4.1 Build — zero compilation errors
- [x] 4.2 Run — game starts without crashes
- [x] 4.3 Navigate to map03, let projectile hit player — confirm life decrements, player blinks, projectile disappears
- [x] 4.4 Confirm lives reach 0 → returns to MainMenuScreen
