package parking.implementation;

/**
 * Created by  on 14/01/15.
 */
public class Client {

    private String lastName;
    private String firstName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    @Override
    public String toString() {
        return lastName + " " + firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
