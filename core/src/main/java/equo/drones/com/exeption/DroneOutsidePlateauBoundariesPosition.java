package equo.drones.com.exeption;

public class DroneOutsidePlateauBoundariesPosition extends IllegalStateException {

    public DroneOutsidePlateauBoundariesPosition(String message) {
        super(message);
    }
}
