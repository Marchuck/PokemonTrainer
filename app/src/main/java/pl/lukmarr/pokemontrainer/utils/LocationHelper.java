package pl.lukmarr.pokemontrainer.utils;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import io.realm.Realm;
import io.realm.RealmResults;
import pl.lukmarr.pokemontrainer.config.Config;
import pl.lukmarr.pokemontrainer.database.OutdoorPoke;
import pl.lukmarr.pokemontrainer.database.RealmPoke;


/**
 * Created by Łukasz Marczak
 *
 * @since 26.11.15.
 */
public final class LocationHelper {

    public static final String TAG = LocationHelper.class.getSimpleName();

    public static float distanceBetweenLatLngs(LatLng latLng1, LatLng latLng2) {
        Location loc1 = new Location("");
        loc1.setLatitude(latLng1.latitude);
        loc1.setLongitude(latLng1.longitude);

        Location loc2 = new Location("");
        loc2.setLatitude(latLng2.latitude);
        loc2.setLongitude(latLng2.longitude);

        return loc1.distanceTo(loc2);
    }

    public static void checkPokesNearby(final Context context, LatLng lastLatLng) {
        Realm realm = Realm.getInstance(context);
        RealmResults<OutdoorPoke> outdoorPokes = realm.where(OutdoorPoke.class)
                .equalTo("poke.isDiscovered", false).findAll();
        int pokemonId = -1;
        float meters = 10000;
        for (int j = 0; j < outdoorPokes.size(); j++) {
            OutdoorPoke outdoorPoke = outdoorPokes.get(j);
            LatLng position = new LatLng(outdoorPoke.getLat(), outdoorPoke.getLon());
            float distance = distanceBetweenLatLngs(lastLatLng, position);
            meters = distance < meters ? distance : meters;
            if (distance < Config.RADIUS_IN_METERS) {
                pokemonId = outdoorPoke.getPoke().getId();
                realm.beginTransaction();
                RealmPoke realmPoke = outdoorPoke.getPoke();
                realmPoke.setIsDiscovered(true);
                outdoorPoke.setPoke(realmPoke);
                realm.commitTransaction();
                realm.close();
                break;
            }
        }
        if (pokemonId == -1) {
            Log.d(TAG, "no pokes nearby. Closest pokemon is " + meters + " far");
        } else {
            final int pokemonIdAsFinal = pokemonId;
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    IntentBuilder.NewPokemonActivityBuilder(context, pokemonIdAsFinal);
                }
            }, 300);
        }
    }
}
