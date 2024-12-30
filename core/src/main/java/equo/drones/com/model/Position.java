package equo.drones.com.model;

/**
 * Represents a drone that can navigate a plateau based on commands.
 */
public class Position {
    private Integer x;
    private Integer y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void increaseX() { x++; }
    public void increaseY() { y++; }
    public void decreaseX() { x--; }
    public void decreaseY() { y--; }


    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
