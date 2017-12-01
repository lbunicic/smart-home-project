package projekt.tel.fer.smarthome;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lovro on 20.12.2016..
 */

public class TempTabAdapter extends RecyclerView.Adapter<TempTabAdapter.ViewHolder> implements OnTaskCompletedTemperature  {

    public static String currentPeriodTemperature;
    public static String currentSensorTemperature;
    public static HashMap<String,Measurement> tempSensors;
    public static int brojacTemperature;
    public static int cardNumTemperature;


    public static final int SETTINGS = 1;
    public static final int GRAPH = 2;
    public static final int CURRENT = 3;
    public static final int AVERAGE = 4;
    public static Context contextTemperature;
    public static YourPreference yourPreference;

    public TempTabAdapter(Context context) {
        TempTabAdapter.contextTemperature = context;
        yourPreference = YourPreference.getInstance(context);
        TempTabAdapter.cardNumTemperature = 1;
        TempTabAdapter.brojacTemperature = 0;
    }

    @Override
    public void OnTaskCompletedCurrent(ViewHolder viewHolder, List<Measurement> list) {
        CurrentViewHolder currentViewHolder = (CurrentViewHolder) viewHolder;
        Measurement measurement = list.get(list.size()-1);
        currentViewHolder.current.setText(measurement.getValue() + " °C");
        currentViewHolder.title.setText("Current temperature");
    }

    @Override
    public void OnTaskCompletedAverage(ViewHolder viewHolder, List<Measurement> list) {
        AverageViewHolder averageViewHolder = (AverageViewHolder) viewHolder;
        float sum = 0;
        for(Measurement sensor : list){
            sum += Float.parseFloat(sensor.getValue());
        }
        averageViewHolder.avg.setText(Float.toString(sum/list.size()) + " °C");
        averageViewHolder.title.setText("Average temperature");

    }

    @Override
    public void OnTaskCompletedGraph(ViewHolder viewHolder, List<Measurement> list) {
        GraphViewHolder gViewHolder = (GraphViewHolder) viewHolder;

        List<Entry> entries = new ArrayList<>();
        int br=0;

        ArrayList<Entry> values = new ArrayList<Entry>();


        for (Measurement m : list) {

            entries.add(new Entry(br, Float.parseFloat(m.getValue())));
            br++;
        }



        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(entries, "DataSet 1");

        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setFillAlpha(110);
        set1.setColor(ContextCompat.getColor(contextTemperature,R.color.graph1));
        set1.setCircleColor(ContextCompat.getColor(contextTemperature,R.color.graph1));
        set1.setLineWidth(3f);
        set1.setCircleRadius(5f);
        set1.setDrawCircleHole(true);
        set1.setValueTextSize(15f);


        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData( dataSets);

        // set data
        gViewHolder.lineChart.setData(data);



        Description desc = new Description();
        desc.setText("");

        gViewHolder.lineChart.setDescription(desc);
        gViewHolder.lineChart.getLegend().setEnabled(false);
        gViewHolder.lineChart.setScaleYEnabled(false);
        gViewHolder.lineChart.setDoubleTapToZoomEnabled(true);
        gViewHolder.lineChart.setBorderColor(ContextCompat.getColor(contextTemperature,R.color.graph1));
        gViewHolder.lineChart.setGridBackgroundColor(ContextCompat.getColor(contextTemperature,R.color.background));
        gViewHolder.lineChart.invalidate();

    }

