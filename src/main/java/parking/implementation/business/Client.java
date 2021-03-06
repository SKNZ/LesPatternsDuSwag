package parking.implementation.business;

import java.io.Serializable;

/**
 * Created by  on 14/01/15.
 */
public class Client implements Serializable {

    private String civility;
    private String lastName;
    private String firstName;

    /**
     * Construct a client
     *
     * @param civility  civility of a client
     * @param lastName  lastName of a client
     * @param firstName firstName of a client
     */
    public Client(String civility, String lastName, String firstName) {
        this.civility = civility;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    /**
     * Get the civility of the client
     *
     * @return the civility of the client
     */
    public String getCivility() {
        return civility;
    }

    /**
     * Get the LastName of the client
     *
     * @return the LastName of the client
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Get the FirstName of the client
     *
     * @return the FirstName of the client
     */
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String toString() {
        return lastName + " " + firstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (firstName != null ? !firstName.equals(client.firstName) : client.firstName != null) return false;
        if (lastName != null ? !lastName.equals(client.lastName) : client.lastName != null) return false;

        return true;
    }
}
