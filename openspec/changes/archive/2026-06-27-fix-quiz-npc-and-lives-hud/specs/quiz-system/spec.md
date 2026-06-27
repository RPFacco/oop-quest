## MODIFIED Requirements

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

## ADDED Requirements

### Requirement: Question text is measured without double rendering
The quiz screen SHALL measure the question text width using `GlyphLayout` before drawing, avoiding a visible render at x=0.

#### Scenario: Question text displayed
- **WHEN** the quiz screen opens
- **THEN** the question text SHALL be measured without drawing at an incorrect position
- **THEN** the text SHALL be drawn once at the correct centered position
