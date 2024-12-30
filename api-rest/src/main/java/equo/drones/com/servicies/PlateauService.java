package equo.drones.com.servicies;

import equo.drones.com.model.Plateau;
import equo.drones.com.repositories.PlateauRepository;

import java.util.UUID;

public class PlateauService {
    private PlateauRepository plateauRepository;

    public PlateauService(PlateauRepository plateauRepository) {
        this.plateauRepository = plateauRepository;
    }

    public Plateau getPlateauById(UUID plateauId) {
        return plateauRepository.getPlateau(plateauId);
    }

    public Plateau createPlateau(Plateau plateau) {
        Plateau responsePlateau = new Plateau(plateau.id(), plateau.minX(), plateau.maxX(), plateau.minY(), plateau.maxY());
        plateauRepository.addPlateau(responsePlateau);
        return responsePlateau;
    }
}
