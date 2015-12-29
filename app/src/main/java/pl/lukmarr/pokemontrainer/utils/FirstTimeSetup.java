package pl.lukmarr.pokemontrainer.utils;

import android.content.Context;

import io.realm.Realm;
import pl.lukmarr.pokemontrainer.database.Achievement;
import pl.lukmarr.pokemontrainer.database.RealmPosition;

/**
 * Created by Łukasz Marczak
 *
 * @since 29.12.15
 */
public class FirstTimeSetup {
    public static void setup(Context context) {
        setupDataForTheFirstTime(context);
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
                "Found Water Pokemon",
                "Found Fire Pokemon",
                "Found Rock Pokemon",
                "Found Ground Pokemon",
                "Found Fly Pokemon",
                "Found Electric Pokemon",
                "Found Fairy Pokemon",
                "Found Normal Pokemon",
                "Found Ice Pokemon",
                "Found Legendary Pokemon",
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
