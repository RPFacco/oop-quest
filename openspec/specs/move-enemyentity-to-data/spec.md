# move-enemyentity-to-data Specification

## Purpose

Move `EnemyEntity` from the `game` package to `data` for consistency with other entity classes.

## Requirements

### Requirement: EnemyEntity in data package
`EnemyEntity` SHALL be in the `com.rpfacco.oopquest.game.data` package.

#### Scenario: File moved
- **WHEN** inspecting the project structure
- **THEN** `EnemyEntity.java` SHALL be in the `data/` directory

#### Scenario: All imports updated
- **WHEN** building the project
- **THEN** all references to `EnemyEntity` SHALL use the correct import
- **AND** the project SHALL compile without errors
