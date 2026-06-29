## ADDED Requirements

### Requirement: WaypointMovement starts at first waypoint

Enemies using WaypointMovement SHALL begin their patrol from waypoint index 0
(the first waypoint in the list), not from index 1.

#### Scenario: Enemy with waypoint movement

- **WHEN** an enemy is loaded with a WaypointMovement from a JSON config
- **THEN** the enemy's currentWaypoint SHALL be initialized to 0
