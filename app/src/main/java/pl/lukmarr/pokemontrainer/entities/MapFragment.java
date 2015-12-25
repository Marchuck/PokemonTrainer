package pl.lukmarr.pokemontrainer.entities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.utils.MapUtils;

public class MapFragment extends Fragment {

    MapUtils mapUtils;
    boolean refreshed = false;

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        mapUtils = new MapUtils();
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LatLng lastLatLng = ((MainActivity) getActivity()).lastLatLng;
                LatLng cacheLatLng = new LatLng(50.4265305, 22.6305689);
                refreshMap(lastLatLng == null ? cacheLatLng : lastLatLng);
            }
        }, 1000);
        return v;
    }

    public void refreshMap(LatLng latLng) {
        if (!refreshed) {
            mapUtils.setupMap(getActivity(), R.id.content, latLng);
            refreshed = true;
        }
    }

}
