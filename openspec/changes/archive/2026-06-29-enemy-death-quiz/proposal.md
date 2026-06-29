## Why

Killing an enemy currently has no gameplay consequence beyond the enemy turning gray. Triggering quiz 3 on enemy death ties combat to the learning system, and raising the win target to 3 quizzes gives the game a clearer completion arc.

## What Changes

- Add quiz "3" to `quizzes.json` with an OOP question
- `ProjectileSystem.update()` gains an `onEnemyDeath` callback fired when a player projectile kills an enemy
- `GameplayScreen` handles the callback: if quiz 3 not yet completed, opens `QuizScreen` for quiz 3
- Win condition now requires 3 completed quizzes (auto-adjusts via `QuizLoader.load().size()`)

## Capabilities

### New Capabilities

- `enemy-death-quiz`: First enemy kill triggers quiz 3; completing all 3 quizzes wins the game

## Impact

- `assets/data/quizzes.json`: add quiz "3"
- `game/ProjectileSystem.java`: add `Consumer<EnemyEntity> onEnemyDeath` parameter
- `game/GameplayScreen.java`: implement `onEnemyDeath` handler
