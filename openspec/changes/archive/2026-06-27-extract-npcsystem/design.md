## Context

`GameplayScreen.java` handles NPC proximity detection, quiz triggering, and NPC rendering inline (237 lines, 7 responsibilities). As enemy NPCs are planned, NPC logic should live in its own class to keep the screen focused on orchestration.

## Goals / Non-Goals

**Goals:**
- Extract `npcs`, `npcTriggerRect`, `quizzes` fields and `checkNpcProximity()`, NPC rendering into a new `NpcSystem` class
- Keep `NpcSystem` stateless about game state — receives what it needs via parameters
- Use a callback (`NpcTriggerHandler`) to decouple NPC logic from `QuizScreen`
- Zero behavioral change

**Non-Goals:**
- No spec-level requirement changes
- No new features

## Decisions

**1. NpcSystem is a plain class, not a Screen or Component**

No interfaces or inheritance. It has `checkProximity()` and `render()` methods called from `GameplayScreen`'s update and render loop. Aligns with libGDX's simplicity — no ECS overhead.

**2. Callback interface for quiz triggering**

```java
@FunctionalInterface
public interface NpcTriggerHandler {
    void onQuizTrigger(String quizId, QuizData quiz);
}
```

`NpcSystem.checkProximity()` receives a handler. `GameplayScreen` passes a lambda: `(quizId, quiz) -> game.setScreen(new QuizScreen(...))`. This keeps `NpcSystem` unaware of `QuizScreen` or `Game` — testable and reusable.

**3. Player rectangle passed in, not Player reference**

`checkProximity()` takes `Rectangle playerRect` instead of the `Player` object. Keeps coupling minimal — NpcSystem only needs position/size, not Player's full API.

**4. GameState passed explicitly to both checkProximity and render**

No field of `JogoOpenSpec` inside NpcSystem. `GameState` is received as a parameter each frame, reinforcing that NPC rendering depends on current completion state.

## Risks / Trade-offs

- **Risk: Forgetting to pass updated npcs after map load** → `GameplayScreen.loadMap()` already calls `NpcLoader.load().get(mapId)`, just needs to also call `npcSystem.setNpcs(...)`.
- **Risk: Callback coupling** → If more trigger types appear (e.g., enemy combat), the `NpcTriggerHandler` interface can extend or be replaced without touching NpcSystem internals.
