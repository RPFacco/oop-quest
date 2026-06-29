## Context

Six code quality issues accumulated across previous changes. The `game/` package imports `data/model/` types, but `data/model/` also imports `game/` (EnemyEntity imports MovementStrategy and ShootPattern). This circular-ish dependency makes the data layer depend on game-specific interfaces. HudRenderer was orphaned when WorldRenderer took over HUD rendering. QuizScreen duplicates BaseScreen's lifecycle. GameState.reset() clears the enemy cache which is a side effect that should be coordinated externally. And the quiz ID is hardcoded in GameplayScreen. This change addresses all of these in one pass.

The codebase uses libGDX with package structure `com.rpfacco.oopquest.game.data.model` for entities and `com.rpfacco.oopquest.game` for systems/screens.

## Goals / Non-Goals

**Goals:**
- Eliminate dead code (HudRenderer)
- Remove cross-package dependency from data layer to game layer
- Consolidate all entity classes in data/model/
- Reduce duplicated lifecycle code (QuizScreen → BaseScreen)
- Replace hardcoded quiz ID with configurable per-enemy value
- Split monolithic ProjectileSystem.update() for readability
- Fix WaypointMovement startWaypoint parsing

**Non-Goals:**
- No behavioral or gameplay changes
- No new features
- No UI/UX changes
- No new dependencies

## Decisions

### 1. MovementStrategy and ShootPattern move to data/model/, not a new package
**Decision**: Move the interfaces to `com.rpfacco.oopquest.game.data.model`.
**Rationale**: These are simple strategy interfaces (2-3 methods each). A `data/strategy/` or `game/strategy/` subpackage adds a new package for only 2 interfaces + 1 factory. Putting them in `model/` keeps things flat and is practical for a small project.
**Alternative considered**: `data/behavior/` subpackage — cleaner separation but excessive for two interfaces.
**Alternative considered**: Leave in `game/` and accept the dependency — simplest but violates dependency direction rule.

### 2. StrategyFactory in game/, not data/
**Decision**: Place `StrategyFactory` in `com.rpfacco.oopquest.game` alongside EnemyLoader.
**Rationale**: The factory imports concrete strategy classes (WaypointMovement, BurstPattern). Moving it to `data/` would re-introduce the same cross-package dependency. The factory belongs where the concrete classes live.
**Alternative considered**: Factory in data/ — would drag concrete imports from game/ back to data/.

### 3. Player moves to existing data/model/ package
**Decision**: Move `Player.java` from `com.rpfacco.oopquest.game` to `com.rpfacco.oopquest.game.data.model`.
**Rationale**: All entity classes (EnemyEntity, ProjectileEntity) are already there. Player is a first-class entity. Add import to 8 referencing files.
**Alternative considered**: Move enemies/projectiles to game/ — larger blast radius, inconsistent with existing structure.

### 4. ProjectileSystem.update() retains its signature
**Decision**: Keep the public signature unchanged. Extract private helper methods for collision checks.
**Rationale**: The method is called from GameplayScreen.update() with a specific lambda signature. Changing it breaks the caller. Extraction can be purely internal.
**Methods extracted**: `checkEnemyCollision`, `checkPlayerCollision`, `checkBounds`.

### 5. quizId is optional on EnemyEntity
**Decision**: `EnemyEntity.quizId` defaults to `null`. Only triggers quiz when non-null.
**Rationale**: Backward compatibility with existing enemy configs that lack the field. No breaking change.

## Risks / Trade-offs

- **Risk**: Moving Player breaks compilation in 8 files → Mitigation: Mechanical import update, straightforward
- **Risk**: FactoryDispatcher introduces runtime error on unknown type → Mitigation: Enum or string switch with IllegalArgumentException for unrecognized types
- **Risk**: QuizScreen forgets to call super.show() → Mitigation: BaseScreen.show() is abstract with empty body; QuizScreen.show() calls super.show() then sets font scale
- **Risk**: enemies.json format changes — not backward compatible if existing tools parse it → Mitigation: quizId is optional, existing files work unchanged
