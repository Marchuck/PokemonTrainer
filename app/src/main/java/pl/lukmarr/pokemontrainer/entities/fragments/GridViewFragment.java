package pl.lukmarr.pokemontrainer.entities.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.adapters.CustomGridViewAdapter;
import pl.lukmarr.pokemontrainer.database.Achievement;
import pl.lukmarr.pokemontrainer.utils.GenericTrio;

public class GridViewFragment extends Fragment {

    private static final String TAG = GridViewFragment.class.getSimpleName();
    @Bind(R.id.achievements_grid_view)
    GridView gridView;

    List<GenericTrio<Boolean, Integer, String>> gridArray = new ArrayList<>();

    CustomGridViewAdapter customGridAdapter;

    public static GridViewFragment newInstance() {
        GridViewFragment fragment = new GridViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public GridViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid_view, container, false);
        ButterKnife.bind(this, view);
        Realm realm = Realm.getInstance(getActivity());

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Achievement> realmResults = realm.where(Achievement.class).findAllSorted("id");
                for (Achievement achievement : realmResults) {

                    boolean isUnlocked = achievement.isUnlocked();
                    Integer resource = (R.drawable.star);
                    String message = achievement.getMessage();
                    gridArray.add(new GenericTrio<>(isUnlocked, resource, message));
                }
            }
        });

        customGridAdapter = new CustomGridViewAdapter(getActivity(), R.layout.grid_achievement_item, gridArray);
        gridView.setAdapter(customGridAdapter);
        return view;
    }
}