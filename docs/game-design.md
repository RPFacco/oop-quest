# Game Design — OopQuest

## Concept

A 2D top-down educational game in which the player explores interconnected maps, defeats enemies, and interacts with peaceful NPCs to answer quizzes about Object-Oriented Programming. The goal is to answer all available quizzes in the game.

---

## Core Mechanics

### Movement
The player moves by clicking a destination on the map (mouse-driven movement). The player navigates through a top-down view. The player is clamped to the map bounds at all times.

### Lives
The player starts with **5 lives**. Lives are lost by:
- A wrong answer in a quiz
- Damage dealt by enemies

When lives reach zero, the **Game Over** screen is displayed.

### Combat
The player attacks by pressing the **E** key, which fires a **homing projectile** toward the nearest enemy.

Enemies have the following variation parameters (defined individually per enemy):
- **Attack speed**: how frequently the enemy attacks
- **Attack pattern**: projectile or melee
- **HP (hit points)**: enemies can be weak (low HP) or strong (high HP)

Defeating an enemy triggers the quiz linked to it.

### Quizzes
Each quiz has one question and four answer choices. The player **must answer correctly** to exit the quiz screen. Wrong answers cost lives, but the quiz remains active until answered correctly or the game ends via Game Over.

The quiz screen is a **separate static screen** (not an overlay or popup). The game world is fully paused while the quiz is displayed.

Quizzes are triggered in two ways:
- **Peaceful NPC:** triggers the quiz automatically upon proximity
- **Enemy NPC:** triggers the quiz linked to it upon being defeated

Each NPC has a specific, fixed quiz. The quiz bank is stored in `assets/data/quizzes.json`, indexed by `quiz_id`:

```json
{
  "1": {
    "question": "What is encapsulation in OOP?",
    "choices": [
      "Hiding internal details and exposing an interface",
      "Creating multiple instances of a class",
      "Inheriting behavior from a parent class",
      "Overriding a method from the superclass"
    ],
    "correct": 0
  }
}
```

- `correct` is the 0-based index into the `choices` array.

---

## NPCs

| Type | Behavior | Quiz trigger |
|---|---|---|
| Peaceful | Static or patrolling. Does not attack. | Player proximity |
| Enemy | Varied attacks (defined per NPC). | Upon being defeated by the player |

---

## Maps

Each map fits the **screen size** — the camera does not follow the player. Movement is confined to the visible area and the player is clamped to the map bounds.

Maps may contain **internal obstacles** with collision hitboxes (e.g. trees, walls, furniture).

Maps may also contain **move entities** — colored rectangles visible on the map. When the player overlaps with a move entity, they are teleported to the target map at a defined spawn position. The spawn position is placed away from any move entities on the destination map to prevent immediate re-triggering.

The architecture is **data-driven**: adding a new map requires no changes to any `.java` file. The process is:
1. Create `assets/maps/new-map.tmx` in Tiled with layout, NPCs, and custom properties (`quiz_id` per NPC)
2. Register the map and its move entities in `assets/data/maps.json`

Move entities are the **only** way to transition between maps — borders are always dead ends.

Schema for `maps.json`:

```json
{
  "startMap": "map01",
  "maps": {
    "map01": {
      "file": "maps/map01.tmx",
      "moveEntities": [
        {
          "x": 300, "y": 200,
          "width": 32, "height": 80,
          "targetMap": "map02",
          "spawnX": 40, "spawnY": 40
        }
      ]
    },
    "map02": {
      "file": "maps/map02.tmx",
      "moveEntities": [
        {
          "x": 300, "y": 60,
          "width": 32, "height": 80,
          "targetMap": "map03",
          "spawnX": 40, "spawnY": 40
        },
        {
          "x": 300, "y": 380,
          "width": 32, "height": 80,
          "targetMap": "map01",
          "spawnX": 40, "spawnY": 40
        }
      ]
    }
  }
}
```

- `startMap`: which map the player loads when the game starts
- `maps`: dictionary indexed by map ID
- `file`: path relative to the `assets/` directory
- `moveEntities`: array of transition zones, each with:
  - `x`, `y`: top-left position on the map (pixels)
  - `width`, `height`: size of the transition zone (pixels)
  - `targetMap`: the map ID to transition to
  - `spawnX`, `spawnY`: position where the player appears on the target map

### Asset structure

```
assets/
├── maps/
│   ├── map01.tmx        # visual layout + NPC positions with quiz_id
│   └── map02.tmx
└── data/
    ├── maps.json        # map list and directional connections between maps
    └── quizzes.json     # quiz bank indexed by quiz_id
```

---

## Screens and Flow

```
Main Menu
    │
    └─▶ Gameplay (current map)
            │
            ├─▶ Quiz Screen
            │       │
            │       └─▶ Gameplay (on correct answer)
            │
            ├─▶ Game Over (lives = 0)
            │       │
            │       └─▶ Main Menu (restart)
            │
            └─▶ Game Completed (all quizzes answered)
```

---

## Initial Scope (v1)

For the first implementable slice:

| Aspect | Value |
|---|---|
| Maps | 3 hand-crafted maps |
| Quizzes | 3 fixed quizzes |
| Map 1 | Peaceful NPC quiz (triggered on proximity) |
| Map 2 | Peaceful NPC quiz (triggered on proximity) |
| Map 3 | Enemy NPC (quiz appears upon defeating it) |
| Art | Colored rectangles as placeholders for all sprites and tiles |

---

## Out of Scope

- Maps and quizzes are hand-crafted data files — no procedural generation
