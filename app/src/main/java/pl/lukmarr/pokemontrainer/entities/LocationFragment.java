package pl.lukmarr.pokemontrainer.entities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trnql.smart.base.SmartFragment;

import pl.lukmarr.pokemontrainer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends SmartFragment {


    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        return view;
    }


}
