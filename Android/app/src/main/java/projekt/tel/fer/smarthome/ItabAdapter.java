package projekt.tel.fer.smarthome;

import android.content.Context;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Lovro on 20.12.2016..
 */

public class ItabAdapter extends RecyclerView.Adapter<ItabAdapter.ViewHolder>{

    private boolean ASYNC1_OVER = false;
    private boolean ASYNC2_OVER = false;



    private static HashMap<String, Actuator> actuatorMap;
    private static Context context;
    public static YourPreference yourPreference;



    public ItabAdapter(Context context) {
        this.context = context;
        yourPreference = YourPreference.getInstance(context);


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class LightControlHolder extends ViewHolder {
        ListView listView;
        ImageButton imgBtn;

        public LightControlHolder (View v) {
            super(v);
             listView  = (ListView) v.findViewById(R.id.lights_list);
                imgBtn = (ImageButton) v.findViewById(R.id.refresh);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.control_card, viewGroup, false);
        return new LightControlHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final LightControlHolder holder = (LightControlHolder) viewHolder;


        holder.imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
            }
        });
             yourPreference = YourPreference.getInstance(context);
            new AsyncTask<String,String,HashMap<String,Actuator>>() {
                public HashMap<String, Actuator> actuators = new HashMap<>();
                @Override
                protected HashMap<String,Actuator> doInBackground(String... urls) {
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
                        JSONObject jsonObject = new JSONObject(finalJSON);
                        JSONObject jsonObjectParent = jsonObject.getJSONObject("_embedded");
                        JSONArray jsonArrayChild = jsonObjectParent.getJSONArray("veryShortActuatorRepresentations");

                        for (int i = 0; i < jsonArrayChild.length(); i++){
                            JSONObject actuatorJSON = jsonArrayChild.getJSONObject(i);
                            Actuator actuator = new Actuator();
                            actuator.setActuatorID(actuatorJSON.getInt("lightId"));
                            actuator.setLocation(actuatorJSON.getString("name"));
                            actuators.put(actuator.getLocation(), actuator);
                        }

                        return actuators;

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
                protected void onPostExecute(HashMap<String,Actuator> s) {
                    super.onPostExecute(s);
                    actuatorMap = new HashMap<String, Actuator>(s);
                    ASYNC1_OVER = true;
                    getActuators(holder);
                    //listener.onTaskCompleted((new LinkedList<Actuator>(s.values())));
                }

            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, yourPreference.getData("URL") + "api/actuator/light");


    }

    @Override
    public int getItemCount() {
        return 1;
    }


    public void getActuators(final LightControlHolder holder) {

        for (Actuator a: actuatorMap.values()) {
            yourPreference = YourPreference.getInstance(context);
                new AsyncTask<String,String,HashMap<String ,Actuator>>() {

                    private HashMap<String,Actuator> actuators;


                    @Override
                    protected HashMap<String,Actuator> doInBackground(String... urls) {
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
                            JSONObject jsonObject = new JSONObject(finalJSON);

                            //actuators = Data.getLightActuators();
                            Actuator actuator = actuatorMap.get(jsonObject.getString("name"));
                            actuator.setValue(jsonObject.getBoolean("state"));
                            actuatorMap.put(jsonObject.getString("name"),actuator);


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
                            return actuatorMap;

                        }

                    }

                    @Override
                    protected void onPostExecute(HashMap<String,Actuator> s) {
                        super.onPostExecute(s);
                        actuatorMap = s;
                        //instantiate custom adapter
                        SwitchListAdapter adapter = new SwitchListAdapter(actuatorMap, context);

                        //handle listview and assign adapter
                        holder.listView.setAdapter(adapter);

                        //Data data = new Data(s);
                    }


                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, yourPreference.getData("URL") + "api/actuator/light/" + a.getActuatorID());

        }
    }
}
