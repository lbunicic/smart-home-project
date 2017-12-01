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
import java.util.List;
import java.util.Map;

/**
 * Created by Lovro on 7.1.2017..
 */

public class DataTaskHumidity extends AsyncTask<String,String,List<Measurement>> {

    OnTaskCompletedHumidity listener;
    Context context;
    HumTabAdapter.ViewHolder viewHolder;
    public DataTaskHumidity(HumTabAdapter.ViewHolder viewHolder, OnTaskCompletedHumidity listener) {
        this.listener = listener;
        this.viewHolder = viewHolder;
    }

    @Override
    protected LinkedList<Measurement> doInBackground(String... urls) {
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
            JSONArray parentArray = b.getJSONArray("shortSensorRepresentations");

            StringBuilder builder = new StringBuilder();
            Map<Integer, Measurement> senzori = new HashMap<>();
            LinkedList<Measurement> list = new LinkedList<>();



            for(int i = 0; i < parentArray.length(); i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);
                Measurement measurement = new Measurement();
                measurement.setTimeOfLastMeasurement(finalObject.getString("timeOfLastMeasurement"));
                measurement.setSensorID(finalObject.getInt("sensorId"));
                measurement.setLocation(finalObject.getString("location"));
                measurement.setValue(finalObject.getString("value"));
                measurement.setSensorType(finalObject.getString("sensorType"));
                measurement.setMeasuringUnit(finalObject.getString("measuringUnit"));
                senzori.put(measurement.getSensorID(), measurement);
                list.add(measurement);
            }

            for(Measurement sensor : senzori.values()){
                builder.append(sensor.toString() + "\n\n");
            }

            return list;

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
    protected void onPostExecute(List<Measurement> list) {

        if(viewHolder instanceof HumTabAdapter.CurrentViewHolder) {
            listener.OnTaskCompletedCurrent(viewHolder, list);
        } else if ( viewHolder instanceof HumTabAdapter.AverageViewHolder) {
            listener.OnTaskCompletedAverage(viewHolder, list);
        } else {
            listener.OnTaskCompletedGraph(viewHolder, list);
        }
    }

}
