## Context

The project is a fresh libGDX 1.14.2 skeleton with only `JogoOpenSpec` (extends `Game`) and `FirstScreen`. No game logic exists yet. The `docs/game-design.md` defines click-to-move, screen-sized maps, and Tiled-based data-driven maps.

This change builds the scaffolding: screen management, map loading, player movement, and data infrastructure.

## Goals / Non-Goals

**Goals:**
- Player sees a map and can click to walk around
- Map renders from a `.tmx` file via libGDX's `TiledMapRenderer`
- Player stays within map bounds (clamped to edges)
- Screen flow: `MainMenuScreen` → `GameplayScreen` → back to menu on Escape
- `maps.json` is loaded and parsed to determine the starting map
- Remove the template-generated `FirstScreen`

**Non-Goals:**
- Map transitions (directional borders) — deferred to a later change
- Combat, NPCs, quizzes
- Camera movement (map is fixed to screen size)
- Internal obstacle collision (trees/walls)
- Art assets beyond colored rectangles
- Tiled editor workflow — the `.tmx` and tileset can be hand-written for now

## Decisions

### Decision 1: Map creation approach
- **Chosen**: Hand-write a minimal `.tmx` and generate a simple tileset PNG via a small Java utility or manual creation
- **Rationale**: Proves the data-driven pipeline (`.tmx` → `TiledMap` → renderer) end-to-end. A `.tmx` is just XML + base64-encoded tile data plus a reference to a tileset image.
- **Alternative considered**: Render everything with `ShapeRenderer` and skip `.tmx` entirely. Rejected because it doesn't validate the Tiled pipeline, which is the core architectural decision in `game-design.md`.

### Decision 2: JSON parsing
- **Chosen**: libGDX's `com.badlogic.gdx.utils.Json` for POJO deserialization
- **Rationale**: Zero external dependencies, built into libGDX, maps cleanly to Java classes
- **Alternative considered**: Manual `JsonReader` + `JsonValue` traversal. More boilerplate and error-prone.

### Decision 3: Player movement
- **Chosen**: Target-based movement with linear interpolation. On mouse click, set a target world position. Each frame, move player toward target at constant speed. Stop when within a threshold distance.
- **Rationale**: Simple, deterministic, easy to debug. Matches "click-to-move" from game-design.md.
- **Alternative considered**: Pathfinding (A*) — overkill until obstacle collision exists.

### Decision 4: Screen management
- **Chosen**: `Game.setScreen()` with `MainMenuScreen` (text-only, click to start) → `GameplayScreen` (map + player)
- **Rationale**: Standard libGDX pattern. `FirstScreen` is replaced by `MainMenuScreen`.

## Risks / Trade-offs

| Risk | Mitigation |
|---|---|
| `.tmx` format issues (tile layers, coordinate system) | Use libGDX's built-in `TiledMapLoader` which handles format quirks. Stick to orthogonal, 32x32 tiles. |
| JSON parsing with libGDX's `Json` class has limitations | Keep data structures simple (plain POJOs with public fields). No nested generics. |
| Click-to-move feels unresponsive without pathfinding or animation | Acceptable for v1. Movement is immediate and direct. Animation and polish deferred. |
