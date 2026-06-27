## MODIFIED Requirements

### Requirement: Player is clamped to map bounds
The player SHALL NOT move beyond the edges of the map. Borders are always dead ends.

#### Scenario: Player reaches any map border
- **WHEN** the player's movement would take them past any edge of the map
- **THEN** the player SHALL be stopped at the edge
