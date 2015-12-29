package pl.lukmarr.pokemontrainer.utils;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;
import pl.lukmarr.pokemontrainer.database.RealmPoke;

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
            double radius = random.nextInt(100);
            radius /= 8000;
            int angle = random.nextInt(360);

            double lat = latLng.latitude + radius * Math.sin(Math.toRadians(angle));
            double lon = latLng.longitude + radius * Math.cos(Math.toRadians(angle));
//            double lon = latLng.longitude + offset();
            pairs.add(new LatLng(lat, lon));
        }
        return pairs;

    }

    public List<LatLng> getPokesNearby2(LatLng latLng, int count) {
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
        d = (random.nextBoolean() ? -d : d) / 7;
        return d;
    }

    public static RandUtils create() {
        return new RandUtils();
    }

    public int randomPoke() {
        return 1 + random.nextInt(150);
    }

    public int randomPokeButUnique(Context context) {
        Realm realm = Realm.getInstance(context);
        RealmResults<RealmPoke> pokes = realm.where(RealmPoke.class).equalTo("isDiscovered", false).findAll();
        if (pokes == null || pokes.size() == 0) return -1;
        List<Integer> indexes = new ArrayList<>();
        for (int j = 0; j < pokes.size(); j++) {
            RealmPoke poke = pokes.get(j);
            indexes.add(poke.getId());
        }
        int randomIndex = random.nextInt(indexes.size());
        realm.close();
        return indexes.get(randomIndex);
    }
}
