package parking.api.business.parking;

import parking.api.business.Utils;
import parking.api.business.observer.BaseObservable;
import parking.api.exceptions.ParkingBookedSpotsExceptions;
import parking.api.exceptions.ParkingExistsException;
import parking.api.exceptions.ParkingNotPresentException;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by SKNZ on 28/12/2014.
 */
public class ParkingApplicationManager extends BaseObservable<ParkingApplicationManager> implements Serializable, Iterable<Parking> {
    public static final long serialVersionUID = 1L;
    private static ParkingApplicationManager instance = new ParkingApplicationManager();
    private String companyName;
    private Map<Integer, Parking> parkingsById = new HashMap<>();
    private Map<String, Object> config = new HashMap<>();

    private ParkingApplicationManager() {
    }

    // For unit testing purposes only, PACKAGE LOCAL
    public static void resetSingleton() {
        instance = new ParkingApplicationManager();
    }

    /**
     * Get the instance of the parking manager
     *
     * @return The instance of the parking manager
     */
    public static ParkingApplicationManager getInstance() {
        return instance;
    }

    /**
     * Get the name of the company
     *
     * @return The name of the company
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Set the name of the company
     *
     * @param companyName The name of the company
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Create a new parking
     *
     * @param name name of the parking
     * @return the new parking
     * @throws ParkingExistsException raised if such a parking already exist
     */
    public Parking newParking(String name) {
        int id = this.count();
        for (; ; ) {
            try {
                return this.newParking(++id, name);
            } catch (ParkingExistsException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create a new parking
     *
     * @param id   id of the parking
     * @param name name of the parking
     * @return The new parking
     * @throws ParkingExistsException raised if a parking with the same id already exist
     */
    public Parking newParking(Integer id, String name) throws ParkingExistsException {
        if (parkingsById.containsKey(id))
            throw new ParkingExistsException(id);

        Parking parking = new Parking(id, name);
        parkingsById.put(id, parking);

        notifyObservers();
        return parking;
    }

    /**
     * Delete a parking
     *
     * @param id Id of the parking to delete
     * @throws ParkingNotPresentException   Raised when the parking to delete doesn't exist
     * @throws ParkingBookedSpotsExceptions Raised when there are booked spots in the parking
     */
    public void deleteParking(Integer id) throws ParkingNotPresentException, ParkingBookedSpotsExceptions {
        if (!parkingsById.containsKey(id))
            throw new ParkingNotPresentException(id);

        Parking parking = parkingsById.get(id);
        if (parking.countParkingSpots(parkingSpot -> false) != 0)
            throw new ParkingBookedSpotsExceptions(parking);

        parkingsById.remove(parking.getId());
        notifyObservers();
    }

    /**
     * Test if parking manager contains the parking passed as parameter
     *
     * @param id Id of the parking to test
     * @return True if contains the parking, false otherwise
     */
    public Boolean containsParking(Integer id) {
        return parkingsById.containsKey(id);
    }

    /**
     * Get the parking with the id passed as parameter
     *
     * @param id Id of the parking to get
     * @return the parking with the id passed as parameter
     * @throws ParkingNotPresentException Raised if no parking found
     */
    public Parking getParkingById(Integer id) throws ParkingNotPresentException {
        if (!parkingsById.containsKey(id))
            throw new ParkingNotPresentException(id);

        return parkingsById.get(id);
    }

    /**
     * Count the number of parking in parking manager
     *
     * @return the number of parking in parking manager
     */
    public int count() {
        return count(parking -> true);
    }

    /**
     * Count the number of parking in parking manager where the predicate respond true
     *
     * @param predicate condition to be tested
     * @return the number of parking in parking manager where the predicate respond true
     */
    public int count(Predicate<Parking> predicate) {
        int i = 0;
        for (Parking parking : parkingsById.values()) {
            if (predicate.test(parking))
                ++i;
        }
        return i;
    }

    /**
     * readResolve() is used for replacing the object read from the stream (for the serialization)
     *
     * @return the instance
     * @throws ObjectStreamException
     */
    protected Object readResolve() throws ObjectStreamException {
        return instance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParkingApplicationManager that = (ParkingApplicationManager) o;

        if (companyName != null ? !companyName.equals(that.companyName) : that.companyName != null) return false;
        if (parkingsById != null ? !parkingsById.equals(that.parkingsById) : that.parkingsById != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = companyName != null ? companyName.hashCode() : 0;
        result = 31 * result + (parkingsById != null ? parkingsById.hashCode() : 0);
        return result;
    }

    @Override
    public Iterator<Parking> iterator() {
        return parkingsById.values().iterator();
    }

    @Override
    public void forEach(Consumer<? super Parking> action) {
        parkingsById.values().forEach(action);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException, ClassNotFoundException {
        stream.writeObject(instance);
        stream.writeObject(parkingsById);
        stream.writeObject(companyName);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        instance = (ParkingApplicationManager) stream.readObject();
        parkingsById = Utils.<Map<Integer, Parking>>uncheckedCast(stream.readObject());
        companyName = (String) stream.readObject();
    }

    public void write(ObjectOutputStream stream) {
        try {
            writeObject(stream);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void read(ObjectInputStream stream) {
        try {
            readObject(stream);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Object get(String key) {
        return config.get(key);
    }

    public Integer getInt(String key) {
        return (Integer) config.get(key);
    }

    public Boolean getBool(String key) {
        return (Boolean) config.get(key);
    }

    public Double getDouble(String key) {
        return (Double) config.get(key);
    }

    public String getString(String key) {
        return (String) config.get(key);
    }
}
