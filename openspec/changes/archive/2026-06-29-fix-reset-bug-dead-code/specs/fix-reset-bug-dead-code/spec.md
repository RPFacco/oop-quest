## ADDED Requirements

### Requirement: Enemy cache is cleared on game restart
`OopQuest.resetGame()` SHALL be called from every game-restart path to ensure enemy cache is cleared alongside game state.

#### Scenario: Main menu starts new game
- **WHEN** the user clicks to start a new game from `MainMenuScreen`
- **THEN** `app.resetGame()` SHALL be called (not `gameState.reset()` alone)

#### Scenario: Player dies and restarts
- **WHEN** the player's lives reach 0 in `GameplayScreen`
- **THEN** `app.resetGame()` SHALL be called before transitioning to `GameOverScreen`

#### Scenario: Player dies during quiz and restarts
- **WHEN** the player's lives reach 0 in `QuizScreen`
- **THEN** `app.resetGame()` SHALL be called before transitioning to `GameOverScreen`

### Requirement: Dead code is removed
Unused public methods SHALL be removed from the codebase.

#### Scenario: Player methods removed
- **WHEN** inspecting `Player.java`
- **THEN** `getSpeed()` and `isMoving()` SHALL NOT exist

#### Scenario: MapManager method removed
- **WHEN** inspecting `MapManager.java`
- **THEN** `getCurrentMapId()` SHALL NOT exist

#### Scenario: EnemySystem method removed
- **WHEN** inspecting `EnemySystem.java`
- **THEN** `getEnemies()` SHALL NOT exist
