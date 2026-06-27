## Context

The game has a working movement system with 3 maps and map transitions via move entities. No NPCs, quizzes, lives, or game-over logic exist. The game design doc (`docs/game-design.md`) specifies peaceful NPCs on maps 1 and 2 that trigger OOP quizzes on proximity.

The existing codebase uses simple data-driven patterns: `MoveEntity` data class loaded from JSON, `ShapeRenderer` for colored rectangles, and `Screen`-based state management via `Game.setScreen()`.

## Goals / Non-Goals

**Goals:**
- Peaceful NPCs on map01 and map02 that trigger quizzes when approached
- Quiz data loaded from `quizzes.json` and displayed via a dedicated `QuizScreen`
- Lives system (5 lives, lost on wrong answers, game over at 0)
- Completed quiz tracking that persists across map transitions
- Visual feedback: orange NPC → gray NPC after completion

**Non-Goals:**
- Enemy NPCs, combat, or projectiles (deferred to a future change)
- Map obstacles or collision with map geometry
- Audio or visual effects beyond colored rectangles
- Procedural quiz generation or randomized question pools

## Decisions

### Decision 1: Separate npcs.json for NPC data
**Chosen:** `assets/data/npcs.json`, organized by map ID, following the same pattern as `maps.json`.

**Alternatives considered:**
- Embedding NPC data in `maps.json` (avoids extra file but couples NPC and map infrastructure)
- Using TMX object layers (more Tiled-idiomatic but adds TMX parsing complexity)

**Rationale:** A separate file keeps concerns separated, mirrors the existing data-driven pattern, and requires no changes to the map loading pipeline.

### Decision 2: Trigger zone larger than NPC body
**Chosen:** NPC trigger zone is 1.5x the NPC body size, centered on the NPC position.

**Rationale:** The player walks toward an NPC, and the larger zone triggers the quiz before the player collides with the NPC visually. This feels more natural than requiring pixel-perfect overlap with a small NPC body.

### Decision 3: One-shot quiz trigger
**Chosen:** Proximity triggers the quiz once. After completion, proximity does nothing. Completed NPCs render gray.

**Rationale:** Simpler state management, avoids re-answering annoyance. The game design says NPCs have "a specific, fixed quiz" — answering it multiple times would be meaningless.

### Decision 4: QuizScreen as a separate Screen, not an overlay
**Chosen:** `QuizScreen` extends `Screen`, activated via `game.setScreen(new QuizScreen(game, gameplayScreen, quiz))`. GameplayScreen is referenced so QuizScreen can return to it.

**Rationale:** Per game design doc — "separate static screen (not an overlay or popup)". A full Screen cleanly pauses the game world via disposal, avoids rendering complications, and fits the existing architecture.

### Decision 5: GameState in JogoOpenSpec (Game subclass)
**Chosen:** A `GameState` object held in `JogoOpenSpec`, containing `int lives` and `Set<String> completedQuizzes`.

**Rationale:** `Game` persists across `Screen` transitions (hide/show), so it's the natural place for global state. Both `GameplayScreen` and `QuizScreen` access it via `((JogoOpenSpec) game).gameState`.

### Decision 6: Orange for active NPCs, gray for completed
**Chosen:** Active NPCs render as orange `(255, 165, 0)` rectangles. Completed NPCs render as gray `(128, 128, 128)` rectangles.

**Rationale:** Orange contrasts well against all three map colors (green, blue, gold). Gray clearly communicates "done" without requiring additional icons or text.

## Data Formats

### npcs.json
```json
{
  "map01": [
    { "x": 400, "y": 300, "width": 32, "height": 32, "quizId": "1" }
  ],
  "map02": [
    { "x": 960, "y": 540, "width": 32, "height": 32, "quizId": "2" }
  ]
}
```

### quizzes.json (per game-design.md)
```json
{
  "1": {
    "question": "What is encapsulation in OOP?",
    "choices": [
      "Hiding internal details and exposing an interface",
      "Creating multiple instances of a class",
      "Inheriting behavior from a parent class",
      "Overriding a method from the superclass"
    ],
    "correct": 0
  }
}
```

## Architecture

```
JogoOpenSpec (Game)
  └── gameState: GameState { lives, completedQuizzes }

MainMenuScreen ──► GameplayScreen
                       │
                       ├── checkMoveEntityOverlap()    [existing]
                       ├── checkNpcProximity()         [new]
                       │      └── overlap? ──► QuizScreen
                       ├── render()                    [modified]
                       │      └── draw NPCs (orange/gray)
                       └── npcs.json / NpcEntity       [new data]

QuizScreen (Screen)
  ├── renders question + 4 choices
  ├── click correct → mark complete, back to GameplayScreen
  └── click wrong  → lose 1 life, stay on QuizScreen
```

## Risks / Trade-offs

- **Risk:** Player walks past an NPC and the quiz triggers unintentionally. → **Mitigation:** The 1.5x trigger zone is generous but not huge. If too sensitive, reduce to 1.25x or require overlap with the body rectangle directly.
- **Risk:** `QuizScreen` holding a reference to `GameplayScreen` could cause memory leaks. → **Mitigation:** `QuizScreen` does not keep the backing `GameplayScreen` alive — it just references the `Screen` object already managed by `JogoOpenSpec`.
- **Trade-off:** No quiz UI styling (fonts, colors, button states) in this first iteration. The placeholder visuals suffice for v1 but will need polish later.
