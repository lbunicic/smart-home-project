package projekt.tel.fer.smarthome;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import projekt.tel.fer.smarthome.MainActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import android.content.SharedPreferences;  //<_----

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;



/**
 * Created by Lovro on 20.12.2016..
 */

public class HumTabAdapter extends RecyclerView.Adapter<HumTabAdapter.ViewHolder> implements OnTaskCompletedHumidity {

    public static String currentPeriodHumidity;
    public static String currentSensorHumidity;
    public static int cardNumHumidity;
    public static int brojacHumidity;
    public static HashMap<String,Measurement> humSensors;

  //  public static MainActivity  MnActivity;

    public static final int SETTINGS =1;
    public static final int GRAPH = 2;
    public static final int CURRENT = 3;
    public static final int AVERAGE = 4;
    public static Context contextHumidity;
    public static YourPreference yourPreference;

    //public static Context contextTemp;
    //public SharedPreferences settings = ;
    //public static final String defaultURL = "http://iotat.tel.fer.hr:59984/api/sensors/";

    public  HumTabAdapter (Context contextHumidity) {
        yourPreference = YourPreference.getInstance(contextHumidity);
        HumTabAdapter.contextHumidity = contextHumidity;
        cardNumHumidity = 1;
        brojacHumidity = 0;
    }

    @Override
    public void OnTaskCompletedCurrent(ViewHolder viewHolder, List<Measurement> list) {
        Measurement measurement = list.get(list.size()-1);
        CurrentViewHolder cViewHolder = (CurrentViewHolder) viewHolder;
        cViewHolder.current.setText(measurement.getValue() + " %" );
        cViewHolder.title.setText("Current humidity");
    }

    @Override
    public void OnTaskCompletedAverage(ViewHolder viewHolder, List<Measurement> list) {
        float sum = 0;
        for (Measurement m: list) {
            sum += Float.parseFloat(m.getValue());
        }

        AverageViewHolder avgViewHolder = (AverageViewHolder) viewHolder;
        avgViewHolder.avg.setText(Float.toString(sum/list.size()) + " " + "%");
        avgViewHolder.title.setText("Average humidity");

    }

    @Override
    public void OnTaskCompletedGraph(ViewHolder viewHolder, List<Measurement> list) {
        GraphViewHolder gViewHolder = (GraphViewHolder) viewHolder;
        List<BarEntry> entries = new ArrayList<>();
        int br=0;
        for (Measurement m : list) {
            entries.add(new BarEntry(br, Float.parseFloat(m.getValue())));
            br++;
        }

        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        set.setColor(ContextCompat.getColor(contextHumidity,R.color.graph1));
        BarData data = new BarData(set);
        data.setBarWidth(0.7f); // set custom bar width

        Description desc = new Description();
        desc.setText("");
        gViewHolder.barChart.setDescription(desc);
        gViewHolder.barChart.setExtraRightOffset(50);
        // gViewHolder.barChart.getAxisLeft().setEnabled(false);
        // gViewHolder.barChart.getAxisRight().setEnabled(false);
        // gViewHolder.barChart.getXAxis().setEnabled(false);
        gViewHolder.barChart.animateXY(2000,2000);
        gViewHolder.barChart.getLegend().setEnabled(false);
        gViewHolder.barChart.setVisibleXRangeMaximum(31); //maksimalni broj trenutno prikazanih stupaca
        gViewHolder.barChart.setVisibleXRangeMinimum(7);
        gViewHolder.barChart.setData(data);
        gViewHolder.barChart.setFitBars(true); // make the x-axis fit exactly all bars
        gViewHolder.barChart.setScaleYEnabled(false); //da se ne moze ici po grafu gore dolje
        gViewHolder.barChart.setScaleXEnabled(true);
        gViewHolder.barChart.invalidate(); // refresh

    }

    @Override
    public void OnTaskCompletedSettings(ViewHolder viewHolder, List<Measurement> list) {
        SettingsViewHolder sViewHolder = (SettingsViewHolder) viewHolder;
        List<String> dataset = new LinkedList<>();
        humSensors = new HashMap<>();

        for (Measurement sensor : list) {
            dataset.add(sensor.getLocation());
            humSensors.put(sensor.getLocation(),sensor);
        }
        Data sensorData = new Data(humSensors,true);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(contextHumidity, R.layout.custom_spinner_item, dataset);
        sViewHolder.sensorSpinner.setAdapter(adapter);

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }


    public class SettingsViewHolder extends ViewHolder {
        Spinner sensorSpinner;
        Spinner periodSpinner;

        public SettingsViewHolder(View v) {
            super(v);
            this.sensorSpinner = (Spinner) v.findViewById(R.id.sensor_spinner);
            this.periodSpinner = (Spinner) v.findViewById(R.id.period_spinner);
        }
    }

    public class CurrentViewHolder extends ViewHolder {
        TextView current;
        TextView title;

        public CurrentViewHolder(View v) {
            super(v);
            this.current = (TextView) v.findViewById(R.id.inside_value);
            this.title = (TextView) v.findViewById(R.id.current_title);
        }
    }

    public class AverageViewHolder extends ViewHolder {
        TextView avg;
        TextView title;