    @Override
    public void OnTaskCompletedSettings(ViewHolder viewHolder, List<Measurement> list) {
        SettingsViewHolder settingsViewHolder = (SettingsViewHolder) viewHolder;
        List<String> dataset = new LinkedList<>();
        tempSensors = new HashMap<>();
        for(Measurement measurement: list){
            dataset.add(measurement.getLocation());
            tempSensors.put(measurement.getLocation(),measurement);
        }
        Data sensorData = new Data(tempSensors,false);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(contextTemperature, R.layout.custom_spinner_item, dataset);
        settingsViewHolder.sensorSpinner.setAdapter(adapter);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class SettingsViewHolder extends TempTabAdapter.ViewHolder {
        Spinner sensorSpinner;
        Spinner periodSpinner;

        public SettingsViewHolder(View v) {
            super(v);
            this.sensorSpinner = (Spinner) v.findViewById(R.id.sensor_spinner);
            this.periodSpinner = (Spinner) v.findViewById(R.id.period_spinner);
        }
    }

    public class CurrentViewHolder extends TempTabAdapter.ViewHolder {
        TextView current;
        TextView title;

        public CurrentViewHolder(View v) {
            super(v);
            this.current = (TextView) v.findViewById(R.id.inside_value);
            this.title = (TextView) v.findViewById(R.id.current_title);
        }
    }

    public class AverageViewHolder extends TempTabAdapter.ViewHolder {
        TextView avg;
        TextView title;

        public AverageViewHolder(View v) {
            super(v);
            this.avg = (TextView) v.findViewById(R.id.value);
            this.title=(TextView) v.findViewById(R.id.title);
        }
    }

    public class GraphViewHolder extends TempTabAdapter.ViewHolder {
        TextView title;
        LineChart lineChart;
        public GraphViewHolder(View v) {
            super(v);
            this.lineChart = (LineChart) v.findViewById(R.id.line_chart);
            this.title = (TextView) v.findViewById(R.id.current_title);
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
        } else if (viewType == SETTINGS) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.settings_card, viewGroup, false);
            return new SettingsViewHolder(v);
        } else {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.temperature_graph, viewGroup, false);
            return new GraphViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case SETTINGS:
                SettingsViewHolder settingsViewHolder = (SettingsViewHolder) viewHolder;

                yourPreference = YourPreference.getInstance(contextTemperature);
                    new SpinnerParserTemperature(viewHolder, this, contextTemperature).execute(yourPreference.getData("URL")  + "api/sensors/temperature");


                List<String> dataset = new LinkedList<>(Arrays.asList("Day", "Week", "Month"));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(contextTemperature, R.layout.custom_spinner_item, dataset);
                settingsViewHolder.periodSpinner.setAdapter(adapter);

                try{
                    currentPeriodTemperature = settingsViewHolder.periodSpinner.getSelectedItem().toString();
                    currentSensorTemperature = settingsViewHolder.sensorSpinner.getSelectedItem().toString();
                } catch (NullPointerException e) {
                    //do nothing
                }

                settingsViewHolder.periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        String selectedPeriod = parent.getItemAtPosition(position).toString().toLowerCase();

                        currentPeriodTemperature = selectedPeriod;
                        if (brojacTemperature !=0) {
                            cardNumTemperature = 4;
                            brojacTemperature++;
                        }


                        notifyItemChanged(1);
                        notifyItemChanged(2);
                        notifyItemChanged(3);

                    } // to close the onItemSelected
                    public void onNothingSelected(AdapterView<?> parent)
                    {

                    }
                });

                settingsViewHolder.sensorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        String selectedSensor = parent.getItemAtPosition(position).toString();
                        currentSensorTemperature = selectedSensor;
                        if (brojacTemperature !=0) {
                            cardNumTemperature = 4;
                        }
                        brojacTemperature++;

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
                Data sensorsData = new Data();
                yourPreference = YourPreference.getInstance(contextTemperature);
                new DataTaskTemperature(viewHolder, this).execute(yourPreference.getData("URL")  + "api/sensors/temperature/" + (sensorsData.getSensorsTemp().get(currentSensorTemperature).getSensorID()) + "/" + "day");
                break;
            case AVERAGE:
                sensorsData = new Data();
                yourPreference = YourPreference.getInstance(contextTemperature);
                new DataTaskTemperature(viewHolder,this).execute(yourPreference.getData("URL")  + "api/sensors/temperature/" + (sensorsData.getSensorsTemp().get(currentSensorTemperature).getSensorID()) + "/" + currentPeriodTemperature);
                break;
            case GRAPH:
                sensorsData = new Data();
                yourPreference = YourPreference.getInstance(contextTemperature);
                new DataTaskTemperature(viewHolder,this).execute(yourPreference.getData("URL")  + "api/sensors/temperature/" + (sensorsData.getSensorsTemp().get(currentSensorTemperature).getSensorID()) + "/" + currentPeriodTemperature);

        }
    }
    @Override
    public int getItemCount() {
        return cardNumTemperature;
    }

    @Override
    public int getItemViewType(int position) {
        int mDataSetTypes[] = {SETTINGS, GRAPH, CURRENT, AVERAGE};
        return mDataSetTypes[position];
    }
}
