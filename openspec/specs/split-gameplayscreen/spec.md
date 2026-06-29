# split-gameplayscreen Specification

## Purpose

`GameplayScreen` (272 lines) is a god class mixing 6+ responsibilities: rendering, input handling, map management, collision detection, game rules, and lifecycle. Decomposing into focused classes improves cohesion and prepares the codebase for future features.

## Requirements

### Requirement: GameplayScreen refactored into focused classes
`GameplayScreen` SHALL be decomposed into focused classes with no change to gameplay behavior or visual output.

#### Scenario: Rendering extracted to WorldRenderer
- **WHEN** inspecting the project structure
- **THEN** all ShapeRenderer and SpriteBatch rendering logic SHALL live in a `WorldRenderer` class
- **AND** `GameplayScreen` SHALL delegate rendering to `WorldRenderer`

#### Scenario: Map lifecycle extracted to MapManager
- **WHEN** inspecting the project structure
- **THEN** tiled map loading, disposal, and transitions SHALL live in a `MapManager` class
- **AND** `GameplayScreen` SHALL delegate map operations to `MapManager`

#### Scenario: Input extracted to InputController
- **WHEN** inspecting the project structure
- **THEN** touch input, ESC handling, and homing projectile creation SHALL be encapsulated in an `InputController` class
- **AND** `GameplayScreen` SHALL delegate input processing to `InputController`

#### Scenario: GameplayScreen uses BaseScreen
- **WHEN** inspecting `GameplayScreen`
- **THEN** it SHALL extend `BaseScreen`
- **AND** it SHALL NOT declare its own `camera`, `viewport`, `batch`, or `font` fields
- **AND** it SHALL NOT create or dispose `camera`, `viewport`, `batch`, or `font`

#### Scenario: Gameplay behavior unchanged
- **WHEN** the game runs during gameplay
- **THEN** player movement, enemy behavior, NPC interaction, projectile physics, map transitions, and game-over detection SHALL behave identically to before the refactoring
