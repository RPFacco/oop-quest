## Context

`GameplayScreen` (272 lines) implements `Screen` directly, duplicating camera/viewport/batch/font lifecycle (already abstracted by `BaseScreen`). Its `render()` method interleaves update logic, input handling, collision detection, and rendering across three different APIs (TiledMap, SpriteBatch, ShapeRenderer). Three callback methods (onNpcTrigger, onEnemyDeath, onProjectileHit) handle game rules inline.

```
Current GameplayScreen responsibilities:
┌─────────────────────────────────────────────┐
│ render() → input → update → collision → draw│
│   ├── handleInput()                         │
│   ├── player.update / enemySystem / projSys │
│   ├── checkMoveEntityOverlap()              │
│   ├── npcSystem.checkProximity()            │
│   ├── mapRenderer.render()  (Tiled)         │
│   ├── batch HUD rendering                   │
│   └── shapeRenderer (entities, NPCs, etc.)  │
├─────────────────────────────────────────────┤
│ show() / dispose() / resize()               │
│   └── camera, viewport, batch, font         │
└─────────────────────────────────────────────┘
```

## Goals / Non-Goals

**Goals:**
- Extract rendering into a standalone `WorldRenderer` class
- Extract map lifecycle into a `MapManager` class
- Extract input handling into an `InputController` class
- Make `GameplayScreen` extend `BaseScreen` to eliminate duplicated lifecycle
- Keep GameplayScreen as a thin orchestrator (< 120 lines)
- Zero behavioral changes — visual output and game mechanics must be identical

**Non-Goals:**
- No new features (power-ups, effects, animations)
- No changes to EnemySystem, NpcSystem, ProjectileSystem
- No changes to data model or loader classes
- No changes to assets (maps, configs)
- No test infrastructure — libGDX game logic is tightly coupled to GL context

## Decisions

### Decision: WorldRenderer owns ShapeRenderer lifecycle
`WorldRenderer` will create, own, and dispose the `ShapeRenderer`. GameplayScreen currently creates `ShapeRenderer` in `show()` and disposes in `dispose()`. Moving it to `WorldRenderer` keeps rendering concerns in one place.

Alternatives considered:
- Inject ShapeRenderer from GameplayScreen → less encapsulation, more params
- Make WorldRenderer stateless with static methods → harder to evolve (particles, effects later)

### Decision: MapManager owns TiledMap + OrthogonalTiledMapRenderer
`MapManager` will:
- Hold `TiledMap`, `OrthogonalTiledMapRenderer`, `MapData`, `currentMapId`
- Expose `loadMap(id)`, `transitionTo(MoveEntity, Player)`, `render(OrthographicCamera)`, `dispose()`
- Handle null-safety and disposal internally

Alternatives considered:
- Keep TiledMap in GameplayScreen and pass to renderer → splits responsibility poorly
- Merge MapManager into WorldRenderer → map loading is not rendering

### Decision: InputController creates ProjectileEntity inline
The homing projectile creation (6 lines in current `handleInput()`) stays in `InputController`. It needs `ProjectileSystem`, `Player`, and `EnemySystem` references.

`InputController` returns a result object:
```java
class InputResult {
    boolean escape;
    boolean homingFired;
    Vector3 touchTarget;
}
```
GameplayScreen reads the result and applies actions. This keeps "what happens" in GameplayScreen and "what input was received" in InputController.

Alternatives considered:
- Pass callbacks to InputController → harder to reason about
- InputController calls GameplayScreen methods → circular dependency

### Decision: GameplayScreen exposes callbacks as package-private
`onNpcTrigger`, `onEnemyDeath`, `onProjectileHit` will be package-private methods on GameplayScreen, passed as method references to systems and WorldRenderer where needed.

### Decision: GameplayScreen extends BaseScreen
This removes 12 lines of duplicated setup (camera, viewport, batch, font) and 6 lines of disposal — the exact duplication BaseScreen was created to eliminate. `font.getData().setScale(2)` (unique to GameplayScreen) stays in `show()` after `super.show()`.

### Decision: clampPlayerToBounds moves into Player
This 6-line bounds check is intrinsic to Player behavior — it should live on Player as a `clampToBounds()` method. No need for a separate utility class.

## Risks / Trade-offs

- **[No behavioral change guarantee]** Manual visual verification needed — no automated tests. Mitigation: extract one concern at a time, compile and run after each step.
- **[Constructor proliferation]** New classes need references to existing systems. Mitigation: pass only what each class needs (no God object injection).
- **[BaseScreen font scaling]** BaseScreen creates a default BitmapFont. GameplayScreen sets scale to 2. This must happen after `super.show()`. Low risk.
- **[InputController timing]** InputController receives `homingCooldown` state. Must ensure cooldown decrement happens before input check. Mitigation: cooldown update stays in GameplayScreen's render loop before handleInput.

## Migration Plan

1. Create `WorldRenderer` — extract all ShapeRenderer + TiledMap rendering
2. Create `MapManager` — extract map load/transition/dispose
3. Create `InputController` — extract input handling and homing creation
4. Move `clampPlayerToBounds` into `Player`
5. Convert `GameplayScreen` to extend `BaseScreen` — remove duplicated fields
6. Rewire GameplayScreen to use extracted classes
7. Build and run to verify no behavioral change

## Open Questions

None — design is fully determined by existing code structure.
