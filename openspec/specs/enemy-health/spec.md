# enemy-health Specification

## Purpose

Enemies have 8 health points, display an always-visible health bar, and die (stop moving, turn gray) when HP reaches 0.

## Requirements

### Requirement: Enemy HP

Each enemy SHALL have 8 HP at creation.

#### Scenario: Default HP on creation

- **WHEN** an `EnemyEntity` is instantiated
- **THEN** its HP SHALL be 8
- **AND** its max HP SHALL be 8
- **AND** it SHALL be alive

#### Scenario: Damage reduces HP

- **WHEN** `takeDamage(1)` is called on an enemy with 8 HP
- **THEN** its HP SHALL be 7
- **AND** it SHALL remain alive

#### Scenario: Death at 0 HP

- **WHEN** `takeDamage(1)` is called on an enemy with 1 HP
- **THEN** its HP SHALL be 0
- **AND** it SHALL NOT be alive

### Requirement: Player projectile deals 1 damage

Player homing projectiles SHALL deal 1 damage on collision with an enemy.

#### Scenario: Projectile hits enemy

- **WHEN** a player homing projectile collides with an enemy
- **THEN** the enemy SHALL lose 1 HP
- **AND** the projectile SHALL be destroyed

### Requirement: Death behavior

Dead enemies SHALL stop moving and stop shooting. They SHALL remain in the enemy list and be rendered gray.

#### Scenario: Dead enemy does not move

- **WHEN** an enemy's HP reaches 0
- **THEN** its movement strategy SHALL NOT be executed on subsequent updates

#### Scenario: Dead enemy does not shoot

- **WHEN** an enemy's HP reaches 0
- **THEN** its shoot pattern SHALL NOT generate projectiles

#### Scenario: Dead enemy rendered gray

- **WHEN** an enemy is not alive
- **THEN** it SHALL be rendered as a gray rectangle (RGB 0.5, 0.5, 0.5)

### Requirement: Health bar

Each alive enemy SHALL display a green health bar below its body, proportional to remaining HP.

#### Scenario: Full HP bar

- **WHEN** an enemy has 8 HP
- **THEN** a green bar SHALL be drawn below the enemy
- **AND** the bar width SHALL equal the enemy's width

#### Scenario: Partial HP bar

- **WHEN** an enemy has 4 HP out of 8
- **THEN** the green bar SHALL be half the enemy's width

#### Scenario: Dead enemy has no bar

- **WHEN** an enemy is not alive
- **THEN** no health bar SHALL be drawn below it

### Requirement: Homing ignores dead enemies

Player homing projectiles SHALL NOT target dead enemies.

#### Scenario: No homing toward corpse

- **WHEN** the player presses E
- **THEN** the nearest ALIVE enemy SHALL be selected as target
- **AND** a dead enemy SHALL NOT be selected
