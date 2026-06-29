# projectile-player-damage Specification

## Purpose
TBD - created by archiving change add-projectile-collision-damage. Update Purpose after archive.
## Requirements
### Requirement: Projectile-player collision detection
The system SHALL detect collisions between alive projectiles (circles) and the player (rectangle) using circle-vs-AABB collision.

#### Scenario: Projectile overlaps player
- **WHEN** a projectile's circle overlaps the player's rectangle
- **THEN** the collision SHALL be detected

#### Scenario: Projectile does not overlap player
- **WHEN** a projectile's circle does not overlap the player's rectangle
- **THEN** no collision SHALL be detected

### Requirement: Hit callback on collision
`ProjectileSystem.update()` SHALL accept a `Consumer<ProjectileEntity>` hit callback, invoked for each projectile that collides with the player. The projectile SHALL be removed immediately after callback.

#### Scenario: Hit callback invoked
- **WHEN** a projectile collides with the player
- **THEN** the hit callback SHALL be invoked with that projectile, and the projectile SHALL be removed

### Requirement: Player invincibility frames
The player SHALL have an `invincibleTimer` (seconds). After taking damage, the timer SHALL be set to 1.0. While the timer is above 0, the player SHALL be invulnerable to further hits. The timer SHALL decrease by delta each frame.

#### Scenario: Invincible after hit
- **WHEN** the player is hit and `invincibleTimer` is set to 1.0
- **THEN** subsequent collisions SHALL be ignored until the timer reaches 0

#### Scenario: Timer counts down
- **WHEN** `invincibleTimer` is 1.0 and 0.5 seconds pass
- **THEN** the timer SHALL be 0.5

### Requirement: Blink visual during invincibility
While `invincibleTimer > 0`, the player SHALL alternate visibility at ~5 blinks per second.

#### Scenario: Player blinks
- **WHEN** `invincibleTimer > 0` and `(int)(timer * 10) % 2 == 0`
- **THEN** the player SHALL NOT be rendered

### Requirement: Life loss on hit
When the player is hit and not invincible, `GameState.lives` SHALL be decremented by 1.

#### Scenario: Lives decrement
- **WHEN** a hit occurs and player is not invincible
- **THEN** `gameState.lives` SHALL decrease by 1

### Requirement: Game over on zero lives
When `lives` reaches 0 from a projectile hit, the game SHALL reset state and return to MainMenuScreen.

#### Scenario: Zero lives triggers reset
- **WHEN** `lives ≤ 0` after a hit
- **THEN** `GameState.reset()` SHALL be called and `MainMenuScreen` SHALL be shown

