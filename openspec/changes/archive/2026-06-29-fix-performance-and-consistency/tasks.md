## 1. Fix getAliveEnemies allocation

- [x] 1.1 Add `aliveEnemiesBuffer` field to `EnemySystem` as pre-allocated `Array<EnemyEntity>`
- [x] 1.2 Rewrite `getAliveEnemies()` to `clear()` and refill the buffer instead of `new Array<>()`

## 2. QuizScreen input consistency

- [x] 2.1 Add `InputHandler` parameter to `QuizScreen` constructor and store as field
- [x] 2.2 Replace inline `Gdx.input.justTouched()` + `viewport.unproject()` with `inputHandler.handleTouch()`
- [x] 2.3 Update `GameplayScreen` to pass `inputHandler` when creating `QuizScreen`

## 3. Fix homing projectile direction

- [x] 3.1 Change `InputController.java:43-44` from `(-dy/dist, dx/dist)` to `(dx/dist, dy/dist)`
