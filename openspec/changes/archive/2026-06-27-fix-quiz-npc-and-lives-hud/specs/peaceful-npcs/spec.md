## MODIFIED Requirements

### Requirement: NPC proximity triggers quiz
When the player's bounding box overlaps an NPC's trigger zone, the system SHALL open the quiz screen for that NPC's quiz. The trigger zone SHALL be larger than the NPC body to give the player room to stop.

#### Scenario: Player walks near an NPC
- **WHEN** the player's rectangle overlaps a trigger rectangle centered on the NPC
- **AND** the trigger rectangle is 2.0x the NPC body size
- **AND** the NPC's quiz has NOT been completed
- **THEN** the game SHALL pause gameplay
- **THEN** the `QuizScreen` SHALL be opened with the NPC's quiz data

#### Scenario: Player walks near a completed NPC
- **WHEN** the player's rectangle overlaps an NPC's trigger zone
- **AND** the NPC's quiz HAS been completed
- **THEN** the game SHALL NOT open the quiz screen

#### Scenario: Player overlaps multiple NPC trigger zones
- **WHEN** the player overlaps more than one NPC's trigger zone
- **THEN** the first NPC in array order SHALL be triggered

## ADDED Requirements

### Requirement: NPC data is cached after first load
The system SHALL load NPC data from `assets/data/npcs.json` only once and cache it for the game session.

#### Scenario: First map load
- **WHEN** a map is loaded for the first time
- **THEN** `npcs.json` SHALL be read from disk and parsed
- **THEN** the parsed data SHALL be cached in memory

#### Scenario: Subsequent map transitions
- **WHEN** the player transitions to a different map
- **THEN** NPC data SHALL be retrieved from the cache
- **THEN** `npcs.json` SHALL NOT be re-read from disk

### Requirement: Missing quiz ID logs an error
When an NPC references a `quizId` that does not exist in the loaded quiz data, the system SHALL log an error via `Gdx.app.log()`.

#### Scenario: NPC with invalid quiz ID approached
- **WHEN** the player walks near an NPC
- **AND** the NPC's `quizId` is not found in the loaded quiz data
- **THEN** the system SHALL log an error message including the NPC position and missing quiz ID
- **THEN** the quiz screen SHALL NOT open
