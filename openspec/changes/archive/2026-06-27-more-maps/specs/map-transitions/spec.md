## ADDED Requirements

### Requirement: Player transitions to connected map on border contact
When the player reaches a map border that has a directional connection to another map, the system SHALL load the connected map and position the player on the opposite border of the destination map.

#### Scenario: Player reaches east border with a connection
- **WHEN** the player's position reaches `MAP_WIDTH - player.width` on the x-axis
- **AND** the current map's `connections.east` points to a valid map ID
- **THEN** the destination map SHALL be loaded and rendered
- **THEN** the player SHALL be positioned at `x = 0` with their current y preserved (clamped to `[0, MAP_HEIGHT - player.height]`)
- **THEN** the player's movement target SHALL be cleared

#### Scenario: Player reaches west border with a connection
- **WHEN** the player's position reaches `x = 0`
- **AND** the current map's `connections.west` points to a valid map ID
- **THEN** the destination map SHALL be loaded and rendered
- **THEN** the player SHALL be positioned at `x = MAP_WIDTH - player.width` with their current y preserved (clamped)
- **THEN** the player's movement target SHALL be cleared

#### Scenario: Player reaches north border with a connection
- **WHEN** the player's position reaches `MAP_HEIGHT - player.height` on the y-axis
- **AND** the current map's `connections.north` points to a valid map ID
- **THEN** the destination map SHALL be loaded and rendered
- **THEN** the player SHALL be positioned at `y = 0` with their current x preserved (clamped to `[0, MAP_WIDTH - player.width]`)
- **THEN** the player's movement target SHALL be cleared

#### Scenario: Player reaches south border with a connection
- **WHEN** the player's position reaches `y = 0`
- **AND** the current map's `connections.south` points to a valid map ID
- **THEN** the destination map SHALL be loaded and rendered
- **THEN** the player SHALL be positioned at `y = MAP_HEIGHT - player.height` with their current x preserved (clamped)
- **THEN** the player's movement target SHALL be cleared

### Requirement: Dead-end borders clamp the player
When the player reaches a map border that has no connection (`null`), the system SHALL clamp the player to the map edge (existing behavior).

#### Scenario: Player reaches a dead-end border
- **WHEN** the player reaches any map border
- **AND** that border's connection is `null`
- **THEN** the player SHALL be stopped at the edge without a map transition

### Requirement: Corner transitions use fixed direction priority
When the player is positioned such that multiple borders are contacted simultaneously (a corner), the transition direction SHALL be determined by a fixed priority order: north > south > east > west.

#### Scenario: Player contacts north and east borders simultaneously with both connected
- **WHEN** the player is at the top-right corner of a map
- **AND** both `connections.north` and `connections.east` are non-null
- **THEN** the north transition SHALL be taken (loading the map connected to the north border)

### Requirement: Map resources are cleaned up on transition
When transitioning between maps, the system SHALL dispose of the previous map's resources to prevent memory leaks.

#### Scenario: Previous map resources disposed
- **WHEN** a map transition occurs
- **THEN** the previous `TiledMap` and `OrthogonalTiledMapRenderer` SHALL be disposed
- **THEN** the new map's `TiledMap` and `OrthogonalTiledMapRenderer` SHALL be loaded and set as active

### Requirement: Current map ID is tracked
The system SHALL track the current map's ID to support connection lookups and state management.

#### Scenario: Map ID updates on transition
- **WHEN** a map transition occurs
- **THEN** the system's current map ID SHALL be updated to match the destination map's ID

### Requirement: GameplayScreen stores MapData for runtime lookups
The `GameplayScreen` SHALL store the parsed `MapData` object (from `MapLoader.load()`) to enable runtime lookup of map entries and connections without re-parsing `maps.json`.

#### Scenario: MapData available after transition
- **WHEN** the game is on the `GameplayScreen`
- **THEN** the `MapData` object SHALL be accessible for looking up the current and connected map entries
- **THEN** transition logic SHALL use this in-memory `MapData` rather than re-reading the JSON file
