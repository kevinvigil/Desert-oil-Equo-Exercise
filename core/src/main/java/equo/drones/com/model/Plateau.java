package equo.drones.com.model;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public record Plateau (
        UUID id,
        Integer minX,
        Integer maxX,
        Integer minY,
        Integer maxY
) {
    public static Plateau newPlateauWithZeroMinCoords(Integer maxX, Integer maxY){
        return new Plateau(randomUUID(),0, maxX, 0, maxY);
    }

    @Override
    public String toString(){
        return String.format("%d %d", maxX, maxY);
    }

    public Boolean isValid(Position position) {
        return position.getX() >= minX &&
                position.getY() >= minY &&
                position.getX() <= maxX &&
                position.getY() <= maxY;
    }
}
