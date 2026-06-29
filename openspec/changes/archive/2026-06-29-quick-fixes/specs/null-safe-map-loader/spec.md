## ADDED Requirements

### Requirement: MapLoader handles missing file gracefully

MapLoader SHALL check if the maps.json file exists before attempting to parse it.
If the file does not exist, it SHALL return an empty MapData with no maps.

#### Scenario: maps.json is missing

- **WHEN** MapLoader.load() is called and data/maps.json does not exist
- **THEN** it SHALL return a MapData instance with no maps
- **AND** no exception SHALL be thrown
