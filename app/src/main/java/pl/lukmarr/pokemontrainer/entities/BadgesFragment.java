package pl.lukmarr.pokemontrainer.entities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import pl.lukmarr.pokemontrainer.R;


public class BadgesFragment extends Fragment {
    public static BadgesFragment newInstance() {
        BadgesFragment fragment = new BadgesFragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_badges, container, false);
        ButterKnife.bind(this, v);
        return v;
    }


}
