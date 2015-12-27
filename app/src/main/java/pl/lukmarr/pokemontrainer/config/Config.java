package pl.lukmarr.pokemontrainer.config;

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
    public static int DETAILS_FRAGMENT = 2;

    public static void registerNewPokemon(LatLng poke, int id) {
        pokesNearby.add(new Pair<LatLng, Integer>(poke, id));
    }

}
