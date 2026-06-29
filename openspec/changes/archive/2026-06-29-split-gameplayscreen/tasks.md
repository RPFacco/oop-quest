## 1. Create WorldRenderer

- [x] 1.1 Create `WorldRenderer` class with `ShapeRenderer` ownership
- [x] 1.2 Move TiledMap rendering into `renderMap(OrthographicCamera, MapManager)` method
- [x] 1.3 Move batch HUD rendering into `renderHud(SpriteBatch, GameState)` method
- [x] 1.4 Move shape rendering (move entities, NPCs, enemies, health bars, projectiles, player) into `renderEntities(ShapeRenderer, ...)` method
- [x] 1.5 Expose `dispose()` to clean up ShapeRenderer

## 2. Create MapManager

- [x] 2.1 Create `MapManager` class holding `TiledMap`, `OrthogonalTiledMapRenderer`, `MapData`, `currentMapId`
- [x] 2.2 Implement `loadMap(String mapId, NpcSystem, EnemySystem, ProjectileSystem)` with null-safe disposal
- [x] 2.3 Implement `transitionTo(MoveEntity me, Player player)` calling loadMap + reposition
- [x] 2.4 Expose `getTiledMap()`, `getMapRenderer()`, `getCurrentMapId()`, `getMoveEntities()`, `dispose()`

## 3. Create InputController

- [x] 3.1 Create `InputController` class with `InputHandler`, player, cooldown refs
- [x] 3.2 Implement `handleInput()` returning `InputResult` record (escape, homing, touchTarget)
- [x] 3.3 Move homing projectile creation logic from current `handleInput()` into InputController
- [x] 3.4 InputController receives `ProjectileSystem` and `EnemySystem` refs for homing creation

## 4. Move clampPlayerToBounds into Player

- [x] 4.1 Add `clampToBounds()` method to `Player` class

## 5. Convert GameplayScreen to extend BaseScreen

- [x] 5.1 Change `GameplayScreen` to `extends BaseScreen`
- [x] 5.2 Remove `camera`, `viewport`, `batch`, `font` field declarations
- [x] 5.3 Remove camera/viewport/batch/font creation from `show()` (keep `super.show()`)
- [x] 5.4 Remove camera/viewport/batch/font disposal from `dispose()`
- [x] 5.5 Keep `font.getData().setScale(2)` after `super.show()`
- [x] 5.6 Remove unused `import com.rpfacco.oopquest.game.OopQuest`

## 6. Rewire GameplayScreen

- [x] 6.1 Instantiate `WorldRenderer`, `MapManager`, `InputController` in `show()`
- [x] 6.2 Replace inline rendering with `worldRenderer.render(...)` calls
- [x] 6.3 Replace inline map logic with `mapManager.loadMap(...)` / `mapManager.transitionTo(...)` calls
- [x] 6.4 Replace inline input logic with `inputController.handleInput()` + result dispatch
- [x] 6.5 Replace `clampPlayerToBounds()` with `player.clampToBounds()`
- [x] 6.7 Remove `resize()` — inherited from BaseScreen
- [x] 6.8 Ensure all game rule callbacks still fire correctly

## 7. Verify

- [x] 7.1 Build project with `gradlew :core:compileJava`
- [x] 7.2 Run project and verify gameplay behavior is unchanged
