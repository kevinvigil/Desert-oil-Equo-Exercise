package equo.drones.com.model;

import equo.drones.com.exeption.DroneOutsidePlateauBoundariesPosition;

import java.util.List;
import java.util.UUID;

/**
 * Represents a drone that can navigate a plateau based on commands.
 */
public class Drone {
    private UUID id;
    private Position position;
    private Direction direction;
    private Plateau plateau;

    /**
     * Creates a new Drone with initial position and facing direction.
     * @param plateau  The area were it can move.
     * @param position  Initial x-coordinate and y-coordinate.
     * @param direction Initial facing direction (N, E, S, W).
     */
    public Drone(UUID id, Plateau plateau, Position position, Direction direction) {
        if(!plateau.isValid(position)) {
            throw new DroneOutsidePlateauBoundariesPosition("Drone is outside Plateau boundaries");
        }
        this.id = id;
        this.position = position;
        this.direction = direction;
        this.plateau = plateau;
    }

    public UUID getId() { return id; }

    private void left() {
        this.direction = direction.turnLeft();
    }

    private void right() {
        this.direction = direction.turnRight();
    }

    private void advance() {
        switch (this.direction) {
            case Direction.N -> position.increaseY();
            case Direction.E -> position.increaseX();
            case Direction.S -> position.decreaseY();
            case Direction.W -> position.decreaseX();
        }
    }

    public String toString() {
        return position.getX() + " " + position.getY() + " " + direction.toString();
    }

    public Drone move(List<Movement> movements) throws IllegalArgumentException {
        if (movements == null || movements.isEmpty())
            throw new IllegalArgumentException("Plateau and line commands cannot be empty");

        Drone newDrone = new Drone(id, plateau, position, direction);
        movements.forEach(movement -> {
            switch (movement) {
                case R -> newDrone.right();
                case L -> newDrone.left();
                case M -> newDrone.advance();
            }

            if (!plateau.isValid(position)) {
                throw new DroneOutsidePlateauBoundariesPosition("Drone is outside Plateau boundaries position=" + position);
            }
        });
        return newDrone;
    }


    public Position getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public Plateau getPlateau() {
        return plateau;
    }
}
