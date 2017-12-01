package projekt.tel.fer.smarthome;


import java.util.List;

/**
 * Created by Lovro on 7.1.2017..
 */

public interface OnTaskCompletedHumidity {
    void OnTaskCompletedCurrent(HumTabAdapter.ViewHolder viewHolder, List<Measurement> list);
    void OnTaskCompletedAverage(HumTabAdapter.ViewHolder viewHolder, List<Measurement> list);
    void OnTaskCompletedGraph(HumTabAdapter.ViewHolder viewHolder, List<Measurement> list);
    void OnTaskCompletedSettings(HumTabAdapter.ViewHolder viewHolder, List<Measurement> list);

}
