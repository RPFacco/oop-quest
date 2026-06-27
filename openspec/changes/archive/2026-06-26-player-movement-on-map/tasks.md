## 1. Asset Creation

- [x] 1.1 Create `assets/data/maps.json` with a single map entry (`map01` as `startMap`, no connections)
- [x] 1.2 Create a minimal tileset PNG (`assets/tileset.png`) — a 32x32 colored square
- [x] 1.3 Create `assets/maps/map01.tmx` — an XML Tiled map file referencing the tileset, with a single tile layer of 20x15 tiles

## 2. Data Structures and Loading

- [x] 2.1 Create data POJO classes (`MapData`, `MapEntry`, `MapConnections`) in package `com.jogoopenspec.game.data` with public fields for libGDX `Json` deserialization
- [x] 2.2 Create `MapLoader` class that reads `assets/data/maps.json`, parses it into `MapData` objects, and provides access to the start map ID

## 3. Main Menu Screen

- [x] 3.1 Create `MainMenuScreen` implementing `Screen` — renders a centered title text and "Click to Start" prompt
- [x] 3.2 On click/keypress, transition to `GameplayScreen` via `Game.setScreen()`

## 4. Gameplay Screen

- [x] 4.1 Create `Player` class with position (`x`, `y`), size (`width`, `height`), speed, target position, and an `update(delta)` method that moves toward the target
- [x] 4.2 Create `GameplayScreen` implementing `Screen` — loads the `.tmx` map via `TmxMapLoader`, renders it with `OrthogonalTiledMapRenderer`, creates the `Player`, and handles click-to-move input
- [x] 4.3 Clamp player position to map bounds (0 to mapWidthInPixels, 0 to mapHeightInPixels)
- [x] 4.4 Render the player as a colored rectangle on top of the map

## 5. Wire and Cleanup

- [x] 5.1 Update `JogoOpenSpec.create()` to set `MainMenuScreen` as the initial screen
- [x] 5.2 Remove the template `FirstScreen.java` file
- [x] 5.3 Verify the game compiles and runs: player sees the main menu, clicks, sees the map, and can click to move the player rectangle
