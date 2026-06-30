## Context

Three independent code quality issues in the game loop and UI:

1. **GC pressure**: `EnemySystem.getAliveEnemies()` allocates a new `Array<>()` every frame (60 calls/sec), called from `GameplayScreen.render()` → `ProjectileSystem.update()`. libGDX avoids allocation in hot paths; this is an anti-pattern.

2. **Input inconsistency**: `GameplayScreen` uses `InputHandler → InputController → InputResult` abstraction, but `QuizScreen` reads `Gdx.input.justTouched()` directly and calls `viewport.unproject()` inline. Breaks the established pattern.

3. **Homing direction bug**: `InputController` creates homing projectiles with velocity `(-dy/dist, dx/dist)` — a 90° rotation from the correct direction `(dx/dist, dy/dist)`. The `HomingBehavior` corrects it instantly due to a high `turnRate`, making it invisible at runtime, but the code is incorrect.

## Goals / Non-Goals

**Goals:**
- Eliminate per-frame `Array` allocation in `getAliveEnemies()`
- Make `QuizScreen` use `InputHandler` like the rest of the game
- Fix homing projectile initial direction to point at target

**Non-Goals:**
- No behavioral changes visible to the player
- No new abstractions or refactoring beyond what's listed
- No changes to `HomingBehavior` logic or `turnRate` calculation

## Decisions

### 1. Pre-allocated array for alive enemies

Instead of `new Array<>()` each call, add a field `aliveEnemiesBuffer` to `EnemySystem`, cleared and repopulated in `getAliveEnemies()`.

```java
private final Array<EnemyEntity> aliveEnemiesBuffer = new Array<>();

public Array<EnemyEntity> getAliveEnemies() {
    aliveEnemiesBuffer.clear();
    if (enemies == null) return null;
    for (EnemyEntity enemy : enemies) {
        if (enemy.isAlive()) aliveEnemiesBuffer.add(enemy);
    }
    return aliveEnemiesBuffer;
}
```

**Alternative considered**: Pass the array by reference from `ProjectileSystem` or `GameplayScreen`. Rejected because it spreads ownership — keeping it inside `EnemySystem` is cleaner.

**Risk**: The returned array is mutated on the next call. Callers must not hold references across frames. Current callers (`projectileSystem.update()`) consume it immediately and don't store it — safe.

### 2. QuizScreen input via InputHandler

Add `InputHandler` field to `QuizScreen`, passed through constructor from `GameplayScreen`. Replace inline `Gdx.input.justTouched()` + `viewport.unproject()` with `inputHandler.handleTouch()`.

`InputHandler.handleTouch()` already returns `Vector3` and handles unproject — the only difference is that `QuizScreen` needs to call it, which gives the exact same behavior.

### 3. Fix homing velocity

Change `InputController.java:43-44` from:
```java
p.setVx(-dy / dist);
p.setVy(dx / dist);
```
to:
```java
p.setVx(dx / dist);
p.setVy(dy / dist);
```

## Risks / Trade-offs

- **Reused array**: If a future caller stores the returned array reference across frames, it will see mutated data. Mitigation: document that the array is reused and only valid for the current frame.
- **QuizScreen constructor change**: Adding `InputHandler` parameter is a minor API change. Only one caller (`GameplayScreen`) — low risk.
