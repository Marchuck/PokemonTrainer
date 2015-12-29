package pl.lukmarr.pokemontrainer.entities.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.adapters.DrawerAdapter;
import pl.lukmarr.pokemontrainer.config.Config;
import pl.lukmarr.pokemontrainer.connection.DataFetcher;
import pl.lukmarr.pokemontrainer.connection.UIAction;
import pl.lukmarr.pokemontrainer.connection.UIError;
import pl.lukmarr.pokemontrainer.database.RealmPoke;
import pl.lukmarr.pokemontrainer.database.RealmPosition;
import pl.lukmarr.pokemontrainer.entities.fragments.AboutFragment;
import pl.lukmarr.pokemontrainer.entities.fragments.BadgesFragment;
import pl.lukmarr.pokemontrainer.entities.fragments.DetailsFragment;
import pl.lukmarr.pokemontrainer.entities.fragments.MapFragment;
import pl.lukmarr.pokemontrainer.entities.fragments.PokedexFragment;
import pl.lukmarr.pokemontrainer.entities.fragments.TrainersFragment;
import pl.lukmarr.pokemontrainer.entities.fragments.WaitFragment;
import pl.lukmarr.pokemontrainer.utils.FirstTimeSetup;
import pl.lukmarr.pokemontrainer.utils.LocationHelper;
import pl.lukmarr.pokemontrainer.utils.interfaces.EnvironmentConnector;
import pl.lukmarr.pokemontrainer.utils.interfaces.ListCallback;
import pl.lukmarr.pokemontrainer.utils.interfaces.PersonConnector;
import pl.lukmarr.pokemontrainer.utils.interfaces.PokemonRefreshable;

public class MainActivity extends SmartCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    public PokemonRefreshable pokemonRefreshable;
    public EnvironmentConnector environmentConnector;
    public PersonConnector personConnector;
    public DataFetcher dataFetcher;
    public SupportMapFragment rightMapFragment;
    public LatLng lastLatLng;

    @Bind(R.id.fab_main)
    FloatingActionButton fabCloseDrawers;

    @OnClick(R.id.fab_main)
    public void onFabClick() {
        drawerLayout.closeDrawers();
    }

    public void openMapDrawer(final @Nullable List<PersonEntry> entries, final View view) {
        if (entries == null || entries.size() == 0) {
            if (view != null && view.isShown())
                Snackbar.make(view, "No trainers nearby!", Snackbar.LENGTH_SHORT)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                            pers
//                            PersonEntry ent = new PersonEntry();
//                            openMapDrawer(ent,view);
                            }
                        }).show();
