package pl.lukmarr.pokemontrainer.database;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;
import pl.lukmarr.pokemontrainer.entities.BadgeBuilder;

/**
 * Created by Łukasz Marczak
 *
 * @since 29.12.15
 */
public class AchievementsManager {
    public static final String TAG = AchievementsManager.class.getSimpleName();

    public static void unlockPokemon(Activity c, int id) {
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

    public static void checkAchievements(final Activity context) {
        Realm realm = Realm.getInstance(context);
        Set<String> typesDiscovered = new HashSet<>();

        List<String> unlockedBadges = new ArrayList<>();

        //check how much pokes discovered
        RealmResults<RealmPoke> realmPokes = realm.where(RealmPoke.class).equalTo("isDiscovered", true).findAll();
        for (RealmPoke poke : realmPokes) {
            Log.d(TAG, "checkAchievements " + poke.getTypes());

            if (poke.getTypes() != null) {
                String[] tt = poke.getTypes().split("&");
                Collections.addAll(typesDiscovered, tt);
            }
        }
        int pokemonSize = realmPokes.size();
        Log.d(TAG, "current discovered pokes size: " + pokemonSize);
        Log.d(TAG, "current discovered types : ");
        for (String s : typesDiscovered) {
            Log.d(TAG, "type:  " + s);
        }
//        RealmResults<RealmPoke> firePokes = realm.where(RealmPoke.class).;

        //update achievements
        realm.beginTransaction();
        RealmResults<Achievement> achievements = realm.where(Achievement.class).equalTo("unlocked", false).findAll();

        for (int j = 0; j < achievements.size(); j++) {
            Achievement achv = achievements.get(j);

            if (achv.getMessage().contains("Pokemons")) {
                if (pokemonSize >= 50 && achv.getMessage().contains("50")) {
                    achv.setUnlocked(true);
                    unlockedBadges.add(achv.getMessage());
                } else if (pokemonSize >= 40 && achv.getMessage().contains("40")) {
                    achv.setUnlocked(true);
                    unlockedBadges.add(achv.getMessage());
                } else if (pokemonSize >= 30 && achv.getMessage().contains("30")) {
                    achv.setUnlocked(true);
                    unlockedBadges.add(achv.getMessage());
                } else if (pokemonSize >= 20 && achv.getMessage().contains("20")) {
                    achv.setUnlocked(true);
                    unlockedBadges.add(achv.getMessage());
                } else if (pokemonSize >= 10 && achv.getMessage().contains("10")) {
                    achv.setUnlocked(true);
                    unlockedBadges.add(achv.getMessage());
                }
            } else if (achv.getMessage().contains("Pokemon")) {
                List<String> list = new ArrayList<>();
                for (String f : typesDiscovered) {
                    list.add(f);
                }
                for (int k = 0; k < list.size(); k++) {
                    if (achv.getMessage().trim().contains(list.get(k))) {
                        achv.setUnlocked(true);
                        unlockedBadges.add(achv.getMessage());
                    }
                }
            }
            if (achv.getMessage().contains("first")) {
                achv.setUnlocked(true);
                unlockedBadges.add(achv.getMessage());
            }
        }
        int j = 1500;
        for (final String s : unlockedBadges) {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BadgeBuilder.build(context, s);
                }
            }, j);
            j += 1500;
        }
        realm.commitTransaction();
    }
}
