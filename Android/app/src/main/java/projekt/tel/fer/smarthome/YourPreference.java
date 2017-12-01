package projekt.tel.fer.smarthome;
import android.content.SharedPreferences;
import android.content.Context;
/**
 * Created by Neven on 20.1.2017..
 */
public class YourPreference {
    private static YourPreference yourPreference;
    private SharedPreferences sharedPreferences;
    private static final String defaultURL = "http://iotat.tel.fer.hr:59984/";

    public static YourPreference getInstance(Context context) {
        if (yourPreference == null) {
            yourPreference = new YourPreference(context);
        }
        return yourPreference;
    }

    private YourPreference(Context context) {
        sharedPreferences = context.getSharedPreferences("Urls",Context.MODE_PRIVATE);
    }

    public void saveData(String key,String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public String getData(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(key, defaultURL );
        }
        return defaultURL ;
    }
}