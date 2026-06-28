## ADDED Requirements

### Requirement: Enemy is loaded from enemies.json
The system SHALL load enemy data from `assets/data/enemies.json`, organized by map ID.

#### Scenario: Enemies exist for a map
- **WHEN** the player enters a map
- **THEN** the system SHALL load the enemy array for that map ID from enemies.json

#### Scenario: No enemies for a map
- **WHEN** the player enters a map with no enemy entry in enemies.json
- **THEN** the system SHALL handle it gracefully and render no enemies

### Requirement: Enemy patrols waypoints in a loop
The enemy SHALL move toward the current target waypoint at a constant speed. Upon reaching it (within arrival distance), the enemy SHALL advance to the next waypoint. After the last waypoint, the enemy SHALL loop back to the first.

#### Scenario: Enemy moves between waypoints
- **WHEN** the enemy update runs each frame
- **THEN** the enemy SHALL move toward the current target waypoint at `speed * delta`

#### Scenario: Enemy reaches a waypoint
- **WHEN** the enemy is within arrival distance (2 px) of the current target waypoint
- **THEN** the enemy SHALL snap to the waypoint position
- **THEN** the enemy SHALL advance to the next waypoint in the sequence

#### Scenario: Enemy loops after last waypoint
- **WHEN** the enemy reaches the last waypoint
- **THEN** the enemy SHALL set the next target to the first waypoint

### Requirement: Enemy renders as a filled red rectangle
The enemy SHALL be rendered as a 32×32 filled rectangle using red color (1, 0, 0).

#### Scenario: Enemy is visible on screen
- **WHEN** the gameplay screen renders
- **THEN** the enemy SHALL be drawn as a filled rectangle at its current position
- **THEN** the rectangle SHALL be 32×32 pixels
- **THEN** the color SHALL be red (1, 0, 0)

### Requirement: Enemy speed is 75% of player speed
The enemy SHALL move at 240 pixels per second.

#### Scenario: Enemy speed is applied
- **WHEN** the enemy moves toward a waypoint
- **THEN** the step distance SHALL be `240 * delta`
