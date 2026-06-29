## ADDED Requirements

### Requirement: Dead code is removed
The codebase SHALL contain no unreferenced source files from prior refactoring rounds.

#### Scenario: HudRenderer is removed
- **WHEN** the project builds
- **THEN** `HudRenderer.java` SHALL NOT exist in the source tree

### Requirement: WaypointMovement reads startWaypoint from JSON config
WaypointMovement SHALL read the `startWaypoint` field from the JSON config if present, defaulting to 0 otherwise.

#### Scenario: startWaypoint is configured
- **WHEN** an enemy JSON config includes `"startWaypoint": 2`
- **THEN** the enemy SHALL begin following waypoints starting at index 2

#### Scenario: startWaypoint is absent
- **WHEN** an enemy JSON config omits `startWaypoint`
- **THEN** the enemy SHALL begin following waypoints starting at index 0

### Requirement: GameState reset does not clear enemy cache
`GameState.reset()` SHALL NOT call `EnemyLoader.clearCache()`. A new `OopQuest.resetGame()` method SHALL coordinate both state reset and cache clearing.

#### Scenario: reset during gameplay
- **WHEN** the player dies and the game resets
- **THEN** `GameState.reset()` runs without clearing the enemy JSON cache
- **AND** `OopQuest.resetGame()` calls both `GameState.reset()` and `EnemyLoader.clearCache()`

### Requirement: QuizScreen uses BaseScreen
QuizScreen SHALL extend `BaseScreen` instead of `ScreenAdapter`, eliminating duplicated camera/viewport/batch/font lifecycle.

#### Scenario: QuizScreen renders
- **WHEN** the quiz screen is shown
- **THEN** it SHALL use the camera, viewport, batch, and font initialized by `BaseScreen`

### Requirement: Data layer does not import from game layer
No class in the `data/` package hierarchy SHALL import from the `game/` package hierarchy. All cross-layer dependencies SHALL flow from `game/` to `data/`.

#### Scenario: EnemyEntity has no game imports
- **WHEN** inspecting `EnemyEntity.java` imports
- **THEN** no import SHALL reference `com.rpfacco.oopquest.game`

#### Scenario: Player has no game imports
- **WHEN** inspecting `Player.java` imports after moving to `data/model/`
- **THEN** no import SHALL reference `com.rpfacco.oopquest.game`

### Requirement: Game layer uses factory for strategy instantiation
`EnemyLoader` SHALL use a strategy factory to instantiate movement and shoot patterns, rather than importing concrete strategy classes directly.

#### Scenario: EnemyLoader creates movement
- **WHEN** `EnemyLoader` reads an enemy with `"movement": { "type": "waypoint" }`
- **THEN** it SHALL obtain the `MovementStrategy` instance via `StrategyFactory`, not by importing `WaypointMovement` directly

#### Scenario: EnemyLoader creates shoot pattern
- **WHEN** `EnemyLoader` reads an enemy with `"shoot": { "type": "burst" }`
- **THEN** it SHALL obtain the `ShootPattern` instance via `StrategyFactory`, not by importing `BurstPattern` directly

### Requirement: All entity classes are in data/model/
`Player` SHALL be located in `com.rpfacco.oopquest.game.data.model` alongside `EnemyEntity` and `ProjectileEntity`.

#### Scenario: Player package
- **WHEN** inspecting `Player.java`
- **THEN** its package declaration SHALL be `com.rpfacco.oopquest.game.data.model`

### Requirement: ProjectileSystem.update() has extracted methods
The `ProjectileSystem.update()` method SHALL delegate collision logic to extracted private methods for readability.

#### Scenario: update delegates collisions
- **WHEN** reading `ProjectileSystem.update()`
- **THEN** it SHALL call extracted helper methods for enemy collision, player collision, and bounds checking

### Requirement: Enemy has configurable quiz ID
An `EnemyEntity` SHALL have an optional `String quizId` field read from JSON. `GameplayScreen.onEnemyDeath` SHALL use `e.getQuizId()` instead of the hardcoded string `"3"`.

#### Scenario: quizId is configured
- **WHEN** an enemy dies and its JSON config includes `"quizId": "3"`
- **THEN** `GameplayScreen.onEnemyDeath` SHALL retrieve `"3"` via `e.getQuizId()`

#### Scenario: quizId is absent
- **WHEN** an enemy dies and its JSON config omits `quizId`
- **THEN** `e.getQuizId()` SHALL return `null`
- **AND** no quiz SHALL be triggered

#### Scenario: enemies.json includes quizId for first enemy
- **WHEN** reading `assets/data/enemies.json`
- **THEN** the enemy on `map03` SHALL include `"quizId": "3"`
