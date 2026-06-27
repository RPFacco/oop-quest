## ADDED Requirements

### Requirement: Game starts with 5 lives
The system SHALL initialize the player with 5 lives when a new game begins.

#### Scenario: New game
- **WHEN** the game starts from the main menu
- **THEN** the player SHALL have 5 lives

#### Scenario: Player loses a life
- **WHEN** the player answers a quiz question incorrectly
- **THEN** lives SHALL decrease by 1
- **THEN** the current life count SHALL be reflected in game state

### Requirement: Completed quizzes persist across map transitions
The system SHALL track which quiz IDs have been completed. This state SHALL be preserved when the player moves between maps.

#### Scenario: Quiz completed on map01, player moves to map02
- **WHEN** the player completes a quiz on map01
- **THEN** the quiz SHALL be marked as completed in game state
- **WHEN** the player transitions to map02 via a move entity
- **THEN** the quiz completion status SHALL be retained

#### Scenario: Player returns to a map with a completed NPC
- **WHEN** the player returns to a map previously visited
- **AND** an NPC on that map had its quiz completed
- **THEN** the NPC SHALL render as completed (gray)
- **THEN** the NPC SHALL NOT trigger its quiz again

### Requirement: Zero lives triggers game over
When lives reach zero, the game SHALL transition to a game over state.

#### Scenario: Lives reach zero
- **WHEN** lives reach 0
- **THEN** the game SHALL display a Game Over message
- **THEN** the player SHALL be returned to the main menu

### Requirement: Game state is held at the Game level
Game state (lives, completed quizzes) SHALL live in the `JogoOpenSpec` class so it persists across screen transitions.

#### Scenario: Screen transitions preserve state
- **WHEN** transitioning from `GameplayScreen` to `QuizScreen` and back
- **THEN** game state SHALL remain unchanged
- **WHEN** transitioning between maps within `GameplayScreen`
- **THEN** game state SHALL remain unchanged
