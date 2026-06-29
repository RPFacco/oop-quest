## Context

Entity classes are split across two packages without clear criteria:
- `game/`: `EnemyEntity`, `Player`
- `data/`: `NpcEntity`, `MoveEntity`, `ProjectileEntity`

Player belongs in `game/` (has game logic — movement, invincibility). The others are data holders. EnemyEntity is a data holder with getters/setters, so it belongs in `data/`.

## Goals / Non-Goals

**Goals:**
- Move `EnemyEntity.java` to `data/` package
- Update package declaration and all imports

**Non-Goals:**
- No class renames or refactors
- No behavioral changes

## Decisions

- **Move only EnemyEntity** — Player stays in `game/` because it contains update logic (movement, target following, invincibility). Pure entity/data classes go in `data/`.

## Risks / Trade-offs

- [Low] 10 files need import changes. Compiler will catch any missed updates.
