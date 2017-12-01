package projekt.tel.fer.smarthome;

import java.util.List;

/**
 * Created by Jakov on 10/01/2017.
 */

public interface OnTaskCompletedTemperature {
    void OnTaskCompletedCurrent(TempTabAdapter.ViewHolder viewHolder, List<Measurement> list);
    void OnTaskCompletedAverage(TempTabAdapter.ViewHolder viewHolder, List<Measurement> list);
    void OnTaskCompletedGraph(TempTabAdapter.ViewHolder viewHolder, List<Measurement> list);
    void OnTaskCompletedSettings(TempTabAdapter.ViewHolder viewHolder, List<Measurement> list);
}
