package pl.lukmarr.pokemontrainer.entities.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.database.RealmPoke;
import pl.lukmarr.pokemontrainer.database.RealmPosition;
import pl.lukmarr.pokemontrainer.utils.MapUtils;
import pl.lukmarr.pokemontrainer.utils.interfaces.PokemonRefreshable;

public class MapFragment extends Fragment implements PokemonRefreshable {
    public LatLng lastLatLng;
    public MapUtils mapUtils;
    boolean refreshed = false;

    @Bind(R.id.content_map)
    RelativeLayout content;


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
        this.view = v;
        ButterKnife.bind(this, v);


        mapUtils = new MapUtils(getActivity());

        lastLatLng = null;
        Realm realm = Realm.getInstance(getActivity());
        RealmPosition pos = realm.where(RealmPosition.class).findFirst();

        if (pos != null) {
            lastLatLng = new LatLng(pos.getLat(), pos.getLon());
        } else {
            lastLatLng = new LatLng(50, 20);
        }
        realm.close();
        refreshMap(lastLatLng);

        return v;
    }

    View view;


    public void refreshMap(LatLng latLng) {
        if (!refreshed) {
            mapUtils.setupMap(R.id.content_map, latLng, content);
            refreshed = true;
        }
    }

    @Override
    public void refreshPokes(List<RealmPoke> pokes) {
        mapUtils.setupMap(R.id.content, lastLatLng, content);

    }
}
