package projekt.tel.fer.smarthome;

/**
 * Created by Jakov on 06/01/2017.
 */

public class Measurement {

    private int sensorID;
    private String location;
    private String sensorType;
    private String value;
    private String measuringUnit;
    private String timeOfLastMeasurement;

    public Measurement() {
    }

    public Measurement(int sensorID, String location, String sensorType, String value, String measuringUnit, String timeOfLastMeasurement) {
        this.sensorID = sensorID;
        this.location = location;
        this.sensorType = sensorType;
        this.value = value;
        this.measuringUnit = measuringUnit;
        this.timeOfLastMeasurement = timeOfLastMeasurement;
    }

    public int getSensorID() {
        return sensorID;
    }

    public void setSensorID(int sensorID) {
        this.sensorID = sensorID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public void setMeasuringUnit(String measuringUnit) {
        this.measuringUnit = measuringUnit;
    }

    public String getTimeOfLastMeasurement() {
        return timeOfLastMeasurement;
    }

    public void setTimeOfLastMeasurement(String timeOfLastMeasurement) {
        this.timeOfLastMeasurement = timeOfLastMeasurement;
    }
}
