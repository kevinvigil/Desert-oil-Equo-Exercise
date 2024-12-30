package equo.drones.com.repositories;

import equo.drones.com.model.Drone;

import java.util.*;
import java.util.logging.Logger;

public class DroneRepository {
    Logger log = Logger.getLogger(DroneRepository.class.getName());
    private static final DroneRepository REPOSITORY = new DroneRepository();

    private Map<UUID, Drone> drones = new HashMap<>();

    private DroneRepository() {}

    public static DroneRepository getInstance() {
        return REPOSITORY;
    }

    public void addDrone(Drone drone) {
        if (drones.get(drone.getId()) == null) {
            drones.put(drone.getId(), drone);
        } else {
            log.info("log info, drone already exists");
            // Do not fail to provide idempotency
        }
    }

    public Drone getDrone(UUID id) {
        return drones.get(id);
    }

    public List<Drone> getDrones() {
        return new ArrayList<>(drones.values());
    }
}
