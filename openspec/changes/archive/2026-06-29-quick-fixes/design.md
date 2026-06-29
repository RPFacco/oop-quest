## Context

Three isolated issues in the codebase: a missing null-safety check that differs from an adjacent loader's pattern, a one-line bug in waypoint initialization, and a hardcoded magic number.

## Goals / Non-Goals

**Goals:**
- Match MapLoader's robustness to EnemyLoader's existing pattern
- Fix waypoint start index to begin at the first waypoint
- Extract starting lives into a named constant in GameConfig

**Non-Goals:**
- No structural/architectural changes
- No new files or abstractions
- No behavior changes beyond the three fixes

## Decisions

No significant design decisions — each fix follows existing patterns in the codebase:

- **MapLoader null check**: mirror the `file.exists()` guard already used in `EnemyLoader.load()` (line 25)
- **Waypoint start**: change `Math.min(1, ...)` to `0` since the first waypoint is always at index 0
- **Lives constant**: add `public static final int LIVES = 5` to `GameConfig`, reference from `GameState`

## Risks / Trade-offs

None — these are low-risk, localized fixes.
