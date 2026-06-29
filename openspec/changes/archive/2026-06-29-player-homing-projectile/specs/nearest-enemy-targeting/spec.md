## ADDED Requirements

### Requirement: EnemySystem finds nearest enemy

The `EnemySystem` SHALL provide a method to find the enemy closest to a given player position by Euclidean distance.

#### Scenario: Enemies exist on the map

- **WHEN** `EnemySystem.findNearest(player)` is called
- **AND** there are enemies on the current map
- **THEN** the method SHALL return the enemy with the smallest Euclidean distance to the player

#### Scenario: No enemies exist

- **WHEN** `EnemySystem.findNearest(player)` is called
- **AND** there are no enemies on the current map
- **THEN** the method SHALL return null
