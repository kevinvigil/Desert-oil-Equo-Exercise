package equo.drones.com;

import equo.drones.com.exeption.DroneOutsidePlateauBoundariesPosition;
import equo.drones.com.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static equo.drones.com.model.Direction.N;
import static equo.drones.com.model.Plateau.newPlateauWithZeroMinCoords;
import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class CommandProcessorTest {
    private static final Plateau PLATEAU = newPlateauWithZeroMinCoords(5, 5);

    public static Stream<Arguments> exampleInputSingleExecution() {
        return Stream.of(
                arguments("5 5\n1 2 N\nLMLMLMLMM", "1 3 N"),
                arguments("5 5\n3 3 E\nMMRMMRMRRM", "5 1 E")
        );
    }

    @ParameterizedTest
    @MethodSource("exampleInputSingleExecution")
    public void shouldReachExpectedCoordinatesAndHeadingWhenSingleExecution(String input, String expectedOutput) {
        String[] lines = input.split("\n");
        Plateau plateau = newPlateauWithZeroMinCoords(parseInt(lines[0].split(" ")[0]), parseInt(lines[0].split(" ")[1]));
        Drone drone = new Drone(UUID.randomUUID(), plateau, new Position(parseInt(lines[1].split(" ")[0]), parseInt(lines[1].split(" ")[1])), Direction.valueOf(lines[1].split(" ")[2]));
        List<Movement> movements = lines[2].chars().mapToObj(operand -> Movement.valueOf(String.valueOf((char) operand))).toList();

        // When
        Drone result = drone.move(movements);

        // Then
        assertEquals(expectedOutput, result.toString());
    }

    /**
     * Test the possibility of illegal move aut of range.
     */
    @Test
    public void shouldThrowExceptionWhenTheDroneMoveForwardTheBoundariesPosition() {
        Drone drone = new Drone(UUID.randomUUID(), PLATEAU, new Position(1,1), N);
        List<Movement> movements = "MMMMMMM".chars().mapToObj(m -> Movement.valueOf(String.valueOf((char) m))).toList();
        assertThrows(DroneOutsidePlateauBoundariesPosition.class, () -> drone.move(movements));
    }

    public static Stream<Arguments> exampleInputMultipleExecution() {
        return Stream.of(
                arguments("5 5\n1 2 N\nLMLMLMLMM\n3 3 E\nMMRMMRMRRM", "1 3 N\n5 1 E")
        );
    }

    @ParameterizedTest
    @MethodSource("exampleInputMultipleExecution")
    public void shouldReachExpectedCoordinatesAndHeadingWhenMultipleExecution(String input, String expectedOutputs) {
        String[] lines = input.split("\n");
        String[] outputs = expectedOutputs.split("\n");
        Plateau plateau = newPlateauWithZeroMinCoords(parseInt(lines[0].split(" ")[0]), parseInt(lines[0].split(" ")[1]));
        int counter = 0;
        int i = 1;

        while (i < lines.length){
            Drone drone = new Drone(UUID.randomUUID(), plateau, new Position(parseInt(lines[i].split(" ")[0]), parseInt(lines[i].split(" ")[1])), Direction.valueOf(lines[i].split(" ")[2]));
            List<Movement> movements = lines[i+1].chars().mapToObj(operand -> Movement.valueOf(String.valueOf((char) operand))).toList();

            // When
            Drone result = drone.move(movements);

            // Then
            assertEquals(outputs[counter], result.toString());
            counter++;
            i+=2;
        }
    }
}