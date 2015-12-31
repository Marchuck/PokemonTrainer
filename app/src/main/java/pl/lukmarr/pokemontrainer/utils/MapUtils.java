package pl.lukmarr.pokemontrainer.utils;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;
import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.config.Config;
import pl.lukmarr.pokemontrainer.connection.PicassoMarker;
import pl.lukmarr.pokemontrainer.connection.PokeSpritesManager;
import pl.lukmarr.pokemontrainer.database.OutdoorPoke;
import pl.lukmarr.pokemontrainer.database.RealmPoke;

/**
 * Created by Łukasz Marczak
 *
 * @since 25.12.15
 */
public class MapUtils {
    public static final String TAG = MapUtils.class.getSimpleName();
    public SupportMapFragment mapFragment;
    public GoogleMap tripGoogleMap;
    public FragmentActivity activity;
    public RelativeLayout relativeLayout;
    public LatLng lastPosition;
    public List<OutdoorPoke> lastPks;

    public MapUtils(FragmentActivity a) {
        this.activity = a;
    }

    public void setupMap(final int mapViewHolder, final LatLng position, final RelativeLayout relativeLayout) {
        Log.d(TAG, "setupMap ");
        this.relativeLayout = relativeLayout;
        this.lastPosition = position;
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
        }

        activity.getSupportFragmentManager().beginTransaction().replace(mapViewHolder, mapFragment)
                .commitAllowingStateLoss();
        setupOutdoorPokes(position );

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);

//                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//                    @Override
//                    public void onMapLongClick(final LatLng latLng) {
//                        Toast.makeText(activity, "Registering...", Toast.LENGTH_SHORT).show();
//                        new android.os.Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                LocationHelper.getInstance(activity)
//                                        .registerNewPokemonWatcher(latLng,
//                                                RandUtils.create().randomPoke());
//                                Toast.makeText(activity, "Done.", Toast.LENGTH_SHORT).show();
//
//                            }
//                        }, 3000);
//                    }
//                });

                Realm realm = Realm.getInstance(activity);
                List<OutdoorPoke> pks = realm.where(OutdoorPoke.class).equalTo("poke.isDiscovered", false).findAll();
                lastPks = pks;
                setupGoogleMap(googleMap, position, pks);
                forceRefreshDelayed(googleMap, position, pks);
                realm.close();
                Log.d(TAG, "onMapReady ");
                addFAB();
            }
        });
    }

    private void setupOutdoorPokes(LatLng position ) {
        List<RealmPoke> realmPokes1 = Realm.getInstance(activity)
                .where(RealmPoke.class).equalTo("isDiscovered", false).findAll();

        int count = realmPokes1.size();
        List<RealmPoke> realmPokes = new ArrayList<>();

        int realLimit = count < 25 ? count : 25;

        List<Integer> indexes = getRandIndexes(count, realLimit);
        for (int j = 0; j < indexes.size(); j++)
            realmPokes.add(realmPokes1.get(indexes.get(j)));

        final List<LatLng> pokesNearby = RandUtils.create().getPokesNearby(position, realLimit   );

        Realm realm0 = Realm.getInstance(activity);
        RealmResults<OutdoorPoke> outdoorPokes = realm0.where(OutdoorPoke.class).findAll();
        if (outdoorPokes == null || outdoorPokes.size() == 0) {
            realm0.beginTransaction();
            for (int k = 0; k < pokesNearby.size(); k++) {
                RealmPoke poke = realmPokes.get(k);
                LatLng pos = pokesNearby.get(k);
                OutdoorPoke pok3 = new OutdoorPoke();
                pok3.setLat(pos.latitude);
                pok3.setLon(pos.longitude);
                pok3.setPoke(poke);
                pok3.setName(poke.getName());
                realm0.copyToRealmOrUpdate(pok3);
            }
            realm0.commitTransaction();
        } else {
            Log.d(TAG, "outdoorPokes are up to date");
        }
    }

    private List<Integer> getRandIndexes(int max, int limit) {
        Log.d(TAG, "getRandIndexes for params: max = " + max + ", limit = " + limit);
        List<Integer> list = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        Random r = new Random();
        while (set.size() < limit) {
            set.add(r.nextInt(max));
        }

        list.addAll(set);
        for (Integer e : list) {
            Log.d(TAG, "getRandIndexes " + e);
        }
        return list;
    }

    private void addFAB() {
        Log.d(TAG, "add custom FAB  in map utils");
        relativeLayout.addView(getFAB(), getParams());
    }

    @NonNull
    RelativeLayout.LayoutParams getParams() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(70, 70);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        params.addRule(RelativeLayout.ALIGN_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.setMargins(0, 0, 0, 0);
        return params;
    }

    @NonNull
    FloatingActionButton getFAB() {
        FloatingActionButton fab = new FloatingActionButton(activity);
        FABUtils.setupFAB(activity, fab, R.drawable.refresh);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshMarkersOnly();
            }
        });
        return fab;
    }

    private void refreshMarkersOnly() {
        setupGoogleMap(tripGoogleMap, lastPosition, lastPks);
    }

    private void forceRefreshDelayed(final GoogleMap googleMap,
                                     final LatLng position, final List<OutdoorPoke> pks) {
        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                setupGoogleMap(googleMap, position, pks);
            }
        }, 1000);
    }

    private void setupPokemonAsMarker(LatLng pokemonPosition, RealmPoke pokemon) {
        MarkerOptions options = new MarkerOptions().title(pokemon.getName()).position(pokemonPosition);
        Marker marker1 = tripGoogleMap.addMarker(options);
        PicassoMarker marker = new PicassoMarker(marker1);

        Picasso.with(activity).load(PokeSpritesManager
                .getPokemonIconByNameAndId(pokemon.getId(), pokemon.getName()))
                .into(marker);
    }

    public static LatLng asGoogleLatLng(com.javadocmd.simplelatlng.LatLng latLng) {
        return new LatLng(latLng.getLatitude(), latLng.getLongitude());
    }

    public void setupGoogleMap(GoogleMap googleMap, LatLng position, List<OutdoorPoke> pks) {
        tripGoogleMap = googleMap;
        tripGoogleMap.clear();
        tripGoogleMap.setMyLocationEnabled(true);

        for (int j = 0; j < pks.size(); j++) {
            OutdoorPoke pk = pks.get(j);
            setupPokemonAsMarker(new LatLng(pk.getLat(), pk.getLon()), pk.getPoke());
        }

        tripGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(new CameraPosition(position, 16, 60, 0)));
        Config.wasEmpty = false;
    }
}