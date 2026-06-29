## ADDED Requirements

### Requirement: Starting lives is configurable

The number of starting lives SHALL be defined as a constant in GameConfig,
and GameState SHALL reference that constant instead of using a hardcoded value.

#### Scenario: GameState initializes with configured lives

- **WHEN** a new GameState is created
- **THEN** getLives() SHALL return the value of GameConfig.LIVES
