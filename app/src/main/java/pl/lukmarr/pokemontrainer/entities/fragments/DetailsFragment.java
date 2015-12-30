package pl.lukmarr.pokemontrainer.entities.fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.trnql.smart.people.PeopleManager;
import com.trnql.smart.weather.WeatherEntry;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.entities.WeatherDisplayer;
import pl.lukmarr.pokemontrainer.entities.activities.MainActivity;
import pl.lukmarr.pokemontrainer.utils.FABUtils;
import pl.lukmarr.pokemontrainer.utils.FirstTimeSetup;
import pl.lukmarr.pokemontrainer.utils.PokeUtils;
import pl.lukmarr.pokemontrainer.utils.dialogs.AbstractDialog;
import pl.lukmarr.pokemontrainer.utils.dialogs.LoginDialog;
import pl.lukmarr.pokemontrainer.utils.general.RealmUtils;
import pl.lukmarr.pokemontrainer.utils.interfaces.ObjectCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    public static final String TAG = DetailsFragment.class.getSimpleName();
    @Bind(R.id.weatherFAB)
    public FloatingActionButton fabWeather;

    @OnClick(R.id.weatherFAB)
    public void seeWeather() {
        String weatherMessage = "Weather unavailable";
        WeatherEntry weatherEntry = fromMain().weather;
        if (weatherEntry != null)
            weatherMessage = weatherEntry.getWeatherSummaryAsString();

        new WeatherDisplayer(getActivity(), weatherMessage);
    }

    @Bind(R.id.clearDataFAB)
    public FloatingActionButton fabDel;

    @OnClick(R.id.clearDataFAB)
    public void clearData() {
        new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure to clear all progress?. " +
                        "You will delete all badges and discovered pokes.")
                .setNegativeButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PokeUtils.resetGame(getActivity(), getView(), new ObjectCallback<Boolean>() {
                            @Override
                            public void onObjectReceived(Boolean aBoolean) {
                                fromMain().switchTo(4);
                            }
                        });
                        dialog.dismiss();
                    }
                }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    @Bind(R.id.changeNameFAB)
    public FloatingActionButton fabChange;


    @OnClick(R.id.changeNameFAB)
    public void showChangeNameDialog() {
        new LoginDialog(new AbstractDialog.DialogInteractionListener() {
            @Override
            public void afterOk(final String newUserName) {
                int length = newUserName.trim().length();
                if (length < 3 || length > 30) {
                    safeMessage(DetailsFragment.this,"Enter valid name!");
                    showChangeNameDialog();
                    return;
                }
                PeopleManager.INSTANCE.setDataPayload(newUserName);
                RealmUtils.updateUser(newUserName, getActivity());
                fromMain().refreshLeftDrawer();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        safeMessage(DetailsFragment.this,"Hello, " + newUserName + "!");
                        fromMain().refreshLeftDrawer();
                    }
                }, 400);
            }

            @Override
            public void afterCancel() {
                Log.d(TAG, "afterCancel ");
            }
        }).prepare(getActivity()).show();
    }

    public static void safeMessage(Fragment f, String s) {
        View view = f.getView();
        Activity activity = f.getActivity();
        if (view != null)
            Snackbar.make(view, s, Snackbar.LENGTH_SHORT).show();
        else if (activity != null) {
            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "both view and activity are null");
        }
    }

    MainActivity fromMain() {
        return ((MainActivity) getActivity());
    }

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.bind(this, v);
        FABUtils.setupFAB(getActivity(), fabDel, R.drawable.delete);
        FABUtils.setupFAB(getActivity(), fabChange, R.drawable.pencil);
        FABUtils.setupFAB(getActivity(), fabWeather, WeatherDisplayer.getRandRes());
        FirstTimeSetup.setupSmarts(getActivity());
        return v;
    }
}
