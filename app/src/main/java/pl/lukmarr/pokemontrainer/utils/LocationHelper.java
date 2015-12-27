package pl.lukmarr.pokemontrainer.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.lukmarr.pokemontrainer.config.Config;


/**
 * Created by Łukasz Marczak
 *
 * @since 26.11.15.
 */
public final class LocationHelper {

    public static final String TAG = LocationHelper.class.getSimpleName();

    private LocationManager locationManager;
    private GpsStatus.Listener gpsStatusListener;
    //    private Toast toast;

    private void buildListeners() {

        gpsStatusListener = new GpsStatus.Listener() {
            @Override
            public void onGpsStatusChanged(int event) {
                Log.d(TAG, "onGpsStatusChanged: event code: " + event);
            }
        };

    }

    public LocationHelper(Activity activity, List<Pair<LatLng, Integer>> pokesNearby) {

        buildListeners();
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        Boolean gpsProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Boolean networkProviderEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Boolean passiveProviderEnabled = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

        Log.d(TAG, "is gpsProviderEnabled: " + isEnabled(gpsProviderEnabled));
        Log.d(TAG, "is networkProviderEnabled: " + isEnabled(networkProviderEnabled));
        Log.d(TAG, "is passiveProviderEnabled: " + isEnabled(passiveProviderEnabled));

        for (String provider : locationManager.getAllProviders())
            Log.d(TAG, "provider: " + provider);


//        for (String provider : locationManager.getAllProviders()) {
//            locationManager.requestLocationUpdates(provider, 0, 0,
//                    locationListener(provider), Looper.getMainLooper());
//        }
        //uncomment to make intentReceiver available 20 seconds after launch

        Intent intent = new Intent(PROX_ALERT_INTENT);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(activity, 0, intent, 0);
        try {
            int size = pokesNearby.size();
            for (int j = 0; j < size; j++) {

                Pair<LatLng, Integer> pokeNearby = pokesNearby.get(j);
                double lat = pokeNearby.first.latitude;
                double lon = pokeNearby.first.longitude;
                int pokemonId = pokeNearby.second;

                locationManager.addProximityAlert(lat, lon, Config.RADIUS_IN_METERS,
                        getExpirationTime(), proximityIntent);
                IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
                activity.registerReceiver(new ProximityIntentReceiver(pokemonId), filter);
            }
        } catch (Exception c) {
            Log.e(TAG, "LocationHelper " + c.getMessage());
            c.printStackTrace();
        }
    }

    public static long getExpirationTime() {
        Date date = Calendar.getInstance().getTime();
//        date.setMonth(Calendar.APRIL);
        date.setYear(2050);
        return date.getTime();
    }

    public static final String PROX_ALERT_INTENT = "XDXDXD";

    private LocationListener locationListener(final String provider) {
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String shortenLatitude = String.format("%.3f", location.getLatitude());
                String shortenLongitude = String.format("%.3f", location.getLongitude());
                Log.d(TAG + ":" + provider, "onLocationChanged(" + shortenLatitude + ", " + shortenLongitude + ")");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d(TAG + ":" + provider, "onStatusChanged: " + provider + ", status: " + status);
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d(TAG + ":" + provider, "onProviderEnabled: " + provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d(TAG + ":" + provider, "onProviderDisabled: " + provider);
            }
        };
    }

    public void removeCallbacks() {
        locationManager.removeGpsStatusListener(gpsStatusListener);
    }

    private String isEnabled(Boolean gpsProviderEnabled) {
        return gpsProviderEnabled ? "enabled" : "disabled";
    }
}
