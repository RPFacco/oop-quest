## ADDED Requirements

### Requirement: Move entities are loaded from maps.json
The system SHALL load an array of move entities for each map from the `moveEntities` field in `maps.json`.

#### Scenario: Move entities parse successfully
- **WHEN** a map is loaded
- **THEN** its `moveEntities` array SHALL be parsed into `MoveEntity` objects with `x`, `y`, `width`, `height`, `targetMap`, `spawnX`, `spawnY`

#### Scenario: Map has no move entities
- **WHEN** a map is loaded
- **AND** the `moveEntities` field is empty or absent
- **THEN** no move entities SHALL be created for that map

### Requirement: Move entities are rendered on the map
The system SHALL render each move entity as a filled colored rectangle on top of the tile map.

#### Scenario: Move entities visible
- **WHEN** a map with move entities is displayed
- **THEN** each move entity SHALL be rendered as a rectangle at its `(x, y)` position with the specified `width` and `height`
- **THEN** all move entities SHALL use the same golden color `(255, 215, 0)`

### Requirement: Player overlap triggers map transition
When the player's bounding box overlaps a move entity, the system SHALL transition to the move entity's target map and spawn the player at the specified spawn position.

#### Scenario: Player walks into a move entity
- **WHEN** the player's rectangle overlaps a move entity's rectangle
- **THEN** the target map SHALL be loaded and rendered
- **THEN** the player SHALL be positioned at `(spawnX, spawnY)` on the target map
- **THEN** the player's movement target SHALL be cleared

#### Scenario: Player overlaps multiple move entities simultaneously
- **WHEN** the player overlaps more than one move entity at the same time
- **THEN** the first move entity in the array order SHALL be triggered

### Requirement: Move entities do not prevent walking
Move entities are visual-only indicators. They do not block player movement.

#### Scenario: Player walks through a move entity
- **WHEN** the player walks over the area occupied by a move entity
- **THEN** the player SHALL pass through freely without being stopped by the entity itself

### Requirement: Map resources are cleaned up on transition
When transitioning between maps via a move entity, the system SHALL dispose of the previous map's resources.

#### Scenario: Previous map resources disposed
- **WHEN** a move entity transition occurs
- **THEN** the previous `TiledMap` and `OrthogonalTiledMapRenderer` SHALL be disposed
- **THEN** the new map's resources SHALL be loaded and set as active
