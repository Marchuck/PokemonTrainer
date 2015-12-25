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
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.adapters.PokemonAdapter;
import pl.lukmarr.pokemontrainer.connection.DataFetcher;
import pl.lukmarr.pokemontrainer.connection.PokeSpritesManager;
import pl.lukmarr.pokemontrainer.database.RealmPoke;
import pl.lukmarr.pokemontrainer.model.Pokemon;
import rx.functions.Action1;

public class PokedexFragment extends Fragment {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.progressive)
    ProgressBar progressive;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new PokemonAdapter(new ArrayList<RealmPoke>(), getActivity()));
        final long time = System.currentTimeMillis();
        List<Integer> ids = new ArrayList<>();
        for (int j = 1; j < 31; j++) {
            ids.add(j);
        }
        DataFetcher.fetch(getActivity(), progressive, ids, new Action1<List<Pokemon>>() {
            @Override
            public void call(final List<Pokemon> pokemons) {
                Log.d("APP", pokemons.size() + "Pokemons fetched in "
                        + (System.currentTimeMillis() - time) + " ms");
//                App.this.pokemons.clear();
//                App.this.pokemons.addAll(pokemons);
                Realm realm = Realm.getInstance(getActivity());
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (Pokemon p : pokemons) {
                            RealmPoke poke = realm.createObject(RealmPoke.class);
                            poke.setUuid(UUID.randomUUID().toString());
                            poke.setName(p.name);
                            poke.setId(p.national_id);
                            poke.setIsDiscovered(false);
                            poke.setImage(PokeSpritesManager.getMainPokeByName(p.name));
                        }
                    }
                });
                Realm realm2 = Realm.getInstance(getActivity());
                realm2.beginTransaction();
                List<RealmPoke> pokes = realm2.where(RealmPoke.class).findAllSorted("id");
                realm2.commitTransaction();
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(new PokemonAdapter(pokes, getActivity()));

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e("APP", "Unable to fetch pokemons, " + throwable.getMessage());
                throwable.printStackTrace();
            }
        });


        return v;
    }
}
