package projekt.tel.fer.smarthome;

/**
 * Created by Jakov on 14/01/2017.
 */

public class Actuator {

    private String location;
    private int actuatorID;
    private Boolean value;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getActuatorID() {
        return actuatorID;
    }

    public void setActuatorID(int actuatorID) {
        this.actuatorID = actuatorID;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Location: %s, ID: %d\n",location,actuatorID);
    }
}
