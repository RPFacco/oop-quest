## Why

MainMenuScreen, GameOverScreen, and VictoryScreen repeat identical boilerplate (camera, viewport, batch, font setup). Extracting a BaseScreen eliminates duplication and makes adding new simple screens trivial.

## What Changes

- Create abstract `BaseScreen` class with common camera/viewport/batch/font lifecycle
- `MainMenuScreen`, `GameOverScreen`, `VictoryScreen` extend `BaseScreen`
- Remove duplicated setup from each screen
- No behavioral changes

## Capabilities

### New Capabilities

- `extract-base-screen`: Extract common Screen boilerplate into a reusable base class

### Modified Capabilities

None.

## Impact

- 1 new file: `BaseScreen.java`
- 3 modified files: `MainMenuScreen.java`, `GameOverScreen.java`, `VictoryScreen.java`
- No API or behavioral changes
