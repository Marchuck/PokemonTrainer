package pl.lukmarr.pokemontrainer.entities.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trnql.smart.people.PersonEntry;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.adapters.TrainersAdapter;
import pl.lukmarr.pokemontrainer.entities.activities.MainActivity;
import pl.lukmarr.pokemontrainer.utils.FABUtils;
import pl.lukmarr.pokemontrainer.utils.interfaces.DrawerConnector;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainersFragment extends Fragment implements DrawerConnector {
    public static final String TAG = TrainersFragment.class.getSimpleName();
    SupportMapFragment rightMapFragment;
    @Bind(R.id.drawer_layout_trainers)
    DrawerLayout drawerLayout;

    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    @Bind(R.id.right_map_drawer_container)
    RelativeLayout mapContainer;

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
        openMapDrawer(entries, fab_trainers);
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

        FABUtils.setupFAB(getActivity(), fab_trainers, R.drawable.many_markers);
        initDrawer();
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                        onRefreshed();
                    }
                }, 500);
            }
        });
        swipeLayout.setColorSchemeResources(R.color.accent_color,
                R.color.accent_color_darker, R.color.accent_color);
        onRefreshed();
        return v;
    }

    void onRefreshed() {
        trainersAdapter.refresh(fromMain().lastPeople);
        if (trainersAdapter.dataSet == null || trainersAdapter.dataSet.size() == 0)
            DetailsFragment.safeMessage(TrainersFragment.this, "No trainers nearby!");
    }

    public void openMapDrawer(final @Nullable List<PersonEntry> entries, final View view) {
        if (entries == null || entries.size() == 0) {
            if (view != null && view.isShown())
                Snackbar.make(view, "No trainers nearby!", Snackbar.LENGTH_SHORT)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                            pers
//                            PersonEntry ent = new PersonEntry();
//                            openMapDrawer(ent,view);
                            }
                        }).show();
        }
        if (rightMapFragment == null) {
            rightMapFragment = SupportMapFragment.newInstance();
        }
        //prevent of multi-kulti code-generated fabs
//        mapContainer.removeAllViews();
        //attach mapFragment to ViewHolder
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.right_map_drawer_container, rightMapFragment).commitAllowingStateLoss();
        drawerLayout.openDrawer(Gravity.RIGHT);
        rightMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                setupBackFab();

                googleMap.setMyLocationEnabled(true);
                int size = entries == null ? 0 : entries.size();
                double meanLat = 0;
                double meanLon = 0;
                if (size > 0)
                    for (PersonEntry personEntry : entries) {
                        double lat = personEntry.getLatitude();
                        double lon = personEntry.getLongitude();

                        LatLng position = new LatLng(lat, lon);
                        String personName = personEntry.getDataPayload();
                        googleMap.addMarker(new MarkerOptions()
                                .position(position)
                                .title(personName)
                                .snippet(personEntry.getDistanceFromUser() + " meters away")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));
                        meanLat += lat;
                        meanLon += lon;
                    }
                LatLng positionMean;
                if (size > 0) positionMean = new LatLng(meanLat / size, meanLon / size);
                else positionMean = fromMain().lastLatLng;
                if (positionMean == null) return;
                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(new CameraPosition(positionMean, 13, 60, 0)));
            }
        });
    }

    private void setupBackFab() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2,-2);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.setMargins(0, 0, 0, 0);
        FloatingActionButton fab = new FloatingActionButton(getActivity());
        FABUtils.setupFAB(getActivity(), fab, R.drawable.arrow_right);
        mapContainer.addView(fab, params);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });
    }

    MainActivity fromMain() {
        return ((MainActivity) getActivity());
    }

    private void initDrawer() {
        Log.d(TAG, "initDrawer()");
        android.support.v7.widget.Toolbar toolbar = fromMain().toolbar;

        android.support.v7.app.ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(),
                drawerLayout, toolbar, android.R.string.ok, android.R.string.no) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d(TAG, "onDrawerOpened " + drawerView.getId());
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        };
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        drawerLayout.closeDrawer(Gravity.RIGHT);
    }

    @Override
    public void closeDrawer() {
        drawerLayout.closeDrawer(Gravity.RIGHT);
    }

    @Override
    public boolean isDrawerOpened() {
        return drawerLayout.isDrawerOpen(Gravity.RIGHT);
    }
}