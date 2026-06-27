## Why

`GameplayScreen` accumulated 7 responsibilities (input, movement, map transitions, NPC proximity, quiz triggers, rendering, HUD). As enemy NPCs will be added next, extracting NPC logic into its own class prevents the screen from becoming unmanageable and establishes a pattern for future systems (e.g., `EnemySystem`).

## What Changes

- Extract `checkNpcProximity()`, NPC rendering, NPC field, and `npcTriggerRect` from `GameplayScreen` into a new `NpcSystem` class
- `NpcSystem` receives NPC list, player reference, quiz map, and an `onQuizTrigger` callback to open `QuizScreen`
- `GameplayScreen` delegates to `NpcSystem.update()` and `NpcSystem.render()` instead of inline code
- No behavioral changes — pure extraction

## Capabilities

### New Capabilities

None — no new capabilities introduced.

### Modified Capabilities

None — no spec-level requirement changes. Pure internal refactor.

## Impact

- **New file:** `core/src/main/java/com/jogoopenspec/game/NpcSystem.java`
- **Modified:** `GameplayScreen.java` — remove NPC fields/methods, add delegation calls
