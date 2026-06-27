## Why

Two code quality issues identified during review of the NPC/quiz system:
- `QuizLoader.load()` parses JSON from disk on every correct answer (inside `handleInput()`), while `NpcLoader` already has a static cache
- `((JogoOpenSpec) game)` is cast repeatedly across `GameplayScreen` and `QuizScreen` — noisy and fragile

Both are internal refactors with no behavioral change.

## What Changes

- Add static cache to `QuizLoader` (mirror `NpcLoader` pattern)
- Replace `Game game` field with `JogoOpenSpec jogoGame` in constructors where `getGameState()` is needed, eliminating casts

## Capabilities

### New Capabilities

None — no new capabilities introduced.

### Modified Capabilities

None — no spec-level requirement changes. Both refactors are internal implementation details.

## Impact

- `QuizLoader.java`: add `private static Map<String, QuizData> cache` and guard clause
- `QuizScreen.java`: change field type from `Game` to `JogoOpenSpec`, remove casts
- `GameplayScreen.java`: change field type from `Game` to `JogoOpenSpec`, remove casts
- `MainMenuScreen.java`: change field type from `Game` to `JogoOpenSpec`, remove casts
