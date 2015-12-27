package pl.lukmarr.pokemontrainer.utils;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Łukasz Marczak
 *
 * @since 26.12.15
 */
public class RandUtils {
    public static final String TAG = RandUtils.class.getSimpleName();
    Random random = new Random();

    private RandUtils() {
    }

    public List<LatLng> getPokesNearby(LatLng latLng, int count) {
        List<LatLng> pairs = new ArrayList<>();

        for (int j = 0; j < count; j++) {
            double lat = latLng.latitude + offset();
            double lon = latLng.longitude + offset();
            pairs.add(new LatLng(lat, lon));
        }
        return pairs;
    }

    private double offset() {
        double d = random.nextDouble();
        while (d > 0.01) {
            d /= 2;
        }
        d = (random.nextBoolean() ? -d : d);
        return d;
    }

    public static RandUtils create() {
        return new RandUtils();
    }
}
