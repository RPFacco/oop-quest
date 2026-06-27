## Why

Establish the game's foundation. Without a map to stand on and a player that can move, nothing else is testable. This is the minimal vertical slice that proves the rendering, input, and data-loading pipelines work end-to-end.

## What Changes

- Add a `GameplayScreen` that renders a Tiled map and handles player input
- Load `maps.json` and parse map connections (only single-map for now)
- Load a `.tmx` map file and render it using libGDX's `TiledMapRenderer`
- Implement click-to-move player movement on the map
- Keep player within map bounds (collision with edges)
- Create a placeholder player entity (colored rectangle)
- Add a minimal `MainMenuScreen` as the entry point
- Wire screen transitions in `JogoOpenSpec`

## Capabilities

### New Capabilities
- `map-rendering-and-movement`: Map loading from `.tmx`, player movement via mouse click, screen management, and map transition infrastructure

### Modified Capabilities
None — first capability, no existing specs to change.

## Impact

- `core/src/main/java/com/jogoopenspec/game/` — new screens (`GameplayScreen`, `MainMenuScreen`), player entity class, data loader for `maps.json`
- `assets/maps/` — first `.tmx` file
- `assets/data/` — `maps.json` config file
- `docs/game-design.md` — no changes needed (already covers movement and maps)
