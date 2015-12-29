package pl.lukmarr.pokemontrainer.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Date;

import pl.lukmarr.pokemontrainer.config.Config;


/**
 * Created by Łukasz Marczak
 *
 * @since 26.11.15.
 */
public final class LocationHelper {
    private static LocationHelper INSTANCE;
    public static final String TAG = LocationHelper.class.getSimpleName();
    Activity activity;
    private LocationManager locationManager;
    private GpsStatus.Listener gpsStatusListener;
    //    private Toast toast;


    public static LocationHelper getInstance(Activity activity) {
        if (INSTANCE == null)
            INSTANCE = new LocationHelper(activity);
        return INSTANCE;
    }

    private LocationHelper(Activity activity) {
        this.activity = activity;
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    public void registerNewPokemonWatcher(LatLng latLng, int pokemonId) {
        Intent intent = new Intent(PROX_ALERT_INTENT);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(activity, 0, intent, 0);
        try {
            double lat = latLng.latitude;
            double lon = latLng.longitude;

            locationManager.addProximityAlert(lat, lon, Config.RADIUS_IN_METERS,
                    getExpirationTime(), proximityIntent);
            IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
            activity.registerReceiver(new ProximityIntentReceiver(pokemonId), filter);
        } catch (Exception c) {
            Log.e(TAG, "LocationHelper " + c.getMessage());
            c.printStackTrace();
        }

    }

    public static long getExpirationTime() {
        Date date = Calendar.getInstance().getTime();
        date.setYear(2050);
        return date.getTime();
    }

    public static final String PROX_ALERT_INTENT = "XDXDXD";
}
