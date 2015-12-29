package pl.lukmarr.pokemontrainer.entities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import io.realm.Realm;
import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.adapters.DrawerAdapter;
import pl.lukmarr.pokemontrainer.config.Config;
import pl.lukmarr.pokemontrainer.connection.DataFetcher;
import pl.lukmarr.pokemontrainer.connection.UIAction;
import pl.lukmarr.pokemontrainer.connection.UIError;
import pl.lukmarr.pokemontrainer.database.RealmPoke;
import pl.lukmarr.pokemontrainer.database.RealmPosition;
import pl.lukmarr.pokemontrainer.entities.fragments.GridViewFragment;
import pl.lukmarr.pokemontrainer.utils.FirstTimeSetup;
import pl.lukmarr.pokemontrainer.utils.interfaces.EnvironmentConnector;
import pl.lukmarr.pokemontrainer.utils.interfaces.ListCallback;
import pl.lukmarr.pokemontrainer.utils.interfaces.PokemonRefreshable;

public class MainActivity extends SmartCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    public PokemonRefreshable pokemonRefreshable;
    public EnvironmentConnector environmentConnector;
    public DataFetcher dataFetcher;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.progressive)
    ProgressBar progressView;
    @Bind(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getAppData().setApiKey("c9ad5f56-8d2b-4c01-9d30-ad6aa477e35c");
        handleNoInternetConnection();
        initDrawer();
        switchTo(0);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.content, PokedexFragment.newInstance()).commitAllowingStateLoss();
        FirstTimeSetup.setup(this);
    }



    void handleNoInternetConnection() {
        if (Config.isNetworkAvailable(this)) {
            fetchPokes();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("NO Internet Connection")
                    .setNegativeButton("Close App", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 100);
                    dialog.dismiss();
                }
            }).show();
        }
    }


    void fetchPokes() {
        Realm realm = Realm.getInstance(this);
        final List<RealmPoke> pokes = realm.where(RealmPoke.class).findAll();
        if (pokes == null || pokes.size() == 0) {

            List<Integer> ids = new ArrayList<>();
            for (int j = 1; j < 31; j++) {
                ids.add(j);
            }
            dataFetcher = new DataFetcher();
            dataFetcher.onError = new UIError(this);
            dataFetcher.onSubscribe = new UIAction(this, progressView, true);
            dataFetcher.onCompleted = new UIAction(this, progressView, false);
            dataFetcher.fetchPokes(MainActivity.this, ids, new ListCallback<RealmPoke>() {
                @Override
                public void onListReceived(List<RealmPoke> list) {
                    Log.i(TAG, "pokemons received!\n size = " + list.size());
                    pokemonRefreshable.refreshPokes(pokes);
                }
            });
        }
    }

    private void initDrawer() {
        Log.d(TAG, "initDrawer() called with: " + "");

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, android.R.string.ok,
                android.R.string.no) {
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
        toggle.setDrawerIndicatorEnabled(false);
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

    /**
     * @param j 0,1,2,3
     */
    void switchTo(int j) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, getFragment(j))
                .commitAllowingStateLoss();
    }

    Fragment getFragment(int j) {
        Config.currentFragmentId = j;
        switch (j) {
            case 0:
                MapFragment mapFragment = MapFragment.newInstance();
                this.pokemonRefreshable = mapFragment;
                return mapFragment;
            case 1:
                PokedexFragment fragment = PokedexFragment.newInstance();
                this.pokemonRefreshable = fragment;
                return fragment;
            case 2:
                return GridViewFragment.newInstance();
            case 3:
            default:
                return new InfoFragment();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && Config.isNetworkAvailable(this)) {
            fetchPokes();
        } else {
            Toast.makeText(this, "No internet connection. Closing app.", Toast.LENGTH_LONG).show();
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 700);
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
        if (environmentConnector != null) environmentConnector.onAddressChange(address);
    }

    @Override
    protected void smartLatLngChange(LocationEntry location) {
        super.smartLatLngChange(location);
        Log.d(TAG, "smartLatLngChange " + location);
        updatePosition(location.getLatitude(), location.getLongitude());
    }

    private void updatePosition(double latitude, double longitude) {
        Realm realm = Realm.getInstance(this);
        realm.beginTransaction();
        try {
            RealmPosition pos = new RealmPosition();
            pos.setPosition("POSITION");
            pos.setTimestamp(System.currentTimeMillis());
            pos.setLat(latitude);
            pos.setLon(longitude);

            realm.copyToRealmOrUpdate(pos);
        } catch (Exception e) {
            realm.cancelTransaction();
        } finally {
            realm.commitTransaction();
        }
    }

    @Override
    protected void smartWeatherChange(WeatherEntry weather) {
        super.smartWeatherChange(weather);
        Log.d(TAG, "smartWeatherChange " + weather);
        if (environmentConnector != null) environmentConnector.onWeatherChange(weather);
    }
}
