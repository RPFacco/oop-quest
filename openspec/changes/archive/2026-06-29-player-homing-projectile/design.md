## Context

The player currently has no offensive interactions — only movement (click-to-move) and quiz triggers (NPC proximity). Enemies shoot projectiles at the player via `ShootPattern`/`BurstPattern`, but the player cannot respond. This feature adds a homing attack with E key.

## Goals / Non-Goals

**Goals:**
- Player fires a blue homing projectile at the nearest enemy with E
- Curved trajectory via constant turn rate
- 0.5s cooldown between shots
- No damage — projectile disappears on contact
- Keep `data/ProjectileEntity` free of `game/` package references

**Non-Goals:**
- No changes to `ShootPattern` or `BurstPattern`
- No melee attack system
- No enemy damage or health system
- No changes to enemy projectiles or existing `ProjectileEntity`

## Decisions

### Decision 1: Behavior managed by ProjectileSystem, not ProjectileEntity

`ProjectileSystem` uses an internal `ProjectileEntry` class that pairs a `ProjectileEntity` with a `ProjectileBehavior`. This keeps the `data` package clean — `ProjectileEntity` has zero knowledge of homing logic.

```
ProjectileSystem (game/)
  entries: Array<ProjectileEntry>
    ├── ProjectileEntity (data/)  ← unchanged
    └── ProjectileBehavior (game/) ← new interface

  update():
    for each entry:
      if behavior != null: behavior.update(entity, delta, enemies)
      entity.x += entity.vx * entity.speed * delta  (existing)
      check bounds + collision (existing)
```

Alternatives considered:
- **Field on ProjectileEntity**: rejected because it creates `data/` → `game/` dependency
- **Subclass of ProjectileEntity**: rejected because it requires `instanceof` checks in update loop
- **Separate system (PlayerProjectileSystem)**: rejected because it duplicates bounds/collision logic

### Decision 2: Constant turn rate for curved trajectory

Each frame, `HomingBehavior` computes the desired direction toward the enemy's center and rotates the current velocity vector toward it, clamped by `turnRate * delta`. This produces a natural smooth curve even when the enemy moves.

```
desiredDir = normalize(enemy.center - projectile.pos)
angleDiff = angleBetween(currentDir, desiredDir)
clampedAngleDiff = clamp(angleDiff, -turnRate * delta, turnRate * delta)
rotate currentDir by clampedAngleDiff
```

### Decision 3: Cooldown tracked in GameplayScreen

The cooldown timer lives as a `float homingCooldown` field in `GameplayScreen`, decremented by `delta` each frame. When E is pressed and the timer is ≤ 0, the projectile fires and the timer resets to 0.5s. No new class needed.

### Decision 4: findNearest in EnemySystem

`EnemySystem` gains a method that iterates its `enemies` array, computing squared Euclidean distance (avoids `sqrt` for perf), and returns the closest. Returns null if no enemies.

## Risks / Trade-offs

- **[Perf] findNearest runs O(n) each time E is pressed** — acceptable since enemy count is small (< 50). If scaling up, a spatial index could be added later.
- **[Perf] HomingBehavior runs per-frame for each homing projectile** — acceptable since at most 1 projectile per 0.5s window. Same bounds as existing projectile count.
- **[Bloat] ProjectileEntry adds minor memory overhead** — ~one object per active projectile. Negligible.
