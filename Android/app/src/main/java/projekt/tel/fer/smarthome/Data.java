package projekt.tel.fer.smarthome;

import java.util.HashMap;

/**
 * Created by Lovro on 8.1.2017..
 */

public class Data {


    private static String currentPeriod;
    private static String currentSensor;


    private static HashMap<String,Measurement> sensorsHum;
    private static HashMap<String,Measurement> sensorsTemp;


    public Data(HashMap<String,Measurement> sensors, boolean var) {
        if (var){
            sensorsHum = sensors;
        }else{
            sensorsTemp = sensors;
        }
    }

    public Data() {

    }

    public static String getCurrentPeriod() {
        return currentPeriod;
    }

    public static void setCurrentPeriod(String currentPeriod) {
        Data.currentPeriod = currentPeriod;
    }

    public static String getCurrentSensor() {
        return currentSensor;
    }

    public static void setCurrentSensor(String currentSensor) {
        Data.currentSensor = currentSensor;
    }

    public static HashMap<String, Measurement> getSensorsHum() {
        return sensorsHum;
    }

    public static void setSensorsHum(HashMap<String, Measurement> sensorsHum) {
        Data.sensorsHum = sensorsHum;
    }

    public static HashMap<String, Measurement> getSensorsTemp() {
        return sensorsTemp;
    }

    public static void setSensorsTemp(HashMap<String, Measurement> sensorsTemp) {
        Data.sensorsTemp = sensorsTemp;
    }



}


