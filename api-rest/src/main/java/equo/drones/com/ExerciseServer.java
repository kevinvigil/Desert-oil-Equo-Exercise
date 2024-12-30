package equo.drones.com;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import equo.drones.com.model.*;
import equo.drones.com.repositories.DroneRepository;
import equo.drones.com.repositories.PlateauRepository;
import equo.drones.com.servicies.DroneService;
import equo.drones.com.servicies.PlateauService;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static java.util.UUID.fromString;

public class ExerciseServer {
    private static final Logger log = Logger.getLogger(ExerciseServer.class.getName());
    private DroneService droneService;
    private PlateauService plateauService;

    public ExerciseServer(DroneService droneService, PlateauService plateauService) {
        this.droneService = droneService;
        this.plateauService = plateauService;

    }

    public void start(int port) {
        try {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            // Generic context for paths starting with "/plateau"
            server.createContext("/plateau", exchange -> {
                String path = exchange.getRequestURI().getPath();

//                if (path.matches("/plateau/drone/.+")) {
//                    patchDroneRequest(exchange, fromString(path.split("/")[3])); // Handle specific path for PATCH
//                } else
                if (path.matches("/plateau/.+/drone")) {
                    postDroneRequest(exchange, fromString(path.split("/")[2])); // Handle specific path for POST
                } else if (path.equals("/plateau")) {
                    postPlateauRequest(exchange); // Handle /plateau
                } else {
                    exchange.sendResponseHeaders(404, -1); // Not found
                }
            });
            server.createContext("/drone", exchange -> {
                String path = exchange.getRequestURI().getPath();
                if (path.matches("/drone/.+/move")) {
                    patchDroneRequest(exchange, fromString(path.split("/")[2])); // Handle specific path for PATCH
                } else {
                    exchange.sendResponseHeaders(404, -1); // Not found
                }
            });
        server.setExecutor(null);
        server.start();
        } catch (Exception e) {
            log.severe("Error:" + e.getMessage() + " - " + e.getStackTrace());
            throw new RuntimeException(e);
        }
    }

    private void patchDroneRequest(HttpExchange httpExchange, UUID droneId) throws IOException {
        try {
            if (!httpExchange.getRequestMethod().equals("POST")) {
                httpExchange.sendResponseHeaders(405, -1);
                return;
            }
            log.info("PATCH DRONE REQUEST");

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));

            List<Movement> movements = reader.readLine().chars().mapToObj(m -> Movement.valueOf(String.valueOf((char) m))).toList();

            Drone drone = droneService.findById(droneId);

            Drone droneFinalPosition = droneService.move(drone, movements);

            httpExchange.getResponseHeaders().set("Content-Type", "text/plain");
            httpExchange.sendResponseHeaders(200, droneFinalPosition.toString().length());
            OutputStream os = httpExchange.getResponseBody();
            os.write((droneFinalPosition.getPosition().getX() + " " + droneFinalPosition.getPosition().getY()
                    + " " + droneFinalPosition.getDirection().toString()).getBytes());
            os.close();
        } catch (Exception e) {
            log.severe("Error:" + e.getMessage() + " - " + e.getStackTrace());
            throw new RuntimeException(e);
        }
    }

    /*
     - Crear tests
     - Capa de Servicio que es la que ejecuta y devuelve un resultado
     - Capa de Comunicacion que es la que hace lo mapeos :
        recivis String, lo transformas a Plateau, Drone, etc y llamas a la capa de servicio
     */

    private void postDroneRequest(HttpExchange httpExchange, UUID plateauId) throws IOException {
        try {
            Plateau plateau = plateauService.getPlateauById(plateauId);
            if (plateau == null) {

                httpExchange.sendResponseHeaders(412, 0);
                log.info("Plateau is null");

            } else if (httpExchange.getRequestMethod().equals("POST")) {

                log.info("Received POST request");

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));

                List<String> r = Arrays.stream(reader.readLine().split(" ")).toList();

                Position position = new Position(Integer.parseInt(r.get(0)), Integer.parseInt(r.get(1)));
                Direction direction = Direction.valueOf(r.get(2));
                Drone droneInitialPosition = droneService.placeDrone(plateau, position, direction);

                httpExchange.getResponseHeaders().set("Content-Type", "text/plain");
                httpExchange.sendResponseHeaders(200, droneInitialPosition.getId().toString().length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(droneInitialPosition.getId().toString().getBytes());
                os.close();
            } else {
                httpExchange.sendResponseHeaders(405, -1);
            }
        } catch (Exception e) {
            log.severe("Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void postPlateauRequest(HttpExchange httpExchange) throws IOException {
        try {
            if (!httpExchange.getRequestMethod().equals("POST")) {
                httpExchange.sendResponseHeaders(405, -1);
                return;
            }

            InputStream inputStream = httpExchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            List<Integer> plateauLineInput = Arrays.stream(reader.readLine().split(" ")).toList().stream().map(Integer::parseInt).toList();

            Plateau plateau;
            if (plateauLineInput.size() > 2) {
                plateau = new Plateau(UUID.randomUUID(), plateauLineInput.get(0), plateauLineInput.get(1), plateauLineInput.get(2), plateauLineInput.get(3));
            } else {
                plateau = Plateau.newPlateauWithZeroMinCoords(plateauLineInput.get(0), plateauLineInput.get(1));
            }
            plateauService.createPlateau(plateau);

            String responseStr = "Plateau created";
            log.info(responseStr);
            httpExchange.getResponseHeaders().set("Content-Type", "text/plain");
            httpExchange.sendResponseHeaders(200, responseStr.getBytes().length);
            OutputStream os = httpExchange.getResponseBody();
            os.write(responseStr.getBytes());
            os.close();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.severe("Error:" + e.getMessage() + " - " + sw);
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        PlateauRepository plateauRepository = PlateauRepository.getInstance();
        PlateauService plateauService = new PlateauService(plateauRepository);
        DroneRepository droneRepository = DroneRepository.getInstance();
        DroneService droneService = new DroneService(droneRepository);
        ExerciseServer server = new ExerciseServer(droneService, plateauService);
        server.start(8080);
    }
}
