## ADDED Requirements

### Requirement: Player fires homing projectile with E key

When the player presses E, the system SHALL fire a blue homing projectile from the player's position toward the nearest enemy. The projectile SHALL follow a curved trajectory, disappear on contact with the enemy, and deal no damage.

#### Scenario: Player presses E with enemies on the map

- **WHEN** the player presses E
- **AND** there is at least one enemy on the current map
- **THEN** a blue projectile SHALL spawn at the player's center position
- **AND** the projectile SHALL move toward the nearest enemy with a curved trajectory
- **AND** the projectile SHALL disappear when it reaches the enemy
- **AND** the enemy SHALL take no damage

#### Scenario: Player presses E with no enemies on the map

- **WHEN** the player presses E
- **AND** there are no enemies on the current map
- **THEN** no projectile SHALL be created

#### Scenario: Cooldown prevents spam

- **WHEN** the player presses E
- **THEN** a cooldown of 0.5 seconds SHALL start
- **AND** pressing E again within the cooldown period SHALL NOT fire another projectile
