## Context

The previous change added peaceful NPCs, quizzes, and game state. Code review revealed a resource leak (`QuizScreen` not disposed on game over), text rendered twice to measure width, `NpcLoader` re-parsing JSON on every map transition, silent failure on missing quiz IDs, a tight trigger zone (1.5x), no lives HUD during gameplay, and no game state reset on death.

## Goals / Non-Goals

**Goals:**
- Fix all bugs and quality issues identified in the code review
- Add lives HUD to `GameplayScreen` (top-left corner)
- Reset lives and completed quizzes when player dies
- Keep all changes minimal and focused

**Non-Goals:**
- UI styling beyond basic text (fonts, colors, positioning remain simple)
- Changes to quiz data format or NPC data format
- New gameplay features beyond what's listed

## Decisions

### Decision 1: QuizScreen disposes before game over transition
**Chosen:** Call `dispose()` explicitly before `game.setScreen()` when lives reach 0, matching the pattern already used by `GameplayScreen.handleInput()` and `MainMenuScreen.render()`.

**Rationale:** libGDX's `Game.setScreen()` does not auto-dispose the previous screen. The existing codebase already handles this correctly in other screens — QuizScreen was the exception.

### Decision 2: Use GlyphLayout to measure text without drawing
**Chosen:** Create a `GlyphLayout` instance, call `layout.setText(font, text)` to measure width, then draw with the computed x position.

**Rationale:** `BitmapFont.draw()` both draws and returns width. Drawing at x=0 to measure causes a one-frame visual glitch. `GlyphLayout` is the standard libGDX way to measure text without rendering.

### Decision 3: Cache NpcLoader data in a static field
**Chosen:** `NpcLoader` keeps a static `Map<String, Array<NpcEntity>>` cache, loaded once on first call and returned on subsequent calls. An explicit `reload()` method forces re-read.

**Rationale:** NPC data doesn't change during gameplay. Re-parsing JSON on every map transition is wasteful. Static cache is simple and matches the stateless loader pattern (`MapLoader` doesn't cache because map data is also static but small — NPC data is also small, but the principle of not re-reading files is still valid).

### Decision 4: Increase trigger zone to 2.0x
**Chosen:** Change the trigger multiplier from 1.5x to 2.0x. Body is 32x32, trigger becomes 64x64.

**Rationale:** 1.5x required the player to be within ~36px of the NPC center — about 0.22s at walking speed. 2.0x gives ~56px, about 0.35s. More forgiving without being so large that NPCs trigger from across the map.

### Decision 5: GameState.reset() method
**Chosen:** Add a `reset()` method to `GameState` that sets `lives = 5` and clears `completedQuizzes`. Called from `MainMenuScreen` when starting a new game.

**Rationale:** Cleaner than re-creating the `GameState` object. Ensures all state is properly reset.

### Decision 6: Lives HUD in GameplayScreen render
**Chosen:** Draw "Lives: N" text in the top-left corner of `GameplayScreen` using `BitmapFont` and `SpriteBatch`, after the tile map render but before the shape renderer.

**Rationale:** Text rendering requires `SpriteBatch`, which isn't used in `GameplayScreen` currently. Adding a `SpriteBatch` alongside the existing `ShapeRenderer` is the cleanest approach — render text with batch, shapes with shape renderer.

## Risks / Trade-offs

- **Risk:** `NpcLoader` static cache could become stale if NPC data files change mid-session. → **Mitigation:** Not a realistic scenario for a single-player game where data is loaded from packaged assets. The cache is a pure win.
