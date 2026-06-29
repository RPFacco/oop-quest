## ADDED Requirements

### Requirement: BaseScreen provides common Screen infrastructure
The project SHALL have an abstract `BaseScreen` class that centralizes camera, viewport, batch, and font lifecycle.

#### Scenario: BaseScreen creates camera and viewport
- **WHEN** a BaseScreen subclass is shown
- **THEN** an OrthographicCamera and FitViewport SHALL be created automatically
- **AND** the camera SHALL be positioned at the center of the game world

#### Scenario: BaseScreen creates batch and font
- **WHEN** a BaseScreen subclass is shown
- **THEN** a SpriteBatch and BitmapFont SHALL be created automatically

#### Scenario: Subclass can override hooks
- **WHEN** a BaseScreen subclass implements renderBackground or renderContent hooks
- **THEN** BaseScreen SHALL call those hooks in the correct order

### Requirement: Simple screens use BaseScreen
MainMenuScreen, GameOverScreen, and VictoryScreen SHALL extend BaseScreen.

#### Scenario: No duplicated setup
- **WHEN** inspecting MainMenuScreen, GameOverScreen, or VictoryScreen
- **THEN** they SHALL NOT contain camera, viewport, batch, or font field declarations
- **AND** they SHALL NOT contain camera/viewport/batch/font setup in show()
- **AND** they SHALL NOT contain camera/viewport/batch/font disposal

#### Scenario: Visual behavior unchanged
- **WHEN** each screen renders
- **THEN** its visual output SHALL be identical to before the refactoring
