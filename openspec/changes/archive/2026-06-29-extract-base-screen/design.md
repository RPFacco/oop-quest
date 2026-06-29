## Context

Three Screen classes (MainMenuScreen, GameOverScreen, VictoryScreen) independently create the same camera, viewport, batch, and font in `show()` and `dispose()`. This duplication makes adding new screens tedious and error-prone.

## Goals / Non-Goals

**Goals:**
- Create `BaseScreen` abstract class with common fields and lifecycle
- Three simple screens extend BaseScreen, removing their duplicated setup
- Keep each screen's unique rendering separate (render() body stays in each)

**Non-Goals:**
- No behavioral or visual changes
- GameplayScreen and QuizScreen not refactored (different needs)
- No asset management changes

## Decisions

- **Abstract class over interface** — Screens share state (batch, font) and lifecycle methods (show, dispose), not just behavior. Abstract class is the right fit.
- **Hook methods** — BaseScreen provides `renderBackground()` (clear color) and `renderContent()` (batch drawing) as overridable hooks so subclasses only provide what differs.
- **BaseScreen stores `app`** — All screens need the OopQuest reference. BaseScreen keeps it so subclasses don't redeclare.

## Risks / Trade-offs

- [Low] If a subclass needs different viewport setup, BaseScreen's rigid create/resize might get in the way. Mitigation: make create/resize overridable hooks.
