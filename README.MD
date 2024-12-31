# Drone Navigation System

### Project: Desert Oil Exploration Drones

#### Company: Equo

This project simulates a drone navigating within a defined plateau, allowing 
users to interact with the system through HTTP requests. Users can configure 
the boundaries of the plateau and issue commands for the drone to move and 
operate within the specified area.

## Modules

The solution is divided into two modules to maintain clarity and good organization:

### Core Module
This module resolved the base exercise modeling challenges in plain Java.

This module contains the system's core logic:

**Drone**: Represents the drone with its current position, orientation, and the rules governing its movement.

**Plateau**: Defines the area in which the drone can operate, with clear boundaries that prevent the drone from moving outside the allowed range.

**Movement Logic**: Contains the rules for interpreting commands and updating the drone's position.

### REST API Module
This module implements the user interface through a REST API in order to solve the extension exercise challenges:

It allows users to interact with the system, such as setting up the plateau and controlling the drone.
It includes input validation and error handling to ensure robust interactions.

### API Endpoints

#### Configure Plateau

Configures the plateau dimensions.
```
POST {HOST}/plateau

Request:
{maxX} {maxY}

Response:
{PlateauId}

Example:

curl --location --request POST '{HOST}/plateau' \
--header 'Content-Type: text/plain' \
--data-raw '5 5'
``` 

#### Get Drone Position

Place drone in specific position on the plateau.
```
POST {HOST}/plateau/{plateauId}/drone

Request: 
{X} {Y} {Direction}

Response:
{DroneID}

Example:

curl --location --request POST '{HOST}/plateau/{plateauId}/drone' \
--header 'Content-Type: text/plain' \
--data-raw '1 2 N'
```

#### Move Drone

Moves the drone according to the given commands and initial position.
```
POST {HOST}/drone/{droneId}/move

Request:
{List of movements}

Response:
{Final position}

Example:

curl --location --request POST '{HOST}/drone/{droneId}/move' \
--header 'Content-Type: text/plain' \
--data-raw 'LMLMLMLMM'
```

## Movement Commands

The drone accepts individual commands that determine its movement or orientation change:

- `L`: Turn 90° left.
- `R`: Turn 90° right.
- `M`: Move forward in the current direction.

[//]: # (## Test Input:)

[//]: # ()
[//]: # ()
[//]: # (- Plateau dimensions `5 5` )

[//]: # ()
[//]: # ()
[//]: # (  1. Drone initial position: `1 2 N`)

[//]: # ()
[//]: # ()
[//]: # (     - Movements: `LMLMLMLMM`)

[//]: # ()
[//]: # (  )
[//]: # (     - Drone final position `1 3 N`)

[//]: # ()
[//]: # ()
[//]: # ()
[//]: # (  2. Drone initial position `3 3 E`)

[//]: # ()
[//]: # ()
[//]: # (     - Movements: `MMRMMRMRRM`)

[//]: # ()
[//]: # ()
[//]: # (     - Drone final position `5 1 E`)