//            return;
        }
        if (rightMapFragment == null) {
            rightMapFragment = SupportMapFragment.newInstance();
        }
        //attach mapFragment to ViewHolder
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.right_map, rightMapFragment).commitAllowingStateLoss();
        drawerLayout.openDrawer(Gravity.RIGHT);
        rightMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                googleMap.setMyLocationEnabled(true);
                int size = entries == null ? 0 : entries.size();
                double meanLat = 0;
                double meanLon = 0;
                if (size > 0)
                    for (PersonEntry personEntry : entries) {
                        double lat = personEntry.getLatitude();
                        double lon = personEntry.getLongitude();

                        LatLng position = new LatLng(lat, lon);
                        String personName = personEntry.getDataPayload();
                        googleMap.addMarker(new MarkerOptions()
                                .position(position)
                                .title(personName)
                                .snippet(personEntry.getDistanceFromUser() + " meters away")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));
                        meanLat += lat;
                        meanLon += lon;
                    }
                LatLng positionMean = null;
                if (size > 0)
                    positionMean = new LatLng(meanLat / size, meanLon / size);
                else positionMean = lastLatLng;
                if (positionMean == null) return;
                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(new CameraPosition(positionMean, 16, 60, 0)));
            }
        });
    }

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
        setupRealm();
        ButterKnife.bind(this);
        getAppData().setApiKey("c9ad5f56-8d2b-4c01-9d30-ad6aa477e35c");
        handleNoInternetConnection();
        initDrawer();
        initFab();
        switchTo(-1);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.content, PokedexFragment.newInstance()).commitAllowingStateLoss();
        FirstTimeSetup.setup(this);


    }

    public void initFab() {
        int color = getResources().getColor(R.color.accent_color);
        int whenPressedColor = getResources().getColor(R.color.accent_color_darker);
        fabCloseDrawers.setImageResource(R.drawable.arrow_right);
        fabCloseDrawers.setBackgroundTintList(ColorStateList.valueOf(color));
        fabCloseDrawers.setRippleColor(whenPressedColor);
    }

    private void setupRealm() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

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
                if (drawerView.getId() == R.id.right_map_drawer_container) {
                    Log.d(TAG, "onDrawerOpened XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                    openMapDrawer(null, drawerView);
                }
//                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (drawerView.getId() == R.id.right_map_drawer_container) {
                    Log.d(TAG, "onDrawerClosed XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
//                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        }; // drawerLayout Toggle Object Made
        drawerLayout.setDrawerListener(toggle); // drawerLayout Listener set to the drawerLayout toggle
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        drawerLayout.closeDrawer(Gravity.LEFT);
        drawerLayout.closeDrawer(Gravity.RIGHT);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(DrawerAdapter.create(this, new DrawerAdapter.Listener() {
            @Override
            public void onClicked(int j) {
                if (j == 6) {
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
            case -1:
                return WaitFragment.newInstance();
            case 0:
                MapFragment mapFragment = MapFragment.newInstance();
                this.pokemonRefreshable = mapFragment;
                return mapFragment;
            case 1:
                PokedexFragment fragment = PokedexFragment.newInstance();
                this.pokemonRefreshable = fragment;
                return fragment;
            case 2:
                return BadgesFragment.newInstance();
            case 3:
                TrainersFragment fr = TrainersFragment.newInstance();
                personConnector = fr;
                return fr;
            case 4:
                return DetailsFragment.newInstance();
            case 5:
            default:
                return AboutFragment.newInstance();
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
        if (personConnector != null) personConnector.onPersonReceived(people);
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
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else if (drawerLayout.isDrawerOpen(drawerLayout)) {
            drawerLayout.closeDrawers();
        } else if (Config.currentFragmentId != -1)
            switchTo(-1);
        super.onBackPressed();
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

    boolean wasFalse = true;

    @Override
    protected void smartLatLngChange(LocationEntry location) {
        super.smartLatLngChange(location);
        if (wasFalse) {
            unlockDrawer();
            switchTo(0);
            wasFalse = false;
        }
        Log.d(TAG, "smartLatLngChange " + location);
        lastLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        updatePosition(location.getLatitude(), location.getLongitude());

        fetchPokesSilently();

    }

    private void fetchPokesSilently() {
        Realm realm = Realm.getInstance(this);
        long realmPokesCount = realm.where(RealmPoke.class).count();
        if (realmPokesCount >= 149) {
            realm.close();
            return;
        }

        dataFetcher = new DataFetcher();
        dataFetcher.onError = new UIError(this);
        dataFetcher.onSubscribe = new UIAction(this, progressView, true);
        dataFetcher.onCompleted = new UIAction(this, progressView, false);

        List<Integer> list = new ArrayList<>();
        int maxSize = 152;
        for (int j = (int) realmPokesCount; j < maxSize && j < realmPokesCount + Config.INCREMENT_POKES; j++) {
            list.add(j - 1);
            Log.d(TAG, "fetchSilently " + (j - 1));
        }
        realm.close();

        dataFetcher.fetchPokes(this, list, null);
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
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume ");
    }

    @Override
    protected void smartWeatherChange(WeatherEntry weather) {
        super.smartWeatherChange(weather);
        Log.d(TAG, "smartWeatherChange " + weather);
        if (environmentConnector != null) environmentConnector.onWeatherChange(weather);
    }

    @Override
    protected void onDestroy() {
        LocationHelper.onDestroy(this);
        super.onDestroy();
    }

    public void lockDrawer() {
        drawerLayout.closeDrawers();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void unlockDrawer() {
        drawerLayout.closeDrawers();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

}
