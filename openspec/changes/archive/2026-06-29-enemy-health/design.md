## Context

Enemies are red rectangles that move and shoot, but player projectiles pass through them harmlessly (the projectile disappears on collision, but the enemy is unaffected). This gives combat no weight. Adding HP, death, and a health bar makes each shot count and gives players clear feedback.

## Goals / Non-Goals

**Goals:**
- Enemies have 8 HP and die when it reaches 0
- Health bar always visible below alive enemies, proportional to remaining HP
- Dead enemies turn gray, stop moving, stop shooting
- Dead enemies remain in the list until map transition (persist as gray corpses)
- Player projectiles (homing) deal 1 damage per hit
- Homing projectiles do not track dead enemies

**Non-Goals:**
- No damage numbers, particles, or death animations
- No changes to enemy projectiles or player damage
- No changes to `EnemyEntity` position in package (stays in `game/`)

## Decisions

### Decision 1: EnemyEntity gains HP fields

```java
public class EnemyEntity {
    private int hp = 8;
    private int maxHp = 8;
    private boolean alive = true;

    public void takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) alive = false;
    }
    // + getters
}
```

`maxHp` is stored separately so the health bar width can be computed as `hp / (float) maxHp`.

### Decision 2: Dead enemies handled in EnemySystem

```
EnemySystem.update():
  for each enemy:
    if !enemy.isAlive(): continue  // skip movement

EnemySystem.updateShooting():
  for each enemy:
    if !enemy.isAlive(): continue  // skip shooting

EnemySystem.findNearest():
  for each enemy:
    if !enemy.isAlive(): continue  // skip corpses

EnemySystem.render():
  for each enemy:
    if enemy.isAlive(): shapeRenderer.setColor(1, 0, 0, 1)      // red
    else:              shapeRenderer.setColor(0.5, 0.5, 0.5, 1) // gray
    shapeRenderer.rect(enemy.getX(), enemy.getY(), ...)

EnemySystem.renderHealthBars():
  for each enemy:
    if !enemy.isAlive(): continue
    float ratio = enemy.getHp() / (float) enemy.getMaxHp()
    barWidth = enemy.getWidth() * ratio
    barX = enemy.getCenterX() - barWidth / 2f
    barY = enemy.getY() - 8  // below enemy
    shapeRenderer.setColor(0, 1, 0, 1)  // green
    shapeRenderer.rect(barX, barY, barWidth, 4)
```

Dead enemies are kept in the main list but excluded from gameplay logic. They persist gray/still until the next `loadMap()` call (map transition), which replaces the entire enemy array.

### Decision 3: Alive-only list for projectile system

`EnemySystem` exposes `getAliveEnemies()` which returns a filtered `Array<EnemyEntity>` containing only `enemy.isAlive()`. This is passed to `projectileSystem.update()` instead of the full list. Benefits:

- `circleEnemyCollision()` never hits corpses
- `HomingBehavior.enemies.contains(target, true)` works unchanged because dead targets aren't in the filtered list
- No changes needed to `ProjectileSystem` or `HomingBehavior`

### Decision 4: Damage applied in ProjectileSystem.update()

Inside the player-projectile branch (`entry.behavior != null`), `circleEnemyCollision()` is changed to both detect the hit AND apply damage. The simplest approach: iterate enemies directly in `update()`:

```
for (int i = enemies.size - 1; i >= 0; i--) {
    EnemyEntity e = enemies.get(i);
    if (circleRectCollision(p, e)) {
        e.takeDamage(1);
        p.setAlive(false);
        break;
    }
}
```

This replaces the current `circleEnemyCollision()` loop that only returns true/false.

## Risks / Trade-offs

- **[Memory]** Dead enemies accumulate in the list until map transition — trivial for current map sizes (< 50 enemies)
- **[findNearest filter]** `findNearest()` skipping dead enemies means the player can't auto-target corpses with E — intended behavior
- **[Health bar Z-order]** Drawn after enemies but before projectiles ensures bars don't overlap comets
