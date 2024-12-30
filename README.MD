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
This module contains the system's core logic:

**Drone**: Represents the drone with its current position, orientation, and the rules governing its movement.

**Plateau**: Defines the area in which the drone can operate, with clear boundaries that prevent the drone from moving outside the allowed range.

**Movement Logic**: Contains the rules for interpreting commands and updating the drone's position.

### REST API Module
This module implements the user interface through a REST API:

It allows users to interact with the system, such as setting up the plateau and controlling the drone.
It includes input validation and error handling to ensure robust interactions.

### API Endpoints

#### Configure Plateau
- **POST** `/plateau`
- Description: Configures the plateau dimensions.

#### Get Drone Position
- **POST** `/plateau/{plateauId}/drone`
- Description: Place the drone in a specific position on the plateau.

#### Move Drone
- **POST** `/drone/move/{droneId}`
- Description: Moves the drone according to the given commands and initial position.

## Movement Commands

The drone accepts individual commands that determine its movement or orientation change:

- `L`: Turn 90° left.
- `R`: Turn 90° right.
- `M`: Move forward in the current direction.

### Example
If the drone is at position (3, 3, N) and receives the command LMLMLMLMM, it will move and end up at position (3, 4, N).


## Test Input:

- Plateau dimensions `5 5` 


  1. Drone initial position: `1 2 N`

     - Movements: `LMLMLMLMM`
  
     - Drone final position `1 3 N`


  2. Drone initial position `3 3 E`

     - Movements: `MMRMMRMRRM`

     - Drone final position `5 1 E`
