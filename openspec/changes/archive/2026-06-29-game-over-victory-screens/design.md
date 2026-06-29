## Context

Both endings use `jogoGame.setScreen(new MainMenuScreen(...))` — no fanfare, no distinction between losing and winning. The game needs a moment of feedback for each outcome.

## Goals / Non-Goals

**Goals:**
- GameOverScreen shows on death (lives = 0)
- VictoryScreen shows on completing all 3 quizzes
- Each screen is a simple full-screen with background color + centered text
- Click anywhere goes to MainMenuScreen
- GameplayScreen and QuizScreen are disposed on transition

**Non-Goals:**
- No animations, particles, or sounds
- No score or stats display
- No "retry" button (just back to menu)

## Decisions

### Decision 1: Separate classes for each screen

Two independent Screen implementations, following the same pattern as MainMenuScreen:

```
GameOverScreen(OopQuest)          VictoryScreen(OopQuest)
┌──────────────────────┐          ┌──────────────────────┐
│  Fundo: 0.15, 0, 0   │          │  Fundo: 0, 0.15, 0   │
│                      │          │                      │
│     GAME OVER        │          │   YOU FINISHED!      │
│                      │          │  Congratulations!    │
│  [click to menu]     │          │  [click to menu]     │
└──────────┬───────────┘          └──────────┬───────────┘
           │                                  │
           ▼                                  ▼
    MainMenuScreen                     MainMenuScreen
```

Both screens:
- Create camera + viewport + batch + font in `show()`
- Clear background with custom color
- Draw text via `font.draw(batch, ...)`
- On `justTouched()`: go to MainMenuScreen and `dispose()`
- Dispose batch + font in `dispose()`

### Decision 2: GameplayScreen routes to GameOverScreen

```java
// Before:
jogoGame.setScreen(new MainMenuScreen(jogoGame));
// After:
jogoGame.setScreen(new GameOverScreen(jogoGame));
```

The `gameState.reset()` and `dispose()` calls before the transition remain unchanged.

### Decision 3: QuizScreen routes to VictoryScreen

```java
// Before:
jogoGame.setScreen(new MainMenuScreen(jogoGame));
// After:
jogoGame.setScreen(new VictoryScreen(jogoGame));
```

The `gameState.reset()` call before the transition remains unchanged.

## Risks / Trade-offs

- **[Memory leak]** GameplayScreen is not disposed when transitioning to GameOverScreen — same issue exists with current MainMenuScreen transition. Not introduced by this change.
- **[QuizScreen disposal]** QuizScreen transitions to VictoryScreen without disposing the previous QuizScreen — same pattern as returning to GameplayScreen. Not introduced.