        public AverageViewHolder(View v) {
            super(v);
            this.avg = (TextView) v.findViewById(R.id.value);
            this.title=(TextView) v.findViewById(R.id.title);
        }
    }

    public class GraphViewHolder extends ViewHolder {
        BarChart barChart;
        TextView title;
        public GraphViewHolder(View v) {
            super(v);
            this.title =(TextView) v.findViewById(R.id.current_title);
            this.barChart = (BarChart) v.findViewById(R.id.bar_chart);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if (viewType == CURRENT) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.current_card_01, viewGroup, false);
            return new CurrentViewHolder(v);
        } else if (viewType == AVERAGE) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.average_card_01, viewGroup, false);
            return new AverageViewHolder(v);
        } else if(viewType == SETTINGS) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.settings_card, viewGroup, false);
            return new SettingsViewHolder(v);
        } else {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.humidity_graph, viewGroup, false);
            return new GraphViewHolder(v);
        }
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case SETTINGS:
                SettingsViewHolder sViewHolder = (SettingsViewHolder) viewHolder;

                //ODVIJA SE NA POČETKU:

                //Pozivamo AsyncTask da popuni spinner senzorima
                OnTaskCompletedHumidity taskCompleted;

                YourPreference yourPreference = YourPreference.getInstance(contextHumidity);

                new SpinnerParserHumidity(viewHolder, this ,
                        contextHumidity).execute(yourPreference.getData("URL") +"api/sensors/humidity/");

//settings = getSharedPreferences("URLsettings", 0);
//settings.getString("URLadr", defaultURL )+ "humidity/"


                //Punimo period spinner (hardcodirano)
                 LinkedList<String> dataset = new LinkedList<>(Arrays.asList("Day", "Week", "Month"));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(contextHumidity, R.layout.custom_spinner_item, dataset);
                sViewHolder.periodSpinner.setAdapter(adapter);

                //određujemo početni period i senzor
                try{
                    currentPeriodHumidity = sViewHolder.periodSpinner.getSelectedItem().toString();
                    currentSensorHumidity = sViewHolder.sensorSpinner.getSelectedItem().toString();
                } catch (NullPointerException e) {
                    //do nothing
                }



                //ODVIJA SE KADA JE ODABRAN NEKI ITEM U SPINNERIMA:

                /*
                 * Ako se odabere neki item periodSpinnera potrebno je azurirati odabrani senzor i period
                 * te pozvati sve kartice dase refreshaju.
                 */
                sViewHolder.periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        String selectedPeriod = parent.getItemAtPosition(position).toString().toLowerCase();

                        currentPeriodHumidity = selectedPeriod;
                        if (brojacHumidity !=0) {
                            cardNumHumidity = 4;
                            brojacHumidity++;
                        }


                        notifyItemChanged(1);
                        notifyItemChanged(2);
                        notifyItemChanged(3);

                    } // to close the onItemSelected
                    public void onNothingSelected(AdapterView<?> parent)
                    {

                    }
                });


                /*
                analogno periodSpinneru
                 */

                sViewHolder.sensorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        String selectedSensor = parent.getItemAtPosition(position).toString();
                        currentSensorHumidity = selectedSensor;
                        if (brojacHumidity !=0) {
                            cardNumHumidity = 4;
                        }
                        brojacHumidity++;

                        notifyItemChanged(1);
                        notifyItemChanged(2);
                        notifyItemChanged(3);

                    } // to close the onItemSelected
                    public void onNothingSelected(AdapterView<?> parent)
                    {

                    }
                });


                break;
            case CURRENT:

                //CurrentViewHolder currentViewHolder = (CurrentViewHolder) viewHolder;
                //currentViewHolder.current.setText(currentPeriod);
                Data sensorsData = new Data();

                yourPreference = YourPreference.getInstance(contextHumidity);
                new DataTaskHumidity(viewHolder, this).execute(yourPreference.getData("URL") +"api/sensors/humidity/" + (sensorsData.getSensorsHum().get(currentSensorHumidity).getSensorID()) + "/" + "day");

                break;
            case AVERAGE:
                //AverageViewHolder avgViewHolder = (AverageViewHolder) viewHolder;
                sensorsData = new Data();
                yourPreference = YourPreference.getInstance(contextHumidity);
                new DataTaskHumidity(viewHolder, this).execute(yourPreference.getData("URL") +"api/sensors/humidity/"  + (sensorsData.getSensorsHum().get(currentSensorHumidity).getSensorID()) + "/" +currentPeriodHumidity );


                break;
            case GRAPH:
                /*GraphViewHolder graphViewHolder = (GraphViewHolder) viewHolder;
                graphViewHolder.title.setText(currentSensorHumidity);*/
                sensorsData = new Data();

                yourPreference = YourPreference.getInstance(contextHumidity);
                 new DataTaskHumidity(viewHolder,this).execute(yourPreference.getData("URL") +"api/sensors/humidity/" +  (sensorsData.getSensorsHum().get(currentSensorHumidity).getSensorID()) + "/" +currentPeriodHumidity );


        }
    }

    @Override
    public int getItemCount() {
        return cardNumHumidity;
    }

    @Override
    public int getItemViewType(int position) {
        int mDataSetTypes[] = {SETTINGS, GRAPH, CURRENT, AVERAGE};
        return mDataSetTypes[position];
    }
}
