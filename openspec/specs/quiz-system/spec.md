## ADDED Requirements

### Requirement: Quizzes are loaded from quizzes.json
The system SHALL load quiz data from `assets/data/quizzes.json`, indexed by quiz ID.

#### Scenario: Quizzes parse successfully
- **WHEN** the game starts or a quiz is triggered
- **THEN** quizzes.json SHALL be parsed into `QuizData` objects with `question`, `choices` (string array of 4), and `correct` (0-based index)

#### Scenario: Quiz ID not found
- **WHEN** an NPC references a `quizId` that does not exist in quizzes.json
- **THEN** the system SHALL log an error and SHALL NOT open the quiz screen

### Requirement: Quiz screen displays question and choices
The `QuizScreen` SHALL display the quiz question and four clickable answer choices. The game world SHALL be fully paused while the quiz is visible.

#### Scenario: Quiz screen opens
- **WHEN** a quiz is triggered
- **THEN** the game SHALL set the active screen to `QuizScreen`
- **THEN** the quiz question SHALL be displayed as text
- **THEN** four answer choices SHALL be displayed as clickable text areas
- **THEN** gameplay SHALL be paused

### Requirement: Correct answer exits quiz screen
When the player clicks the correct answer, the system SHALL return to the gameplay screen and mark the quiz as completed.

#### Scenario: Player answers correctly
- **WHEN** the player clicks the correct choice
- **THEN** the quiz SHALL be marked as completed in game state
- **THEN** the game SHALL return to `GameplayScreen` with the game world unchanged

### Requirement: Wrong answer costs a life
When the player clicks a wrong answer, the system SHALL decrement lives by 1 and keep the quiz screen open. If lives reach 0, the system SHALL dispose of quiz screen resources before transitioning to the main menu.

#### Scenario: Player answers incorrectly
- **WHEN** the player clicks a wrong choice
- **THEN** the player SHALL lose 1 life
- **THEN** the quiz screen SHALL remain open
- **THEN** the player SHALL be able to try again

#### Scenario: Lives reach zero during quiz
- **WHEN** the player answers incorrectly
- **AND** lives reach 0
- **THEN** the quiz screen SHALL dispose its resources (font, batch, shape renderer)
- **THEN** the game SHALL transition to the main menu

### Requirement: Question text is measured without double rendering
The quiz screen SHALL measure the question text width using `GlyphLayout` before drawing, avoiding a visible render at x=0.

#### Scenario: Question text displayed
- **WHEN** the quiz screen opens
- **THEN** the question text SHALL be measured without drawing at an incorrect position
- **THEN** the text SHALL be drawn once at the correct centered position
