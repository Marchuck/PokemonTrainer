package pl.lukmarr.pokemontrainer.utils.general;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import pl.lukmarr.pokemontrainer.database.RealmPoke;

/**
 * Created by Łukasz Marczak
 *
 * @since 30.12.15
 */
public class RealmUtils {
    public static final String TAG = RealmUtils.class.getSimpleName();

    public static int currentPokesCount(Context c) {
        Realm realm = Realm.getInstance(c);
        List<RealmPoke> all = realm.where(RealmPoke.class).equalTo("isDiscovered", true).findAll();
        int count = all == null ? 0 : all.size();
        Log.d(TAG, "currentPokesCount " + count);
        return count;
    }

    public static void setupRealm(Activity athis) {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(athis)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getInstance(athis);
        realm.beginTransaction();
        RealmResults<RealmUser> user = realm.where(RealmUser.class).findAll();
        if (user == null) {
            RealmUser realmUser = new RealmUser("User");
            realm.copyToRealmOrUpdate(realmUser);
        }
        realm.commitTransaction();
        realm.close();
    }

    public static String getUserName(Context context) {
        Realm realm = Realm.getInstance(context);
        RealmUser user = realm.where(RealmUser.class).findFirst();
        String userName = user == null ? "User" : user.getName();
        realm.close();
        return userName;
    }

    public static void updateUser(final String newName, Context context) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        realm.commitTransaction();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmUser user = realm.where(RealmUser.class).findFirst();
                if (user == null) {
                    RealmUser realmUser = new RealmUser(newName);
                    realm.copyToRealmOrUpdate(realmUser);
                } else user.setName(newName);
            }
        });
        realm.close();
    }
}
