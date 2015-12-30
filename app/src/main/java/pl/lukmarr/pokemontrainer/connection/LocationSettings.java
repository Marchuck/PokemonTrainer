package pl.lukmarr.pokemontrainer.connection;

import android.content.IntentSender;
import android.util.Log;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.trnql.smart.base.SmartActivity;
import com.trnql.smart.base.SmartCompatActivity;

import static android.util.Log.d;

/**
 * Created by Łukasz Marczak
 *
 * @since 30.12.15
 */
public class LocationSettings {
    public static final String TAG = LocationSettings.class.getSimpleName();
    protected LocationSettingsRequest locationSettingsRequest;

    private LocationSettings() {
    }

    public static void launch(SmartCompatActivity context) {
        d(TAG, "inside launch ");
//        context.getAppData().startLocationService();
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
//        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }

    protected void checkLocationSettings(final SmartActivity activity) {
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(
                null/*googleApiClient*/, locationSettingsRequest);
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                                "upgrade location settings ");
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(activity, 0x1);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                                "not created.");
                        break;
                }
            }
        });
    }
}
