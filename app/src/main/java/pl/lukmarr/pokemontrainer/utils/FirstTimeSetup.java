package pl.lukmarr.pokemontrainer.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.trnql.smart.people.PeopleManager;

import java.util.Random;

import io.realm.Realm;
import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.config.Config;
import pl.lukmarr.pokemontrainer.database.Achievement;
import pl.lukmarr.pokemontrainer.database.RealmPosition;
import pl.lukmarr.pokemontrainer.entities.activities.MainActivity;

/**
 * Created by Łukasz Marczak
 *
 * @since 29.12.15
 */
public class FirstTimeSetup {
    public static final String TAG = FirstTimeSetup.class.getSimpleName();

    public static void handleNoInternetConnection(final MainActivity athis) {

        if (Config.isNetworkAvailable(athis)) {
            athis.fetchPokes();
        } else {
            new AlertDialog.Builder(athis)
                    .setMessage("No Internet connection detected.")
                    .setNegativeButton("Close App", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            athis.finish();
                        }
                    }).setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    athis.startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 100);
                    dialog.dismiss();
                }
            }).show();
        }
    }


    public static void setup(Context context) {
        Log.d(TAG, "setup ");
        setupDataForTheFirstTime(context);
    }

    public static void setupSmarts(Context c) {
        Log.d(TAG, "setupSmarts ");
        try {
            setupSmartPeopleForTheFirstTime(c);
            modifySmartPeopleForTheFirstTime();
        } catch (Exception x) {
            Log.e(TAG, "setupSmarts " + x.getMessage());
            x.printStackTrace();
        }
    }

    public static void setupSmartPeopleForTheFirstTime(Context context) {
        Log.d(TAG, "setupSmartPeopleForTheFirstTime ");
        PeopleManager.INSTANCE.setProductName(context.getResources().getString(R.string.trnql_key));
        PeopleManager.INSTANCE.setUserToken(context.getResources().getString(R.string.trnql_key));
    }

    public static void modifySmartPeopleForTheFirstTime() {
        Log.d(TAG, "modifySmartPeopleForTheFirstTime ");
        PeopleManager.INSTANCE.setSearchRadius(50000);
        PeopleManager.INSTANCE.setDataPayload("RealmUser " + getRandy());
    }

    static int getRandy() {
        return 1 + new Random().nextInt() % 9;
    }

    private static void setupDataForTheFirstTime(Context mContext) {
        Realm realm = Realm.getInstance(mContext);
        RealmPosition position = realm.where(RealmPosition.class).findFirst();
        if (position != null) {
            realm.close();
            return;
        }
        realm.beginTransaction();
        String[] messages = new String[]{
                "Found first Pokemon",
                "Found grass Pokemon",
                "Found bug Pokemon",
                "Found poison Pokemon",
                "Found water Pokemon",
                "Found fire Pokemon",
                "Found rock Pokemon",
                "Found ground Pokemon",
                "Found flying Pokemon",
                "Found fighting Pokemon",
                "Found electric Pokemon",
                "Found steel Pokemon",
                "Found fairy Pokemon",
                "Found dark Pokemon",
                "Found ghost Pokemon",
                "Found normal Pokemon",
                "Found ice Pokemon",
                "Found psychic Pokemon",
                "Found dragon Pokemon",
                "Found 10 Pokemons",
                "Found 20 Pokemons",
                "Found 30 Pokemons",
                "Found 40 Pokemons",
                "Found 50 Pokemons",
        };
        for (int j = 0; j < messages.length; j++) {
            Achievement achievement = new Achievement();
            achievement.setId(j + 1);
            achievement.setMessage(messages[j]);
            achievement.setUnlocked(false);
            realm.copyToRealmOrUpdate(achievement);
        }
        realm.commitTransaction();
    }
}
