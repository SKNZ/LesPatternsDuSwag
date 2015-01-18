package parking.api.business.parking;

import java.io.*;

/**
 * Created by Thomas on 18/01/2015.
 */
public class ParkingManagerSerializer {
    /**
     * Deserialize a ParkingManager object stored in save/parkingManager.ser
     * @return ParkingManager contained in save/parkingManager.ser
     */
    public static void deserialize() {
        ObjectInputStream ois = null;

        InputStream file = null;
        try {
            file = new ObjectInputStream(new FileInputStream("save/parkingManager.ser"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ois = new ObjectInputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ParkingManager.getInstance().read(ois);
    }

    /**
     * Serialize a Parking Manager
     * @param
     */
    public static void serialize() {
        ObjectOutputStream oos = null;
        try {

            OutputStream file = new ObjectOutputStream(new FileOutputStream("save/parkingManager.ser"));
            oos = new ObjectOutputStream(file);

            ParkingManager.getInstance().write(oos);
            oos.flush();

        } catch (FileNotFoundException | NotSerializableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (oos != null) {
                    oos.flush();
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}