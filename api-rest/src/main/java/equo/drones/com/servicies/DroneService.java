package equo.drones.com.servicies;

import equo.drones.com.model.*;
import equo.drones.com.repositories.DroneRepository;

import java.util.List;
import java.util.UUID;

public class DroneService {
    private final DroneRepository droneRepository;

    public DroneService(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    public Drone placeDrone(Plateau plateau, Position position, Direction direction) {
        Drone drone = new Drone(UUID.randomUUID(), plateau, position, direction);
        droneRepository.addDrone(drone);
        return drone;
    }

    public Drone move(Drone drone, List<Movement> movements){
        Drone responseDrone = new Drone(drone.getId(), drone.getPlateau(), drone.getPosition(), drone.getDirection());
        responseDrone.move(movements);
        return responseDrone;
    }

    public Drone findById(UUID id) {
        return droneRepository.getDrone(id);
    }
}
