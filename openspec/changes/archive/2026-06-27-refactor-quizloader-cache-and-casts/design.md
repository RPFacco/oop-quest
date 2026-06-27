## Context

Two code smells from the NPC/quiz implementation:

1. `QuizLoader.load()` is called on every correct answer inside `QuizScreen.handleInput()`. The method reads and parses `quizzes.json` from disk each time. `NpcLoader` already uses a static cache — `QuizLoader` should follow the same pattern for consistency and performance.

2. `((JogoOpenSpec) game)` is cast 5+ times across three screens (`GameplayScreen`, `QuizScreen`, `MainMenuScreen`). Each screen only ever receives a `JogoOpenSpec` instance. Storing the concrete type eliminates casts and makes `getGameState()` accessible without casting.

## Goals / Non-Goals

**Goals:**
- Add static cache to `QuizLoader.load()` matching `NpcLoader` pattern
- Replace `Game game` with `JogoOpenSpec jogoGame` in affected screens
- Zero behavioral change

**Non-Goals:**
- No spec-level requirement changes
- No new features

## Decisions

**1. QuizLoader cache mirrors NpcLoader exactly**

Both are static `Map<String, ...>` with a null guard. Consistent pattern, easy to read.

**2. Constructor injection of JogoOpenSpec**

The `game` parameter in all three screen constructors is already a `JogoOpenSpec` instance. Changing the constructor parameter type from `Game` to `JogoOpenSpec` is a local change — no callers need updating since all callers pass `this` (JogoOpenSpec) or `game` (already a JogoOpenSpec reference). The `Game game` field becomes `JogoOpenSpec jogoGame`, and `((JogoOpenSpec) game)` becomes `jogoGame`.

## Risks / Trade-offs

- **Low risk** — Both are mechanical refactors. Compiler catches any mismatch.
- `QuizScreen` and `GameplayScreen` have a `game.setScreen(...)` call (inherited from `Game`). With `JogoOpenSpec jogoGame`, this still works because `JogoOpenSpec extends Game`.
