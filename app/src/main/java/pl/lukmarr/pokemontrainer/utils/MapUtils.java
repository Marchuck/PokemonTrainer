package pl.lukmarr.pokemontrainer.utils;

import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Łukasz Marczak
 *
 * @since 25.12.15
 */
public class MapUtils {
    public SupportMapFragment mapFragment;
    public GoogleMap tripGoogleMap;

    public void setupMap(FragmentActivity activity, int mapViewHolder, final LatLng position) {
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
        }

        activity.getSupportFragmentManager().beginTransaction().replace(mapViewHolder, mapFragment)
                .commitAllowingStateLoss();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                tripGoogleMap = googleMap;
                tripGoogleMap.clear();
                tripGoogleMap.setMyLocationEnabled(true);
                tripGoogleMap.addMarker(new MarkerOptions()
                                .position(position)
//                        .icon(categorizedIconDescriptor(categoryCode))
                );
                tripGoogleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(new CameraPosition(position, 16, 60, 0)));
            }
        });

    }
    public static LatLng asGoogleLatLng(com.javadocmd.simplelatlng.LatLng latLng) {
        return new LatLng(latLng.getLatitude(), latLng.getLongitude());
    }
}