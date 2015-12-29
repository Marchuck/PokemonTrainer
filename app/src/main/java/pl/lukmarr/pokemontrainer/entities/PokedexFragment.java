package pl.lukmarr.pokemontrainer.entities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.adapters.PokemonAdapter;
import pl.lukmarr.pokemontrainer.database.RealmPoke;
import pl.lukmarr.pokemontrainer.utils.interfaces.PokemonRefreshable;

public class PokedexFragment extends Fragment implements PokemonRefreshable {
    public static final String TAG = PokedexFragment.class.getSimpleName();
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.progressive)
    ProgressBar progressive;

    public PokemonAdapter pokemonAdapter;

    public static PokedexFragment newInstance() {
        PokedexFragment fragment = new PokedexFragment();
        return fragment;
    }

    public PokedexFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pokedex, container, false);
        ButterKnife.bind(this, v);
        setupRecyclerView();
        return v;
    }


    void showProgress(final boolean s) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressive.setVisibility(s ? View.VISIBLE : View.GONE);
            }
        });
    }

    void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Realm realm2 = Realm.getInstance(getActivity());
        List<RealmPoke> pokes = realm2.where(RealmPoke.class).findAllSorted("id");
        pokes = pokes == null ? new ArrayList<RealmPoke>() : pokes;
        Log.d(TAG, "setupRecyclerView with " + pokes.size() + " pokes");
        pokemonAdapter = new PokemonAdapter(pokes, getActivity(), progressive,recyclerView);
        recyclerView.setAdapter(pokemonAdapter);
    }

    @Override
    public void refreshPokes(final List<RealmPoke> pokes) {
        Log.d(TAG, "refreshPokes with " + pokes.size() + " pokes");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pokemonAdapter.refresh(pokes);
            }
        });
    }
}
