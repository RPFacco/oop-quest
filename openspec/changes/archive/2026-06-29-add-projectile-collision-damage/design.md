## Context

Projectiles fly through the player with no interaction. `ProjectileSystem` currently moves projectiles and removes off-screen ones. `Player` has no invincibility or damage state. `GameplayScreen` displays lives but never decrements them during gameplay (only QuizScreen does).

## Goals / Non-Goals

**Goals:**
- Circle-vs-rectangle collision between projectiles and player
- On collision: projectile removed, player loses 1 life
- 1 second invincibility after hit (player blinks visually)
- When lives ≤ 0: game resets and returns to MainMenuScreen

**Non-Goals:**
- No Game Over screen (same reset-to-menu behavior as QuizScreen)
- No sound effects or particles on hit
- No changes to enemy data or JSON format

## Decisions

| Decision | Choice | Rationale |
|----------|--------|-----------|
| Collision detection | Inside `ProjectileSystem.update()` with callback | PS handles geometry, caller decides consequence |
| Callback type | `Consumer<ProjectileEntity>` | Simple, standard Java, no new interface needed |
| Invincibility storage | `Player.invincibleTimer` (float) | Player owns its state, timer counts down in Player.update() |
| Blink visual | Skip render when `(int)(timer * 10) % 2 == 0` | Simple, 5 blinks/sec, no extra state |
| Game over on 0 lives | `GameState.reset()` + `setScreen(MainMenuScreen)` | Same pattern as QuizScreen, no new screen needed |

**Alternatives considered:**
- **PS having GameState reference**: Couples PS to game logic. Callback is cleaner separation.
- **Separate collision system**: Overkill for one collision type.
- **Player-internal lives**: Lives are already in GameState, consistent to keep there.

## Risks / Trade-offs

| Risk | Mitigation |
|------|------------|
| [Multiple hits same frame] 2+ projectiles hit in one frame | Invincibility timer prevents multi-hit; only first registers |
| [Callback invoked mid-iteration] Hit removes projectile while iterating | PS removes projectile immediately after callback, uses reverse iteration (already pattern) |
