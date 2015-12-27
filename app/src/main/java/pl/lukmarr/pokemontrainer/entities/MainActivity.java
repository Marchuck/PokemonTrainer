package pl.lukmarr.pokemontrainer.entities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.trnql.smart.activity.ActivityEntry;
import com.trnql.smart.base.SmartCompatActivity;
import com.trnql.smart.location.AddressEntry;
import com.trnql.smart.location.LocationEntry;
import com.trnql.smart.people.PersonEntry;
import com.trnql.smart.places.PlaceEntry;
import com.trnql.smart.weather.WeatherEntry;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.adapters.DrawerAdapter;
import pl.lukmarr.pokemontrainer.connection.DataFetcher;
import pl.lukmarr.pokemontrainer.utils.MapUtils;

public class MainActivity extends SmartCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    public DataFetcher dataFetcher;
    public LatLng lastLatLng;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getAppData().setApiKey("c9ad5f56-8d2b-4c01-9d30-ad6aa477e35c");
        fetchPokes();
        initDrawer();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, PokedexFragment.newInstance()).commitAllowingStateLoss();

        startLocationWatcher();

    }

    void startLocationWatcher() {

    }

    void fetchPokes() {
        List<Integer> ids = new ArrayList<>();
        for (int j = 1; j < 31; j++) {
            ids.add(j);
        }
        dataFetcher = new DataFetcher();
        dataFetcher.fetch(MainActivity.this, ids);
    }

    private void initDrawer() {
        Log.d(TAG, "initDrawer() called with: " + "");

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, android.R.string.ok, android.R.string.no) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        }; // drawerLayout Toggle Object Made
        drawerLayout.setDrawerListener(toggle); // drawerLayout Listener set to the drawerLayout toggle
        toggle.syncState();
        drawerLayout.closeDrawer(Gravity.LEFT);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(DrawerAdapter.create(this, new DrawerAdapter.Listener() {
            @Override
            public void onClicked(int j) {
                if (j == 4) {
                    new AlertDialog.Builder(MainActivity.this).setTitle("Are you sure to quit?")
                            .setMessage("Are you sure to quit?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
                    return;
                }
                switchTo(j);
                drawerLayout.closeDrawers();
            }
        }));
    }

    void switchTo(int j) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, getFragment(j))
                .commitAllowingStateLoss();
    }

    Fragment getFragment(int j) {
        switch (j) {
            case 0:
                return MapFragment.newInstance();
            case 1:
                return PokedexFragment.newInstance();
            case 2:
                return BadgesFragment.newInstance();
            case 3:
            default:
                return new InfoFragment();
        }
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
