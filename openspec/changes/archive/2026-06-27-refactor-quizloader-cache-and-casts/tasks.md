## 1. QuizLoader static cache

- [x] 1.1 Add `private static Map<String, QuizData> cache` field to `QuizLoader`
- [x] 1.2 Add null guard at top of `load()`: `if (cache != null) return cache;`
- [x] 1.3 Assign parsed result to `cache` before returning

## 2. Replace `((JogoOpenSpec) game)` casts with direct field

- [x] 2.1 `GameplayScreen`: change `private final Game game` → `private final JogoOpenSpec jogoGame`, update constructor parameter, replace all `((JogoOpenSpec) game)` with `jogoGame`
- [x] 2.2 `QuizScreen`: change `private final Game game` → `private final JogoOpenSpec jogoGame`, update constructor parameter, replace all `((JogoOpenSpec) game)` with `jogoGame`
- [x] 2.3 `MainMenuScreen`: change `private final Game game` → `private final JogoOpenSpec jogoGame`, update constructor parameter, replace all `((JogoOpenSpec) game)` with `jogoGame`

## 3. Verification

- [x] 3.1 Build compiles successfully (`./gradlew :core:compileJava`)
- [ ] 3.2 Run game and verify NPCs, quizzes, lives HUD, game over reset, and win reset all work as before
