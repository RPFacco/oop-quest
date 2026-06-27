## ADDED Requirements

### Requirement: Map loads and renders from .tmx file
The system SHALL load a Tiled `.tmx` map file and render it using libGDX's `OrthogonalTiledMapRenderer`.

#### Scenario: Map file exists and loads successfully
- **WHEN** the game starts and `maps.json` points to a valid `.tmx` file
- **THEN** the map SHALL be rendered on screen as the current gameplay view

#### Scenario: Map file is missing or invalid
- **WHEN** the referenced `.tmx` file cannot be loaded
- **THEN** the game SHALL display an error message and return to the main menu

### Requirement: Player click-to-move movement
The player SHALL move toward the clicked position on the map when the mouse is clicked.

#### Scenario: Player clicks on a valid position within map bounds
- **WHEN** the player left-clicks on a position within the map area
- **THEN** the player character SHALL move toward that position at a constant speed
- **THEN** the player SHALL stop when reaching the clicked position

#### Scenario: Player clicks outside map bounds
- **WHEN** the player clicks outside the map area
- **THEN** no movement SHALL occur

### Requirement: Player is clamped to map bounds
The player SHALL NOT move beyond the edges of the map.

#### Scenario: Player reaches the map border
- **WHEN** the player's movement would take them past any edge of the map
- **THEN** the player SHALL be stopped at the edge

### Requirement: Screen flow: MainMenu → Gameplay
The system SHALL display a main menu screen on launch, and transition to the gameplay screen on user action.

#### Scenario: Launch game
- **WHEN** the game is launched
- **THEN** the MainMenuScreen SHALL be displayed

#### Scenario: Start gameplay
- **WHEN** the player clicks or presses a key on the main menu
- **THEN** the GameplayScreen SHALL be displayed with the loaded map and player

### Requirement: maps.json data loading
The system SHALL load and parse `assets/data/maps.json` to determine the starting map.

#### Scenario: maps.json loads successfully
- **WHEN** the game launches
- **THEN** `maps.json` SHALL be parsed to extract `startMap` and map connection data

#### Scenario: maps.json is malformed
- **WHEN** `maps.json` cannot be parsed
- **THEN** the game SHALL display an error and return to the main menu
