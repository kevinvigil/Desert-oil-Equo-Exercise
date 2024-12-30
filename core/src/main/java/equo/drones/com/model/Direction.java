package equo.drones.com.model;

public enum Direction {
    N, E, S, W;

    // If a new values is added to the enum,
    // it will fail on compilation time because there is missing value in the switch
    // This makes it safer for future changes.
    public Direction turnRight() {
        return switch (this) {
            case N -> E;
            case E -> S;
            case S -> W;
            case W -> N;
        };
    }

    public Direction turnLeft() {
        return switch (this) {
            case N -> W;
            case E -> N;
            case S -> E;
            case W -> S;
        };
    }
}
