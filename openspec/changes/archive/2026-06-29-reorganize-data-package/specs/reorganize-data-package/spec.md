## ADDED Requirements

### Requirement: data package split into subpackages
The `data/` package SHALL be split into `data/model/` and `data/loader/` subpackages.

#### Scenario: Entities in model subpackage
- **WHEN** inspecting the project structure
- **THEN** `MoveEntity`, `NpcEntity`, `ProjectileEntity`, `QuizData`, `MapData`, `MapEntry`, and `EnemyEntity` SHALL be in `data/model/`

#### Scenario: Loaders in loader subpackage
- **WHEN** inspecting the project structure
- **THEN** `MapLoader`, `NpcLoader`, `QuizLoader`, and `EnemyLoader` SHALL be in `data/loader/`

#### Scenario: Project compiles
- **WHEN** building the project
- **THEN** all imports SHALL reference the correct subpackages
- **AND** the project SHALL compile without errors
