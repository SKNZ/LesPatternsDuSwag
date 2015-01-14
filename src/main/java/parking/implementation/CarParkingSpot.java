package parking.implementation;

import parking.api.business.concrete.BaseParkingSpot;

/**
 * Created by SKNZ on 31/12/2014.
 */
public class CarParkingSpot extends BaseParkingSpot {
    static {
        vehicleTypeFits.put(Carrier.class,      false);
        vehicleTypeFits.put(Car.class,          true);
        vehicleTypeFits.put(Motorbike.class,    true);
    }

    public CarParkingSpot(Integer id) {
        this.id = id;
    }
}