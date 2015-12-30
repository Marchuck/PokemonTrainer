package pl.lukmarr.pokemontrainer.config;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.util.Pair;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Łukasz Marczak
 *
 * @since 25.12.15
 */
public class Config {
    public static final String rxJavaError = "rxJavaError";

    public static float RADIUS_IN_METERS = 20f;
    public static List<Pair<LatLng, Integer>> pokesNearby = new ArrayList<>();
    public static boolean wasEmpty = true;

    public static int POKEDEX_FRAGMENT = 1;
    public static int DETAILS_FRAGMENT = 3;

    public static int INCREMENT_POKES = 7;
    public static int currentFragmentId = -1;
    public static int transparentLayoutHeight = 200;
    public static String currentUser = "RealmUser";

    public static void registerNewPokemon(LatLng poke, int id) {
        pokesNearby.add(new Pair<>(poke, id));
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
