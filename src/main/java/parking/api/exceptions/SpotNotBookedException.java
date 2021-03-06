package parking.api.exceptions;

/**
 * Created by SKNZ on 29/12/2014.
 */
public class SpotNotBookedException extends ParkingException {
    /**
     * Raised when try to unbook a spot which has not been booked
     */
    public SpotNotBookedException() {
        super("Can not unbook a spot that's not booked.");
    }
}
