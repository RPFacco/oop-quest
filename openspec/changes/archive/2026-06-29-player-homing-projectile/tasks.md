## 1. New interfaces and behaviors

- [x] 1.1 Create ProjectileBehavior interface in game/
- [x] 1.2 Create HomingBehavior implementation in game/

## 2. Extended existing classes

- [x] 2.1 Add isEPressed() to InputHandler
- [x] 2.2 Add findNearest(Player) to EnemySystem
- [x] 2.3 Add internal ProjectileEntry and behavior support to ProjectileSystem

## 3. Wire gameplay

- [x] 3.1 Add homing cooldown timer to GameplayScreen
- [x] 3.2 Wire E key → findNearest → fire homing projectile in handleInput()
- [x] 3.3 Render player projectiles in blue

## 4. Verify

- [x] 4.1 Build project with gradlew
