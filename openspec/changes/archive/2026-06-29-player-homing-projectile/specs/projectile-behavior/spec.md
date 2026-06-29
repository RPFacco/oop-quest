## ADDED Requirements

### Requirement: Projectile behavior is swappable

The system SHALL support a `ProjectileBehavior` interface that defines how a projectile moves each frame. Behaviors SHALL be composable with existing `ShootPattern` (creation side) without modifying `ProjectileEntity` in the `data` package.

#### Scenario: Projectile has no behavior

- **WHEN** a projectile is created with no behavior (null)
- **THEN** it SHALL move in a straight line at constant velocity (existing default behavior)

#### Scenario: Projectile has HomingBehavior

- **WHEN** a projectile has `HomingBehavior` attached
- **THEN** its velocity SHALL gradually rotate toward the nearest enemy each frame
- **AND** the rotation rate SHALL be limited by `turnRate * delta`
- **AND** the projectile SHALL move at its configured speed

### Requirement: ProjectileSystem manages behavior separately

The `ProjectileSystem` SHALL track projectile behavior in an internal entry structure, keeping `ProjectileEntity` free of references to `game` package classes.

#### Scenario: Behavior tracked internally

- **WHEN** a projectile is added to `ProjectileSystem` with a behavior
- **THEN** the system SHALL apply the behavior's `update()` each frame during its own update loop
- **AND** `ProjectileEntity` SHALL have no reference to any `game` package class
