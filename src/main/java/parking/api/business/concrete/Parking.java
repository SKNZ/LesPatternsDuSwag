package parking.api.business.concrete;

import parking.api.business.contract.ParkingSpot;
import parking.api.business.contract.ParkingSpotFactory;
import parking.api.business.contract.ParkingSpotSelector;
import parking.api.business.contract.Vehicle;
import parking.api.exceptions.*;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by SKNZ on 28/12/2014.
 */
public class Parking implements Serializable {
    private final Integer id;
    private String name;
    private Map<Integer, ParkingSpot> parkingSpotsById = new HashMap<>();
    private ParkingSpotSelector parkingSpotSelector;

    Parking(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer countParkingSpots() {
        return parkingSpotsById.size();
    }

    public Integer countParkingSpots(Predicate<ParkingSpot> predicate) {
        return Long.valueOf(parkingSpotsById.values().stream().filter(predicate).count()).intValue();
    }

    public ParkingSpot newParkingSpot(ParkingSpotFactory parkingSpotFactory) {
        ParkingSpot parkingSpot = parkingSpotFactory.createParkingSpot();

        parkingSpotsById.put(parkingSpot.getId(), parkingSpot);

        return parkingSpot;
    }

    public Collection<ParkingSpot> newParkingSpot(ParkingSpotFactory parkingSpotFactory, Integer amount) {
        if (amount < 1)
            throw new IllegalArgumentException("Amount must be > 1, is " + amount);

        Collection<ParkingSpot> parkingSpots = new ArrayList<>();

        for (Integer i = 0; i < amount; ++i)
            parkingSpots.add(newParkingSpot(parkingSpotFactory));

        return parkingSpots;
    }

    public ParkingSpot getSpotBySpotId(Integer parkingSpotId) {
        return parkingSpotsById.getOrDefault(parkingSpotId, null);
    }

    public ParkingSpot getSpotByVehiclePlate(String plate) {
        return parkingSpotsById.values().stream().filter(parkingSpot ->
                        parkingSpot.isVehicleParked() && parkingSpot.getVehicle().getPlate().equals(plate)
        ).findFirst().orElse(null);
    }

    public void setParkingSpotSelector(ParkingSpotSelector parkingSpotSelector) {
        this.parkingSpotSelector = parkingSpotSelector;
    }

    // Undefined behaviour if vehicle already parked
    public ParkingSpot findAvailableParkingSpotForVehicle(Vehicle vehicle) throws NoSpotAvailableException {
        List<ParkingSpot> availableParkingSpots = parkingSpotsById.values().stream()
                .filter(parkingSpot -> parkingSpot.fits(vehicle) && !parkingSpot.isVehicleParked())
                .collect(Collectors.toList());

        if (availableParkingSpots.isEmpty())
            throw new NoSpotAvailableException();

        return parkingSpotSelector.select(vehicle, availableParkingSpots);
    }

    public void reorganizeParking() throws ReorganizationException {
        try {
            parkingSpotsById.values().stream()
                    .filter(parkingSpot -> parkingSpot.isVehicleParked() && !parkingSpotSelector.isOptimalParkingSpotForVehicle(parkingSpot, parkingSpot.getVehicle()))
                    .forEach(parkingSpot -> {
                        Vehicle vehicle = parkingSpot.unpark();
                        try {
                            ParkingSpot optimalParkingSpot = findAvailableParkingSpotForVehicle(vehicle);
                            optimalParkingSpot.park(vehicle);
                        } catch (NoSpotAvailableException e) {
                            e.printStackTrace();
                        } catch (SpotNotEmptyException | VehicleNotFitException | SpotBookedException e) {
                            throw new RuntimeException(new ReorganizationException());
                        }
                    });
        } catch (RuntimeException e) {
            if (e.getCause() instanceof ReorganizationException)
                throw (ReorganizationException) e.getCause();

            throw e;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Parking parking = (Parking) o;

        if (!id.equals(parking.id)) return false;
        if (name != null ? !name.equals(parking.name) : parking.name != null) return false;
        if (parkingSpotSelector != null ? !parkingSpotSelector.equals(parking.parkingSpotSelector) : parking.parkingSpotSelector != null)
            return false;

        return !(parkingSpotsById != null ? !parkingSpotsById.equals(parking.parkingSpotsById) : parking.parkingSpotsById != null);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (parkingSpotsById != null ? parkingSpotsById.hashCode() : 0);
        result = 31 * result + (parkingSpotSelector != null ? parkingSpotSelector.hashCode() : 0);
        return result;
    }
}
