## 1. Create NpcTriggerHandler interface

- [x] 1.1 Create `NpcTriggerHandler.java` in `com.jogoopenspec.game` package with single method `void onQuizTrigger(String quizId, QuizData quiz)`

## 2. Create NpcSystem class

- [x] 2.1 Create `NpcSystem.java` with fields: `Array<NpcEntity> npcs`, `Rectangle npcTriggerRect`, `Map<String, QuizData> quizzes`
- [x] 2.2 Add constructor that initializes `npcTriggerRect` and loads quizzes via `QuizLoader.load()`
- [x] 2.3 Add `setNpcs(Array<NpcEntity> npcs)` method
- [x] 2.4 Add `checkProximity(Rectangle playerRect, GameState gameState, NpcTriggerHandler handler)` method with same logic as current `GameplayScreen.checkNpcProximity()`
- [x] 2.5 Add `render(ShapeRenderer shapeRenderer, GameState gameState)` method with same logic as current NPC rendering inside `GameplayScreen.render()`

## 3. Wire NpcSystem into GameplayScreen

- [x] 3.1 Add `private NpcSystem npcSystem` field to `GameplayScreen`
- [x] 3.2 Initialize `npcSystem` in `show()`, remove `npcTriggerRect` and `quizzes` initialization
- [x] 3.3 Replace `npcs = NpcLoader.load().get(mapId)` in `loadMap()` with `npcSystem.setNpcs(NpcLoader.load().get(mapId))`
- [x] 3.4 Replace `checkNpcProximity()` call with `npcSystem.checkProximity(playerRect, jogoGame.getGameState(), this::onNpcTrigger)`
- [x] 3.5 Remove `checkNpcProximity()` method from `GameplayScreen`
- [x] 3.6 Replace inline NPC rendering with `npcSystem.render(shapeRenderer, jogoGame.getGameState())`
- [x] 3.7 Remove `private Array<NpcEntity> npcs` field from `GameplayScreen`

## 4. Verification

- [x] 4.1 Build compiles successfully (`./gradlew :core:compileJava`)
- [x] 4.2 Run game and verify NPCs render, proximity triggers quiz, and game over/win reset work as before
