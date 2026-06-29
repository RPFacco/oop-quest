## 1. Add HP and death to EnemyEntity

- [x] 1.1 Add `int hp`, `int maxHp`, `boolean alive` fields (default 8, 8, true)
- [x] 1.2 Add `takeDamage(int amount)` method
- [x] 1.3 Add `isAlive()`, `getHp()`, `getMaxHp()` getters

## 2. Update EnemySystem

- [x] 2.1 `update()` — skip dead enemies (don't run movement strategy)
- [x] 2.2 `updateShooting()` — skip dead enemies
- [x] 2.3 `findNearest()` — skip dead enemies
- [x] 2.4 `render()` — gray for dead, red for alive
- [x] 2.5 Add `renderHealthBars(ShapeRenderer)` — green bar below alive enemies
- [x] 2.6 Add `getAliveEnemies()` — return filtered array

## 3. Apply damage in ProjectileSystem

- [x] 3.1 Replace `circleEnemyCollision()` boolean check with inlined loop that calls `enemy.takeDamage(1)` on hit

## 4. Wire up in GameplayScreen

- [x] 4.1 Pass `enemySystem.getAliveEnemies()` to `projectileSystem.update()`
- [x] 4.2 Call `enemySystem.renderHealthBars(shapeRenderer)` in render loop

## 5. Verify

- [x] 5.1 Build project with gradlew
- [x] 5.2 Run game and confirm: enemy takes 8 hits, bar shrinks, turns gray on death
