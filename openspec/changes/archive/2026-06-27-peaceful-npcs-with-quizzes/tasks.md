## 1. Data Models

- [x] 1.1 Create `NpcEntity.java` — data class with `x`, `y`, `width`, `height`, `quizId` fields (public floats/String, same pattern as `MoveEntity`)
- [x] 1.2 Create `QuizData.java` — data class with `String question`, `String[] choices`, `int correct`
- [x] 1.3 Create `GameState.java` — class with `int lives` (starts at 5) and `Set<String> completedQuizzes`
- [x] 1.4 Create `NpcLoader.java` — reads `assets/data/npcs.json` into `Map<String, Array<NpcEntity>>` (one array per map ID), using libGDX `JsonReader`/`JsonValue`
- [x] 1.5 Create `QuizLoader.java` — reads `assets/data/quizzes.json` into `Map<String, QuizData>` indexed by quiz ID, using libGDX `JsonReader`/`JsonValue`

## 2. Game State Integration

- [x] 2.1 Add `GameState gameState` field to `JogoOpenSpec.java`, initialized in `create()` with 5 lives and empty completed set
- [x] 2.2 Add getter `getGameState()` to `JogoOpenSpec` so screens can access it via `((JogoOpenSpec) game).getGameState()`

## 3. NPC Loading, Rendering, and Proximity in GameplayScreen

- [x] 3.1 Add `Array<NpcEntity> npcs` and `Rectangle npcTriggerRect` fields to `GameplayScreen`
- [x] 3.2 Load NPCs for current map in `loadMap()` using `NpcLoader`, keyed by `currentMapId`
- [x] 3.3 Render NPCs in `render()` between move entities and player: active NPCs in orange `(255, 165, 0)`, completed NPCs (quiz ID in `completedQuizzes`) in gray `(128, 128, 128)`
- [x] 3.4 Add `checkNpcProximity()` method: for each NPC, build trigger rect at 1.5x body size centered on NPC, check `playerRect.overlaps(npcTriggerRect)`, and if not completed → open `QuizScreen`
- [x] 3.5 Call `checkNpcProximity()` in `render()` after `checkMoveEntityOverlap()`

## 4. QuizScreen

- [x] 4.1 Create `QuizScreen.java` implementing `Screen` — constructor receives `Game`, `GameplayScreen`, and `QuizData`
- [x] 4.2 Render question text centered near top of screen using libGDX `BitmapFont`
- [x] 4.3 Render four answer choices as clickable text areas, evenly spaced vertically
- [x] 4.4 Handle click input: unproject touch coords, detect which choice was clicked
- [x] 4.5 On correct answer: add quiz ID to `completedQuizzes` in game state, return to `GameplayScreen` via `game.setScreen(gameplayScreen)`
- [x] 4.6 On wrong answer: decrement lives in game state. If lives > 0, stay on quiz screen. If lives == 0, return to `MainMenuScreen`
- [x] 4.7 Implement `dispose()` to clean up font and shape renderer resources

## 5. Data Files

- [x] 5.1 Create `assets/data/npcs.json` with NPC on map01 (quizId "1") and NPC on map02 (quizId "2") at reasonable positions away from move entities
- [x] 5.2 Create `assets/data/quizzes.json` with two OOP quiz entries ("1" and "2") — each with question, 4 choices, and correct index

## 6. Verification

- [x] 6.1 Build compiles successfully
- [ ] 6.2 Walk player to NPC on map01: verify QuizScreen opens with correct quiz
- [ ] 6.3 Answer incorrectly: verify life decrements and quiz stays open
- [ ] 6.4 Answer correctly: verify return to gameplay and NPC turns gray
- [ ] 6.5 Walk to NPC again: verify quiz does not re-trigger
- [ ] 6.6 Transition to map02 via move entity: verify NPC renders and quiz triggers
- [ ] 6.7 Transition back to map01: verify NPC is still gray and quiz does not re-trigger
- [ ] 6.8 Answer all questions wrong until lives = 0: verify game over returns to menu
