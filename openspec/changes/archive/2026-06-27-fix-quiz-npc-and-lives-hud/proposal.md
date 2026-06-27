## Why

The quiz and NPC system implemented in the previous change has several bugs (resource leak on game over, text rendered twice) and quality gaps (inconsistent loading, silent errors, tight trigger range). The player also has no way to see their remaining lives during gameplay, and dying doesn't reset game state — making replay confusing.

## What Changes

- Fix resource leak: `QuizScreen` now disposes its resources before transitioning on game over
- Fix text: question text measured with `GlyphLayout` instead of drawing twice
- Cache NPC data: `NpcLoader` loaded once, not re-parsed on every map transition
- Add logging: missing quiz ID now logs an error via `Gdx.app.log()`
- Increase NPC trigger zone from 1.5x to 2.0x body size for more forgiving detection
- Add lives HUD to `GameplayScreen` — always visible during gameplay
- Reset `GameState` (lives to 5, clear completed quizzes) when player dies

## Capabilities

### New Capabilities
- _(none — this change modifies existing capabilities only)_

### Modified Capabilities
- `quiz-system`: Add resource disposal on game over; fix double-render of question text
- `peaceful-npcs`: Widen trigger zone to 2x; cache NPC data; log errors for missing quiz IDs
- `game-state`: Show lives HUD during gameplay; reset state on death

## Impact

- **Modified files:** `QuizScreen.java`, `GameplayScreen.java`, `GameState.java`, `NpcLoader.java`
- **No new files**
- **No data format changes**
