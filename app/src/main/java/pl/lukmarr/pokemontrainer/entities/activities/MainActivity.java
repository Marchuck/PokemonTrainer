package pl.lukmarr.pokemontrainer.entities.activities;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
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
import io.realm.Realm;
import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.adapters.DrawerAdapter;
import pl.lukmarr.pokemontrainer.config.Config;
import pl.lukmarr.pokemontrainer.connection.DataFetcher;
import pl.lukmarr.pokemontrainer.connection.LocationSettings;
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
import pl.lukmarr.pokemontrainer.utils.general.RealmUtils;
import pl.lukmarr.pokemontrainer.utils.interfaces.DrawerConnector;
import pl.lukmarr.pokemontrainer.utils.interfaces.ListCallback;
import pl.lukmarr.pokemontrainer.utils.interfaces.PokemonRefreshable;

public class MainActivity extends SmartCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    public PokemonRefreshable pokemonRefreshable;
    public DrawerConnector drawerConnector;
    public DataFetcher dataFetcher;
    public SupportMapFragment rightMapFragment;
    public LatLng lastLatLng;
    public WeatherEntry weather = null;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.greetTextView)
    TextView greetTextView;

    @Bind(R.id.howMuchPokes)
    TextView discoveredPokesMessage;
//
//    @Bind(R.id.right_map_drawer_container)
//    RelativeLayout rightDrawerContainer;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.progressive)
    ProgressBar progressView;
    @Bind(R.id.toolbar)
    public android.support.v7.widget.Toolbar toolbar;

    public ActionBarDrawerToggle toggle;
    public List<PersonEntry> lastPeople = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAppData().startAllServices();
        RealmUtils.setupRealm(this);
        ButterKnife.bind(this);
        getAppData().setApiKey("c9ad5f56-8d2b-4c01-9d30-ad6aa477e35c");
        FirstTimeSetup.handleNoInternetConnection(this);
        initDrawer();
        switchTo(-1);
        FirstTimeSetup.setup(this);
        LocationSettings.launch(this);
        invalidateGreetMessage();
    }

    public void invalidateGreetMessage() {
        String greetMessage =
                String.format(getResources().getString(R.string.welcome_message), RealmUtils.getUserName(this));
        String pokeStatus =
                String.format(getResources().getString(R.string.pokes_discovered), RealmUtils.currentPokesCount(this));
        greetTextView.setText(greetMessage);
        discoveredPokesMessage.setText(pokeStatus);
    }

    private void initDrawer() {
        Log.d(TAG, "initDrawer() called with: " + "");

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, android.R.string.ok,
                android.R.string.no) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d(TAG, "onDrawerOpened " + drawerView.getId());
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.setDrawerListener(toggle); // drawerLayout Listener set to the drawerLayout toggle
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        drawerLayout.closeDrawer(Gravity.LEFT);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(DrawerAdapter.create(this, new DrawerAdapter.Listener() {
            @Override
            public void onClicked(int j) {
                if (j == 6) {
                    new AlertDialog.Builder(MainActivity.this)
 //                           .setTitle("Are you sure to quit?")
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
    public void switchTo(int j) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, getFragment(j))
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
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

                return fr;
            case 4:
                return DetailsFragment.newInstance();
            case 5:
            default:
                return AboutFragment.newInstance();
        }
    }

    public void fetchPokes() {
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
        lastPeople = people;
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
            Log.d(TAG, "closing left drawer");
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else if (drawerConnector != null && drawerConnector.isDrawerOpened()) {
            Log.d(TAG, "closing right drawer");
            drawerConnector.closeDrawer();
        } else if (Config.currentFragmentId != -1) {
            Log.d(TAG, "switch to -1");
            switchTo(-1);
        } else {
            Log.d(TAG, "onBackPressed ");
            super.onBackPressed();
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
//        if (environmentConnector != null) environmentConnector.onAddressChange(address);
    }

    boolean wasFalse = true;

    @Override
    protected void smartLatLngChange(LocationEntry location) {
        super.smartLatLngChange(location);

        Log.d(TAG, "smartLatLngChange " + location);
        lastLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        updatePosition(location.getLatitude(), location.getLongitude());
        if (wasFalse) {
            unlockDrawer();
            switchTo(0);
            wasFalse = false;
        }
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
        invalidateGreetMessage();
    }

    @Override
    protected void smartWeatherChange(WeatherEntry weather) {
        super.smartWeatherChange(weather);
        Log.d(TAG, "smartWeatherChange " + weather);
//        if (environmentConnector != null) environmentConnector.onWeatherChange(weather);
        this.weather = weather;
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

    public void refreshLeftDrawer() {
        invalidateGreetMessage();
    }
}
