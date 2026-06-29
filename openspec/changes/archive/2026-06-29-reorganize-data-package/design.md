## Context

Current `data/` layout mixes concerns:

```
data/
├── MoveEntity.java       ← model (state holder)
├── NpcEntity.java        ← model
├── ProjectileEntity.java ← model
├── QuizData.java         ← model
├── MapData.java          ← model
├── MapEntry.java         ← model
├── EnemyEntity.java      ← model
├── MapLoader.java        ← loader (data access)
├── NpcLoader.java        ← loader
├── QuizLoader.java       ← loader
└── EnemyLoader.java      ← loader
```

## Goals / Non-Goals

**Goals:**
- Create `data/model/` with 7 entity/data classes
- Create `data/loader/` with 4 loader classes
- Update all imports across 14 files

**Non-Goals:**
- No class renames or refactors
- No behavioral changes

## Decisions

- **`model` and `loader` names** — Standard Java naming. `model` for domain objects, `loader` for data access. Other options considered: `entity`/`repository` (too heavy for this scale), `dto`/`dao` (overly specific).

## Risks / Trade-offs

- [Low] Compiler catches any missed imports.
