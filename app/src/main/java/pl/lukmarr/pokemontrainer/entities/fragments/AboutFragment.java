package pl.lukmarr.pokemontrainer.entities.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.utils.EmailSender;


public class AboutFragment extends Fragment {
    public static final String TAG = AboutFragment.class.getSimpleName();

    @OnClick(R.id.fab)
    public void onClickedFAB(){
        Log.d(TAG, "onClickedFAB ");
        EmailSender.fire(getActivity());
    }

    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    public AboutFragment() {
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
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, v);
        return v;
    }


}
