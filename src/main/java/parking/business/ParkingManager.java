package parking.business;

import parking.exceptions.ParkingBookedSpotsExceptions;
import parking.exceptions.ParkingExistsException;
import parking.exceptions.ParkingNotPresentException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SKNZ on 28/12/2014.
 */
public class ParkingManager {
    private static ParkingManager instance = new ParkingManager();
    private String companyName;
    private Map<Integer, Parking> parkingsById = new HashMap<>();

    private ParkingManager() {

    }

    // For unit testing purposes only, PACKAGE LOCAL
    static void resetSingleton() {
        instance = new ParkingManager();
    }

    public static ParkingManager getInstance() {
        return instance;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Parking newParking(Integer id, String name) throws ParkingExistsException {
        if (parkingsById.containsKey(id))
            throw new ParkingExistsException(id);

        Parking parking = new Parking(id, name);
        parkingsById.put(id, parking);

        return parking;
    }

    public void deleteParking(Integer id) throws ParkingNotPresentException, ParkingBookedSpotsExceptions {
        if (!parkingsById.containsKey(id))
            throw new ParkingNotPresentException(id);

        Parking parking = parkingsById.get(id);
        if (parking.countParkingSpots(parkingSpot -> false) != 0)
            throw new ParkingBookedSpotsExceptions(parking);

        parkingsById.remove(parking.getId());
    }

    public Parking getParkingById(Integer id) throws ParkingNotPresentException {
        if (!parkingsById.containsKey(id))
            throw new ParkingNotPresentException(id);

        return parkingsById.get(id);
    }
}
