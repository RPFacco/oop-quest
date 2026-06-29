## Why

Both endings (death and victory) currently dump the player straight to the main menu with no transition or feedback. Adding dedicated Game Over and Victory screens makes the game feel complete and gives players emotional closure — disappointment on death, celebration on winning.

## What Changes

- New `GameOverScreen`: dark red background, "GAME OVER" text, click to return to menu
- New `VictoryScreen`: dark green background, "YOU FINISHED! Congratulations!" text, click to return to menu
- `GameplayScreen` routes `gameOver` to GameOverScreen instead of MainMenuScreen
- `QuizScreen` routes victory to VictoryScreen instead of MainMenuScreen

## Capabilities

### New Capabilities

- `game-over-victory-screens`: Dedicated end screens for death and completion

## Impact

- `game/GameOverScreen.java` — NEW
- `game/VictoryScreen.java` — NEW
- `game/GameplayScreen.java` — change gameOver transition
- `game/QuizScreen.java` — change victory transition
