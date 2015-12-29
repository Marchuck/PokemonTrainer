package pl.lukmarr.pokemontrainer.entities.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.entities.activities.MainActivity;

/**
 * Created by Łukasz Marczak
 *
 * @since 29.12.15
 */
public class WaitFragment extends Fragment {
    public static final String TAG = WaitFragment.class.getSimpleName();

    public static WaitFragment newInstance() {
        return new WaitFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wait, container, false);
        ButterKnife.bind(this, v);
        ((MainActivity)getActivity()).lockDrawer();
        return v;
    }

}

