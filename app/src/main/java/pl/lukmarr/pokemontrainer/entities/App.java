package pl.lukmarr.pokemontrainer.entities;

import android.util.Log;

import com.trnql.zen.core.AppData;

/**
 * Created by Łukasz Marczak
 *
 * @since 25.12.15
 */
public class App extends AppData {
    public static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, "onLowMemory ");
        try {
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception x) {
            Log.e(TAG, "onLowMemory " + x.getMessage());
            x.printStackTrace();
        }
    }
}
