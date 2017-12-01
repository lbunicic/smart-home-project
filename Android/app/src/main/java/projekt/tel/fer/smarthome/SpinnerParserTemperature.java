package projekt.tel.fer.smarthome;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Jakov on 10/01/2017.
 */

public class SpinnerParserTemperature extends AsyncTask<String, String, LinkedList<Measurement>> {
    Context context;
    OnTaskCompletedTemperature listener;
    TempTabAdapter.ViewHolder viewHolder;

    public SpinnerParserTemperature(TempTabAdapter.ViewHolder viewHolder, OnTaskCompletedTemperature listener, Context context){
        super();
        this.context = context;
        this.listener = listener;
        this.viewHolder = viewHolder;
    }

    public static Map<Integer, Measurement> sensors = new HashMap<>();

    @Override
    protected LinkedList<Measurement> doInBackground(String...urls ) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urls[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream in = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(in));

            StringBuilder buffer = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            String finalJSON = buffer.toString();
            JSONObject parentObject = new JSONObject(finalJSON);
            JSONObject b = parentObject.getJSONObject("_embedded");
            JSONArray parentArray = b.getJSONArray("veryShortSensorRepresentations");
            //JSONArray parentArray = new JSONArray(finalJSON);


            StringBuilder builder = new StringBuilder();
            LinkedList<Measurement> senzori = new LinkedList<>();



            for(int i = 0; i < parentArray.length(); i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);
                Measurement measurement = new Measurement();
                //measurement.setTimeOfLastMeasurement(finalObject.getString("timeOfLastMeasurement"));
                measurement.setSensorID(finalObject.getInt("sensorId"));
                measurement.setLocation(finalObject.getString("location"));
                //measurement.setValue(finalObject.getString("value"));
                //measurement.setSensorType(finalObject.getString("sensorType"));
                //measurement.setMeasuringUnit(finalObject.getString("measuringUnit"));
               /* JSONObject finalObject = parentArray.getJSONObject(i);
                Measurement measurement = new Measurement();
                measurement.setTimeOfLastMeasurement(finalObject.getString("date"));
                measurement.setSensorID(finalObject.getInt("id"));
                measurement.setLocation(finalObject.getString("location"));
                measurement.setValue(finalObject.getString("value"));
                measurement.setSensorType(finalObject.getString("typeOfSensor"));
                measurement.setMeasuringUnit(finalObject.getString("unit"));*/

                senzori.add(measurement);
            }

            return senzori;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch(JSONException e){
            e.printStackTrace();
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(LinkedList<Measurement> list) {
        listener.OnTaskCompletedSettings(viewHolder, list);

    }
}
