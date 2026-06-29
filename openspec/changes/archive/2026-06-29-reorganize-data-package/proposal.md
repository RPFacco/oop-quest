## Why

The `data/` package mixes entity models with data loaders. Separating into `data/model/` and `data/loader/` improves cohesion, makes the architecture clearer, and follows standard Java package conventions.

## What Changes

- Create `data/model/` subpackage for entity classes
- Create `data/loader/` subpackage for loader classes
- Move 7 entity files → `data/model/`
- Move 4 loader files → `data/loader/`
- Update all imports across the project

## Capabilities

### New Capabilities

- `reorganize-data-package`: Split `data/` into `data/model/` and `data/loader/` subpackages

### Modified Capabilities

None.

## Impact

- 11 files relocated
- 14 files with import updates
- No behavioral changes
