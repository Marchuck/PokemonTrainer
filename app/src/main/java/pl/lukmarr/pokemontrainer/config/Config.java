package pl.lukmarr.pokemontrainer.config;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Łukasz Marczak
 *
 * @since 25.12.15
 */
public class Config {
    public static final String rxJavaError = "rxJavaError";

    public static float RADIUS_IN_METERS = 20f;
    public static boolean wasEmpty = true;

    public static int INCREMENT_POKES = 7;
    public static int currentFragmentId = -1;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
