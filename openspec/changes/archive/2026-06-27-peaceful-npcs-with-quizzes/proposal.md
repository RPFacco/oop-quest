## Why

The game design specifies peaceful NPCs that trigger OOP quizzes on maps 1 and 2, but no NPC or quiz system exists yet. This change delivers the first interactive content beyond movement — turning the prototype into a real game with learning goals.

## What Changes

- Add peaceful NPC entities to map01 and map02 that trigger quizzes on proximity
- Create a quiz system: data format (`quizzes.json`), loader, and `QuizScreen`
- Add global game state (lives, completed quizzes) that persists across maps
- Add lives system: start with 5, lose 1 on wrong answer, Game Over at 0
- Track quiz completion so answered quizzes don't re-trigger

## Capabilities

### New Capabilities
- `peaceful-npcs`: NPC entity data, rendering, and proximity-based quiz triggering
- `quiz-system`: Quiz data schema, loading, QuizScreen, answer validation
- `game-state`: Global lives counter, completed quiz tracking, Game Over condition

### Modified Capabilities
- _(none — no existing capability requirements change)_

## Impact

- **New files:** `NpcEntity.java`, `QuizData.java`, `QuizLoader.java`, `QuizScreen.java`, `GameState.java`, `assets/data/quizzes.json`, `assets/data/npcs.json`
- **Modified files:** `GameplayScreen.java` (NPC rendering + proximity check), `JogoOpenSpec.java` (game state holder)
- **New asset:** `assets/data/npcs.json` — NPC positions and quiz associations per map
- **No changes** to existing data formats (`maps.json`, `.tmx` files), build config, or dependencies
