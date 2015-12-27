package pl.lukmarr.pokemontrainer.entities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.adapters.PokemonAdapter;
import pl.lukmarr.pokemontrainer.connection.DataFetcher;
import pl.lukmarr.pokemontrainer.database.RealmPoke;
import pl.lukmarr.pokemontrainer.utils.ListCallback;
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
        setupRecyclerView();
        downloadData();
        return v;
    }

    void downloadData() {
        showProgress(true);
        DataFetcher fetcher = ((MainActivity) getActivity()).dataFetcher;
         fetcher.subject.subscribe(fetcher.receivedListAction1(getActivity(), new ListCallback<RealmPoke>() {
            @Override
            public void onListReceived(List<RealmPoke> list) {
                recyclerView.setAdapter(new PokemonAdapter(list, getActivity()));
                showProgress(false);
            }
        }), new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                String msg = throwable.getMessage() == null ? "Error occurred" : throwable.getMessage();
                showProgress(false);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
            }
        });
    }

    void showProgress(boolean s) {
        progressive.setVisibility(s ? View.VISIBLE : View.GONE);
    }

    void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new PokemonAdapter(new ArrayList<RealmPoke>(), getActivity()));
        Realm realm2 = Realm.getInstance(getActivity());
        realm2.beginTransaction();
        List<RealmPoke> pokes = realm2.where(RealmPoke.class).findAllSorted("id");
        realm2.commitTransaction();
        pokes = pokes == null ? new ArrayList<RealmPoke>() : pokes;
        recyclerView.setAdapter(new PokemonAdapter(pokes, getActivity()));
    }
}
