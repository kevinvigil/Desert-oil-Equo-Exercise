package equo.drones.com.repositories;

import equo.drones.com.model.Plateau;

import java.util.*;
import java.util.logging.Logger;

public class PlateauRepository {
    Logger log = Logger.getLogger(PlateauRepository.class.getName());
    private static final PlateauRepository REPOSITORY = new PlateauRepository();

    private Map<UUID, Plateau> plateaus = new HashMap<>();

    private PlateauRepository() {}

    public static PlateauRepository getInstance() {
        return REPOSITORY;
    }

    public void addPlateau(Plateau plateau) {
        if (plateaus.get(plateau.id()) == null) {
            plateaus.put(plateau.id(), plateau);
        } else {
            log.info("log info, plateau already exists");
            // Do not fail to provide idempotency
        }
    }

    public Plateau getPlateau(UUID id) {
        return plateaus.get(id);
    }

    public List<Plateau> getPlateaus() {
        return new ArrayList<>(plateaus.values());
    }
}
