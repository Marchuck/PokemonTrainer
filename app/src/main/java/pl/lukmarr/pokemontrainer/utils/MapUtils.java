package pl.lukmarr.pokemontrainer.utils;

import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;
import pl.lukmarr.pokemontrainer.connection.PicassoMarker;
import pl.lukmarr.pokemontrainer.connection.PokeSpritesManager;
import pl.lukmarr.pokemontrainer.database.RealmPoke;

/**
 * Created by Łukasz Marczak
 *
 * @since 25.12.15
 */
public class MapUtils {
    public SupportMapFragment mapFragment;
    public GoogleMap tripGoogleMap;

    public void setupMap(final FragmentActivity activity, int mapViewHolder, final LatLng position) {
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
        }

        activity.getSupportFragmentManager().beginTransaction().replace(mapViewHolder, mapFragment)
                .commitAllowingStateLoss();

        final List<RealmPoke> realmPokes = Realm.getInstance(activity).where(RealmPoke.class).findAll();
        final List<LatLng> pokesNearby = RandUtils.create().getPokesNearby(position, realmPokes.size() / 2);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                tripGoogleMap = googleMap;
                tripGoogleMap.clear();
                tripGoogleMap.setMyLocationEnabled(true);

                int j = 0;
                for (LatLng poke : pokesNearby) {
                    RealmPoke poke2 = realmPokes.get(j);
                    MarkerOptions opt = new MarkerOptions();
                    opt.title(poke2.getName()).position(poke).snippet(String.valueOf(poke2.getId()));

                    Marker marker1 = tripGoogleMap.addMarker(opt);
                    PicassoMarker marker = new PicassoMarker(marker1);

                    Picasso.with(activity).load(PokeSpritesManager
                            .getPokemonIconByNameAndId(poke2.getId(), poke2.getName()))
                            .into(marker);
                    ++j;

                }
                tripGoogleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(new CameraPosition(position, 16, 60, 0)));


            }
        });

    }

    public static LatLng asGoogleLatLng(com.javadocmd.simplelatlng.LatLng latLng) {
        return new LatLng(latLng.getLatitude(), latLng.getLongitude());
    }
}