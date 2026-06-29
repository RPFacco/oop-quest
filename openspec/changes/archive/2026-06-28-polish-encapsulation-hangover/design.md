## Context

After the `refactor-code-quality` change, four minor issues remain:

1. **MapData.getMaps()** still exposes its internal `ObjectMap<String, MapEntry>` — any caller can `put()` or `clear()` it
2. **InputHandler.handleTouch()** returns a reference to the internal `Vector3 touchPos` — a caller that stores it sees aliased mutations
3. **QuizScreen.gameOver** is checked after rendering, unlike GameplayScreen which checks before — inconsistent and fragile
4. **EnemyEntity.moving** is always set to `true` in `EnemyLoader` and never set to `false` — the field is dead code, the real guard is `getStrategy() == null`

## Goals / Non-Goals

**Goals:**
- Replace `MapData.getMaps()` with `getMap(String id)`, keep raw accessor package-private for MapLoader
- Return `touchPos.cpy()` from `InputHandler.handleTouch()` (defensive copy)
- Move `gameOver` check in `QuizScreen.render()` before rendering, matching GameplayScreen pattern
- Remove `EnemyEntity.moving` field and all related accessors/calls
- Zero behavior change — all refactoring is mechanically equivalent

**Non-Goals:**
- Changing the shape of `handleTouch()` (returns `Vector3 | null`, callers need no changes)
- Any API changes beyond the 4 listed
- Performance optimization — the `.cpy()` allocation is per-click, negligible

## Decisions

### 1. MapData API hardening

```java
// MapData.java
private ObjectMap<String, MapEntry> maps = new ObjectMap<>();

public MapEntry getMap(String id) { return maps.get(id); }  // novo método público

// getMaps() vira package-private para MapLoader continuar populando
ObjectMap<String, MapEntry> getMaps() { return maps; }
```

- `MapLoader` (mesmo package `data/`) usa `getMaps().put()` sem mudança
- `GameplayScreen` troca `mapData.getMaps().get(id)` por `mapData.getMap(id)`
- Alternatives considered: full encapsulation with `putMap()` method — unnecessary, only one loader populates

### 2. InputHandler defensive copy

```java
public Vector3 handleTouch() {
    if (!Gdx.input.justTouched()) return null;
    touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
    viewport.unproject(touchPos);
    return touchPos.cpy();  // defensive copy
}
```

One `Vector3` allocation (~24 bytes) per click — negligible for a game with per-frame allocations in the KB range.

Alternatives considered:
- **Out parameter**: `void handleTouch(Vector3 out)` — zero allocation but changes the API surface, callers must manage temp Vector3
- **Documented aliasing**: "only valid until next handleTouch() call" — fragile, defeats the purpose

### 3. QuizScreen gameOver position

Move check from after rendering to before rendering:

```java
public void render(float delta) {
    handleInput(delta);       // pode setar gameOver = true
    if (gameOver) {           // ← movido para antes do rendering
        gs.reset();
        dispose();
        jogoGame.setScreen(new MainMenuScreen(jogoGame));
        return;
    }
    // ... rendering continua ...
}
```

Mantém `GameplayScreen` como referência (padrão consistente). O `return` evita executar rendering após o `dispose()`.

### 4. EnemyEntity.moving removal

- Remove `private boolean moving` de `EnemyEntity`
- Remove `isMoving()` e `setMoving()` 
- Remove `enemy.setMoving(true)` de `EnemyLoader`
- Simplifica `EnemySystem.update()`: `if (enemy.getStrategy() == null) continue;`

O campo era sempre `true` — nunca há um caminho onde um inimigo tenha strategy mas não esteja "moving". A condição `getStrategy() == null` já cobre o caso de pular.

## Risks / Trade-offs

- **[Low] MapData package-private accessor** — any future class outside `data/` that needs to enumerate maps (e.g., a map editor) won't have access. Mitigation: add a `getMapIds()` or `getMapCount()` method if needed later.
- **[Low] .cpy() allocation** — one extra Vector3 per click. If a rapid-clicking loop ever emerges, create a single temp Vector3 in the caller. Not needed currently.
- **[Very Low] EnemyEntity moving removal** — if a future mechanic needs to stop an enemy mid-movement, the flag would need to be reintroduced. Mitigation: by that point the mechanic will inform the correct design (e.g., a `stunned` or `paralyzed` field).
