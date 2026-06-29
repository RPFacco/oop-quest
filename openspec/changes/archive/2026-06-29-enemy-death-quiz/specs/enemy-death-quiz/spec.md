# enemy-death-quiz Specification

## Purpose

The first enemy kill triggers quiz 3. Completing all 3 quizzes wins the game.

## Requirements

### Requirement: Quiz 3 exists

A third quiz with ID "3" SHALL be available in `quizzes.json`.

#### Scenario: Quiz 3 loads

- **WHEN** `QuizLoader.load()` is called
- **THEN** the result SHALL contain a key "3"
- **AND** it SHALL have a question, 4 choices, and a correct answer index

### Requirement: Enemy kill triggers quiz

When a player projectile kills an enemy, quiz 3 SHALL open if not already completed.

#### Scenario: First kill opens quiz

- **WHEN** a player projectile reduces an enemy's HP to 0
- **AND** quiz 3 has not been completed
- **THEN** the `QuizScreen` SHALL open with quiz 3

#### Scenario: Subsequent kills do nothing

- **WHEN** an enemy dies
- **AND** quiz 3 has already been completed
- **THEN** no quiz SHALL open

### Requirement: Win at 3 quizzes

The game SHALL be won when 3 quizzes are completed.

#### Scenario: Complete quiz 3 triggers victory

- **WHEN** the player correctly answers quiz 3
- **AND** quizzes 1 and 2 are also completed
- **THEN** the game SHALL reset to the main menu (victory)

#### Scenario: Partial progress returns to game

- **WHEN** the player correctly answers quiz 3
- **AND** not all other quizzes are completed
- **THEN** the player SHALL return to gameplay
