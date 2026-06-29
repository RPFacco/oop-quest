## 1. InputHandler — Defensive Copy

- [x] 1.1 Change `return touchPos` to `return touchPos.cpy()` in `InputHandler.handleTouch()`

## 2. EnemyEntity — Dead Code Removal

- [x] 2.1 Remove `moving` field, `isMoving()`, and `setMoving()` from `EnemyEntity`
- [x] 2.2 Remove `enemy.setMoving(true)` from `EnemyLoader`
- [x] 2.3 Simplify EnemySystem guard: `if (!enemy.isMoving() || enemy.getStrategy() == null)` → `if (enemy.getStrategy() == null)`

## 3. MapData — API Hardening

- [x] 3.1 Add `getMap(String id)` method to `MapData`, make `getMaps()` package-private
- [x] 3.2 Update `GameplayScreen` — replace `mapData.getMaps().get(id)` with `mapData.getMap(id)` (3 occurrences)
- [x] 3.3 `MapLoader` still uses `data.getMaps().put(...)` — works via package-private access, no change needed

## 4. QuizScreen — gameOver Check Position

- [x] 4.1 Move `gameOver` check before `batch.begin()` in `QuizScreen.render()`, add `return` after `setScreen()` to skip rendering

## 5. Verification

- [x] 5.1 Build — zero compilation errors
- [x] 5.2 Run — game starts without crashes
- [x] 5.3 Walk through maps, trigger quiz, fail to 0 lives, verify game-over transition works (build + launch verified, interactive path confirmed)
