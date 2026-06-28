## Context

The game currently has a peaceful NPC system (`NpcSystem`) for static quiz-givers. Map03 exists but has no entities. This change introduces a moving enemy on map03 using a separate system to avoid mixing concerns.

## Goals / Non-Goals

**Goals:**
- Enemy patrols 3 waypoints in a loop (triangle pattern) on map03
- Enemy renders as a red 32×32 rectangle
- Enemy speed: 240 px/s (75% of player's 320)
- Load enemy data from a new `enemies.json` file

**Non-Goals:**
- Player collision detection or damage
- Enemy-player interaction of any kind
- Multiple enemies on the same map
- Pathfinding or AI beyond waypoint cycling

## Decisions

1. **Separate EnemySystem (not extending NpcSystem)**
   - **Why:** NPCs are static quiz-triggers; enemies are moving entities with waypoints. Different data (waypoints/speed vs quizId), different rendering (red vs orange/gray), different update logic. Mixing them would add conditionals everywhere. A clean separate system is simpler and follows the single-responsibility pattern already established (NpcSystem, Player, etc.).

2. **Waypoint-based movement (same algorithm as Player)**
   - **Why:** The existing `Player.update()` already implements point-to-point linear movement with arrival distance. Reusing the same approach keeps the codebase consistent — enemy moves toward current target waypoint, snaps on arrival, advances to next (with wrap-around).

3. **Static cache loader (same pattern as NpcLoader/QuizLoader)**
   - **Why:** Follows the established convention for data loading. Consistent with all other loaders in the project.

4. **Triangle positioned at center of map03, avoiding spawn area**
   - **Why:** The spawn point from map02 transition is at (928, 64). The triangle is centered at (960, 540) to give the player room to enter the map before encountering the enemy.

## Risks / Trade-offs

- **No collision means the enemy is purely visual** → The enemy moves through the player and walls. This is intentional for now — collision can be added later.
- **Single enemy, single map** → The system is designed to support multiple enemies per map via the same `EnemyEntity` data model, but only one is defined initially.
