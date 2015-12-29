package pl.lukmarr.pokemontrainer.entities.fragments;


import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.trnql.smart.people.PersonEntry;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.adapters.TrainersAdapter;
import pl.lukmarr.pokemontrainer.entities.activities.MainActivity;
import pl.lukmarr.pokemontrainer.utils.TrainerManager;
import pl.lukmarr.pokemontrainer.utils.interfaces.PersonConnector;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainersFragment extends Fragment implements PersonConnector {
    public static final String TAG = TrainersFragment.class.getSimpleName();

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.progressive)
    ProgressBar progressBar;

    @Bind(R.id.fab_trainers)
    FloatingActionButton fab_trainers;

    @OnClick(R.id.fab_trainers)
    public void fab_trainersClick() {
        if (trainersAdapter == null) return;

        List<PersonEntry> entries = trainersAdapter.dataSet;
        ((MainActivity) getActivity()).openMapDrawer(entries, fab_trainers);
    }

    public TrainersAdapter trainersAdapter;

    public static TrainersFragment newInstance() {
        return new TrainersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trainers_nearby, container, false);
        ButterKnife.bind(this, v);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        trainersAdapter = new TrainersAdapter(getActivity(), null);
        recyclerView.setAdapter(trainersAdapter);

        int color = getActivity().getResources().getColor(R.color.accent_color);
        int whenPressedColor = getActivity().getResources().getColor(R.color.accent_color_darker);
        fab_trainers.setImageResource(R.drawable.many_markers);
        fab_trainers.setBackgroundTintList(ColorStateList.valueOf(color));
        fab_trainers.setRippleColor(whenPressedColor);

        return v;
    }

    @Override
    public void onPersonReceived(List<PersonEntry> personEntries) {
        Log.d(TAG, "onPersonReceived ");
        trainersAdapter.refresh(personEntries);
        TrainerManager.setup(getActivity(), "productName", "payload");
    }
}