package pl.lukmarr.pokemontrainer.entities;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.trnql.smart.activity.ActivityEntry;
import com.trnql.smart.base.SmartCompatActivity;
import com.trnql.smart.location.AddressEntry;
import com.trnql.smart.location.LocationEntry;
import com.trnql.smart.people.PersonEntry;
import com.trnql.smart.places.PlaceEntry;
import com.trnql.smart.weather.WeatherEntry;

import java.util.List;

import butterknife.ButterKnife;
import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.utils.MapUtils;

public class MainActivity extends SmartCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    public LatLng lastLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getAppData().setApiKey("c9ad5f56-8d2b-4c01-9d30-ad6aa477e35c");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, MapFragment.newInstance()).commitAllowingStateLoss();


    }

    @Override
    protected void smartPeopleChange(List<PersonEntry> people) {
        super.smartPeopleChange(people);
        Log.d(TAG, "smartPeopleChange");
        for (PersonEntry entry : people) {
            Log.d(TAG, "- " + entry);
        }
    }

    @Override
    protected void smartPlacesChange(List<PlaceEntry> places) {
        super.smartPlacesChange(places);
        Log.d(TAG, "smartPlacesChange ");
        for (PlaceEntry entry : places) {
            Log.d(TAG, "- " + entry);
        }
    }

    @Override
    protected void smartActivityChange(ActivityEntry userActivity) {
        super.smartActivityChange(userActivity);
        Log.d(TAG, "smartActivityChange " + userActivity);
    }

    @Override
    protected void smartAddressChange(AddressEntry address) {
        super.smartAddressChange(address);
        Log.d(TAG, "smartAddressChange " + address);
    }

    @Override
    protected void smartLatLngChange(LocationEntry location) {
        super.smartLatLngChange(location);
        Log.d(TAG, "smartLatLngChange " + location);
        this.lastLatLng = MapUtils.asGoogleLatLng(location.getLatLng());
    }


    @Override
    protected void smartIsHighAccuracy(boolean isHighAcc) {
        super.smartIsHighAccuracy(isHighAcc);
        Log.d(TAG, "smartIsHighAccuracy " + isHighAcc);
    }

    @Override
    protected void smartWeatherChange(WeatherEntry weather) {
        super.smartWeatherChange(weather);
        Log.d(TAG, "smartWeatherChange " + weather);
    }
}
