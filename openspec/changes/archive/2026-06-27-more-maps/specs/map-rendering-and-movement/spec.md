## MODIFIED Requirements

### Requirement: Player is clamped to map bounds (modified)
The player SHALL NOT move beyond the edges of the map, UNLESS the edge has a directional connection to another map, in which case a map transition SHALL occur instead.

#### Scenario: Player reaches a map border with no connection (unchanged)
- **WHEN** the player's movement would take them past any edge of the map
- **AND** that edge's connection is `null`
- **THEN** the player SHALL be stopped at the edge

#### Scenario: Player reaches a map border with a connection (new)
- **WHEN** the player's movement would take them past any edge of the map
- **AND** that edge's connection points to another map ID
- **THEN** the destination map SHALL be loaded
- **THEN** the player SHALL be positioned on the opposite edge of the destination map
- **THEN** the player's movement target SHALL be cleared
