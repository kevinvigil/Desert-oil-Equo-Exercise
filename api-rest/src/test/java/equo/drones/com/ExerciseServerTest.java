package equo.drones.com;

import equo.drones.com.model.Drone;
import equo.drones.com.repositories.DroneRepository;
import equo.drones.com.repositories.PlateauRepository;
import equo.drones.com.servicies.DroneService;
import equo.drones.com.servicies.PlateauService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ExerciseServerTest {
    private static PlateauRepository plateauRepository = PlateauRepository.getInstance();
    private static PlateauService plateauService = new PlateauService(plateauRepository);
    private static DroneRepository droneRepository = DroneRepository.getInstance();
    private static DroneService droneService = new DroneService(droneRepository);

    private static ExerciseServer server = new ExerciseServer(droneService, plateauService);

    @BeforeAll
    public static void setup() {
        server.start(8080);
    }

    @Test
    public void shouldCreatePlateau() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/plateau").openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "text/plain");


        OutputStream os = connection.getOutputStream();
        os.write("5 5".getBytes());
        os.flush();
        os.close();

        InputStream responseStream = connection.getInputStream();
        String response = new BufferedReader(new InputStreamReader(responseStream)).lines().reduce("", (acc, line) -> acc + line);
        assertTrue(response.contains("Plateau created"));

        connection.disconnect();
    }

    @Test
    public void shouldCreateDrone() throws IOException {
        shouldCreatePlateau();
        UUID plateauId = plateauRepository.getPlateaus().getFirst().id();
        String url = "http://localhost:8080/plateau/" + plateauId.toString() + "/drone";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "text/plain");

        OutputStream os = connection.getOutputStream();
        os.write("3 3 E".getBytes());
        os.flush();
        os.close();
        InputStream responseStream = connection.getInputStream();
        String response = new BufferedReader(new InputStreamReader(responseStream)).lines().reduce("", (acc, line) -> acc + line);
        Drone drone = droneRepository.getDrones().getFirst();
        assertTrue(response.contains(drone.getId().toString()));
        connection.disconnect();
    }

    @Test
    public void shouldMoveTheDrone() throws IOException {
        shouldCreateDrone();
        Drone drone = droneRepository.getDrones().getFirst();

        String url = "http://localhost:8080/drone/"+drone.getId().toString()+"/move";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "text/plain");

        OutputStream os = connection.getOutputStream();
        os.write("MMRMMRMRRM".getBytes());
        os.flush();
        os.close();

        String response = new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().reduce("", (acc, line) -> acc + line);
        assertEquals(response, "5 1 E");
        connection.disconnect();
    }
}