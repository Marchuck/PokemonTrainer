package pl.lukmarr.pokemontrainer.database;

import android.content.Context;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Łukasz Marczak
 *
 * @since 29.12.15
 */
public class AchievementsManager {
    public static final String TAG = AchievementsManager.class.getSimpleName();

    public static void unlockPokemon(Context c, int id) {
        Realm realm = Realm.getInstance(c);
        realm.beginTransaction();
        RealmPoke realmPoke = realm.where(RealmPoke.class).equalTo("id", id).findFirst();
        if (realmPoke == null) {
            realm.commitTransaction();
            realm.close();
            return;
        }
        realmPoke.setIsDiscovered(true);
        realm.commitTransaction();
        realm.close();
    }

    public static void checkAchievements(Context context) {
        Realm realm = Realm.getInstance(context);

        //check how much pokes discovered
        RealmResults<RealmPoke> realmPokes = realm.where(RealmPoke.class).equalTo("isDiscovered", true).findAll();
        for (RealmPoke poke :
                realmPokes) {
            Log.d(TAG, "checkAchievements " + poke.getTypes());
        }
        int pokemonSize = realmPokes.size();
        Log.d(TAG, "current discovered pokes size: " + pokemonSize);
//        RealmResults<RealmPoke> firePokes = realm.where(RealmPoke.class).;

        //update achievements
        realm.beginTransaction();


        realm.commitTransaction();
    }
}
