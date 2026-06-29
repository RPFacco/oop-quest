## 1. Add quiz 3 to data

- [x] 1.1 Add quiz "3" entry to `assets/data/quizzes.json`

## 2. Add onEnemyDeath callback to ProjectileSystem

- [x] 2.1 Add `Consumer<EnemyEntity> onEnemyDeath` parameter to `update()` method
- [x] 2.2 Fire callback when `takeDamage()` results in enemy death

## 3. Wire up in GameplayScreen

- [x] 3.1 Pass `this::onEnemyDeath` to `projectileSystem.update()`
- [x] 3.2 Implement `onEnemyDeath(EnemyEntity)` handler: check `isCompleted("3")`, load quiz, open QuizScreen

## 4. Verify

- [x] 4.1 Build project with gradlew
- [ ] 4.2 Run game: kill enemy, confirm quiz 3 opens; complete it, confirm win at 3 quizzes
