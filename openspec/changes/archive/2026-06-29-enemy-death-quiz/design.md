## Context

Enemies now have 8 HP and die when it reaches 0, turning gray. But killing them does nothing gameplay-wise. The game has 2 NPC-triggered quizzes and the win condition is completing all quizzes (currently 2). This change adds a third quiz triggered by the first enemy kill and raises the win threshold to 3.

## Goals / Non-Goals

**Goals:**
- Killing an enemy opens quiz 3 (if not already completed)
- Quiz 3 has a question + 4 choices, same format as quizzes 1 and 2
- Win condition: complete all 3 quizzes
- Only triggers once (quiz 3 won't re-open after completion)
- Works with any enemy (first kill triggers it)

**Non-Goals:**
- No changes to NPC quiz flow
- No victory screen (still resets to main menu)
- No changes to how enemies are loaded or spawned

## Decisions

### Decision 1: Quiz 3 content

Add a third quiz about polymorphism:

```json
"3": {
  "question": "What is polymorphism in OOP?",
  "choices": [
    "The ability of an object to take many forms",
    "Hiding internal data from external access",
    "Creating a new class from an existing class",
    "Grouping related variables and methods"
  ],
  "correct": 0
}
```

Polymorphism is the remaining core OOP pillar not yet covered (encapsulation = quiz 1, object creation = quiz 2).

### Decision 2: onEnemyDeath callback in ProjectileSystem

```java
public void update(Player player, float delta, Array<EnemyEntity> enemies,
                   Consumer<ProjectileEntity> onPlayerHit,
                   Consumer<EnemyEntity> onEnemyDeath) {
```

Inside the player projectile collision block, after `e.takeDamage(1)`:

```java
if (!e.isAlive() && onEnemyDeath != null) {
    onEnemyDeath.accept(e);
}
```

This follows the same pattern as the existing `onPlayerHit` callback. No new coupling — `ProjectileSystem` just fires events upward.

Existing callers (none besides GameplayScreen) would need updating, but there's only one caller.

### Decision 3: GameplayScreen handler

```java
private void onEnemyDeath(EnemyEntity e) {
    if (jogoGame.getGameState().isCompleted("3")) return;
    QuizData quiz = QuizLoader.load().get("3");
    if (quiz != null) {
        jogoGame.setScreen(new QuizScreen(jogoGame, this, "3", quiz));
    }
}
```

The `isCompleted("3")` guard ensures the quiz only opens once. After completion, subsequent enemy kills do nothing.

### Decision 4: Win condition unchanged

`QuizScreen` already computes victory as `getCompletedCount() >= QuizLoader.load().size()`. Adding quiz 3 increases `.size()` from 2 to 3, so the win threshold automatically becomes 3. No code change needed.

## Risks / Trade-offs

- **[Race condition]** Multiple projectiles hitting different enemies in one frame — first kill fires the callback, second kill is guarded by `isCompleted`. Fine.
- **[QuizLoader init]** `QuizLoader.load()` uses `Gdx.files.internal()` — safe because GameplayScreen runs within the game loop.
- **[Removed dead enemies]** If enemies are removed before quiz 3 is completed (e.g., map transition), the player would need to kill another enemy to trigger it. Acceptable.
