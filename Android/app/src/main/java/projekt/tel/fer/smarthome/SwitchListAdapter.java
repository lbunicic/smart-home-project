package projekt.tel.fer.smarthome;

import android.content.Context;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.app.AppCompatActivity;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lovro on 13.1.2017..
 */

public class SwitchListAdapter extends BaseAdapter implements ListAdapter {

    private HashMap<String,Actuator> actuatorMap;
    private ArrayList<Actuator> actuatorList;
    private Context context;
    public static YourPreference yourPreference;


    public SwitchListAdapter(HashMap<String,Actuator> map, Context context) {
        this.actuatorMap = map;
        yourPreference = YourPreference.getInstance(context);
        this.actuatorList = new ArrayList<>(map.values());
        this.context = context;
    }

    @Override
    public int getCount() {
        return actuatorList.size();
    }

    @Override
    public Object getItem(int pos) {
        return actuatorList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return actuatorList.get(pos).getActuatorID();
        //just return 0 if your list items do not have an Id variable.
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.switch_list_item, null);
        }

        final Switch lightSwitch = (Switch) view.findViewById(R.id.switch1);
        final TextView lightName = (TextView) view.findViewById(R.id.actuator_name);
        final ImageView image = (ImageView) view.findViewById(R.id.bulb);

        lightName.setText(actuatorList.get(position).getLocation());
        //lightSwitch.setText(Integer.toString(actuatorList.get(position).getActuatorID()));

        if(actuatorList.get(position).getValue() == true) {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.lights_on));

        } else {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.lights_off));

        }
        lightSwitch.setChecked(actuatorList.get(position).getValue());

        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {


                new AsyncTask<String,String,String >() {
                        String state = Boolean.toString(isChecked);  //mozda !isChecked?

                        @Override
                        protected String doInBackground(String... urls) {
                
                           /* try{
                                URL url = new URL(urls[0]);
                                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                                //connection.setRequestProperty("Content-Type","application/json");

                                JSONObject body=new JSONObject();
                                body.put("state",state);
                                connection.setRequestMethod("PUT");
                                connection.setDoOutput(true);
                                connection.connect();
                                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

                                String body2 = "{\"state\":\"true\"}";
                                outputStream.writeBytes(body2);
                                //outputStream.writeBytes(body.toString());
                                outputStream.flush();
                                outputStream.close();

                            }
                            catch (IOException e) {
                                // Writing exception to log
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }*/

                            OkHttpClient client = new OkHttpClient();
                            String body2 = "{\"state\":"+state +"}";
                            RequestBody body = RequestBody.create(MediaType.parse("application/json"),body2);
                            yourPreference = YourPreference.getInstance(context);

                            Request request = new Request.Builder()
                                    .url( yourPreference.getData("URL")+ "api/actuator/light/" + actuatorList.get(position).getActuatorID())
                                    .put(body)
                                    //.addHeader("content-type", "application/json")
                                    .build();

                            try {
                                Response response = client.newCall(request).execute();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //do tu
                            return null;
                        }

                    }.execute(yourPreference.getData("URL") + "api/actuator/light/" + lightSwitch.getText());
                    actuatorList.get(position).setValue(isChecked);
                if(actuatorList.get(position).getValue() == true) {
                    image.setImageDrawable(context.getResources().getDrawable(R.drawable.lights_on));

                } else {
                    image.setImageDrawable(context.getResources().getDrawable(R.drawable.lights_off));

                }
                    notifyDataSetChanged();
            }
        });
        return view;
    }
}
