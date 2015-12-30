package pl.lukmarr.pokemontrainer.entities.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import pl.lukmarr.pokemontrainer.utils.Triple;

public class BadgesFragment extends Fragment {

    private static final String TAG = BadgesFragment.class.getSimpleName();
    @Bind(R.id.achievements_grid_view)
    GridView gridView;

    @Bind(R.id.swipeLayout)
    android.support.v4.widget.SwipeRefreshLayout swipeRefreshLayout;

   public List<Triple<Boolean, Integer, String>> gridArray = new ArrayList<>();

    CustomGridViewAdapter customGridAdapter;

    public static BadgesFragment newInstance() {
        BadgesFragment fragment = new BadgesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BadgesFragment() {
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
        setupGridAdapter();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gridArray.clear();
                        setupGridAdapter();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.accent_color,
                R.color.accent_color_darker, R.color.accent_color);

        return view;
    }

    private void setupGridAdapter() {
        Realm realm = Realm.getInstance(getActivity());

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Achievement> realmResults = realm.where(Achievement.class).findAllSorted("id");
                for (Achievement achievement : realmResults) {

                    boolean isUnlocked = achievement.isUnlocked();
                    Integer resource = (R.drawable.star);
                    String message = achievement.getMessage();
                    gridArray.add(new Triple<>(isUnlocked, resource, message));
                }
            }
        });

        customGridAdapter = new CustomGridViewAdapter(getActivity(),
                R.layout.grid_achievement_item, gridArray);
        gridView.setAdapter(customGridAdapter);
    }
}