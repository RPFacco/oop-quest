## 1. GameState: reset method

- [x] 1.1 Add `public void reset()` method to `GameState.java` that sets `lives = 5` and calls `completedQuizzes.clear()`

## 2. QuizScreen: fix leaks and double render

- [x] 2.1 Add `GlyphLayout` field to `QuizScreen` for text measurement
- [x] 2.2 Replace double `font.draw()` question centering with `GlyphLayout`: measure first, draw once at correct x
- [x] 2.3 Add explicit `dispose()` call before `game.setScreen(new MainMenuScreen(...))` when lives reach 0 (line 121)

## 3. NpcLoader: cache and error logging

- [x] 3.1 Add `private static Map<String, Array<NpcEntity>> cache` field to `NpcLoader`
- [x] 3.2 Modify `load()` to check cache first: return cached data if not null, otherwise load from file, store in cache, and return
- [x] 3.3 Add `Gdx.app.log("NpcLoader", "Quiz ID " + quizId + " not found for NPC at (" + x + ", " + y + ")")` error log in `GameplayScreen.checkNpcProximity()` when `quiz == null`

## 4. GameplayScreen: wider trigger and lives HUD

- [x] 4.1 Change NPC trigger multiplier from 1.5f to 2.0f in `checkNpcProximity()`
- [x] 4.2 Add `SpriteBatch` and `BitmapFont` fields to `GameplayScreen`
- [x] 4.3 Initialize `SpriteBatch` and `BitmapFont` in `show()`
- [x] 4.4 Draw "Lives: N" text in top-left corner during `render()`, after map render (using SpriteBatch, before ShapeRenderer.begin)
- [x] 4.5 Add `batch.dispose()` and `font.dispose()` to `GameplayScreen.dispose()`

## 5. MainMenuScreen: reset game state on new game

- [x] 5.1 In `MainMenuScreen.render()`, call `((JogoOpenSpec) game).getGameState().reset()` before `game.setScreen(new GameplayScreen(game))`

## 6. Verification

- [x] 6.1 Build compiles successfully
- [ ] 6.2 Walk to NPC, answer wrong until lives reach 0: verify no resource leak (game returns to menu cleanly)
- [ ] 6.3 Click to start new game: verify lives back to 5 and NPC is orange again (quiz not completed)
- [ ] 6.4 Walk to NPC with 2.0x trigger: verify quiz triggers from slightly further away
- [ ] 6.5 Walk through NPC: verify error log appears if quiz ID is missing (manual check with modified data)
