package pl.lukmarr.pokemontrainer.utils;

import android.content.Context;
import android.util.Log;

import com.trnql.smart.people.PeopleManager;

import pl.lukmarr.pokemontrainer.R;

/**
 * Created by Łukasz Marczak
 *
 * @since 29.12.15
 */
public class TrainerManager {

    public static final String TAG = TrainerManager.class.getSimpleName();

    public static void setup(Context context, String productName, String payload) {
        printPeopleManager();

        Log.d(TAG, "TrainersAdapter after set");
        PeopleManager.INSTANCE.setUserToken(context.getResources().getString(R.string.trnql_key));
        PeopleManager.INSTANCE.setDataPayload(payload);
        PeopleManager.INSTANCE.setProductName(productName);
        PeopleManager.INSTANCE.setSearchRadius(100000000);
        printPeopleManager();
    }

    static void printPeopleManager() {
        Log.d(TAG, "PeopleManager");
        Log.d(TAG, "name: " + PeopleManager.INSTANCE.name());
        Log.d(TAG, "data payload: " + PeopleManager.INSTANCE.getDataPayload());
        Log.d(TAG, "searchRadius: " + PeopleManager.INSTANCE.getSearchRadius());
        Log.d(TAG, "productName: " + PeopleManager.INSTANCE.getProductName());
        Log.d(TAG, "isRunning: " + PeopleManager.INSTANCE.isRunning());
    }

}
