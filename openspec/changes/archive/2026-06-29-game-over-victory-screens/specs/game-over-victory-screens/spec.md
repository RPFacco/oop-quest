# game-over-victory-screens Specification

## Purpose

Dedicated end screens for death (Game Over) and completion (Victory).

## Requirements

### Requirement: Game Over screen

When the player loses all lives, a Game Over screen SHALL be shown.

#### Scenario: Death triggers screen

- **WHEN** the player's lives reach 0
- **THEN** the game SHALL display the GameOverScreen
- **AND** the screen SHALL have a dark red background
- **AND** it SHALL display "GAME OVER"
- **AND** it SHALL display a prompt to continue

#### Scenario: Click returns to menu

- **WHEN** the player clicks/taps on the GameOverScreen
- **THEN** the game SHALL return to the MainMenuScreen

### Requirement: Victory screen

When the player completes all 3 quizzes, a Victory screen SHALL be shown.

#### Scenario: Completion triggers screen

- **WHEN** the player correctly answers the last quiz
- **THEN** the game SHALL display the VictoryScreen
- **AND** the screen SHALL have a dark green background
- **AND** it SHALL display "YOU FINISHED!"
- **AND** it SHALL display "Congratulations!"
- **AND** it SHALL display a prompt to continue

#### Scenario: Click returns to menu

- **WHEN** the player clicks/taps on the VictoryScreen
- **THEN** the game SHALL return to the MainMenuScreen
