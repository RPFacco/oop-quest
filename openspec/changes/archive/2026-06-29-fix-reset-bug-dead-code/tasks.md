## 1. Wire resetGame()

- [x] 1.1 `GameplayScreen.java` — remove `app.getGameState().reset()` from escape handler; change to `app.resetGame()` in game-over handler
- [x] 1.2 `QuizScreen.java` — change `app.getGameState().reset()` to `app.resetGame()` in game-over path
- [x] 1.3 `MainMenuScreen.java` — change `gs.reset()` to `app.resetGame()`
- [x] 1.4 Build and confirm no compilation errors

## 2. Remove dead code

- [x] 2.1 `Player.java` — remove `getSpeed()` and `isMoving()` methods
- [x] 2.2 `MapManager.java` — remove `getCurrentMapId()` method
- [x] 2.3 `EnemySystem.java` — remove `getEnemies()` method
- [x] 2.4 Build and confirm no compilation errors

## 3. Build & Verify

- [x] 3.1 Run full gradle build: `./gradlew build`
- [x] 3.2 Run `openspec archive` to save all tasks
