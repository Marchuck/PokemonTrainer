package pl.lukmarr.pokemontrainer.utils;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;
import pl.lukmarr.pokemontrainer.connection.UIAction;
import pl.lukmarr.pokemontrainer.connection.UIError;
import pl.lukmarr.pokemontrainer.database.Achievement;
import pl.lukmarr.pokemontrainer.database.OutdoorPoke;
import pl.lukmarr.pokemontrainer.database.RealmPoke;
import pl.lukmarr.pokemontrainer.model.Pokemon;
import pl.lukmarr.pokemontrainer.model.Type;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static android.util.Log.d;

/**
 * Created by Łukasz Marczak
 *
 * @since 29.12.15
 */
public class PokeUtils {
    public static final String TAG = PokeUtils.class.getSimpleName();

    private PokeUtils() {
    }

    public static String generatePokemonTypes(Pokemon pokemon) {
        String types = "";
        for (Type type : pokemon.types) {
            types += type.name + "&";
        }
        d(TAG, "generatePokemonTypes " + types);
        return types;
    }

    public static void resetGame(final Activity context, View view) {

        rx.Observable.create(new rx.Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {

                if (context == null) {
                    subscriber.onError(new Throwable("Null context Exception!!!"));
                    return;
                }
                Realm realm = Realm.getInstance(context);
                RealmResults<OutdoorPoke> outdoorPokes = realm.where(OutdoorPoke.class).findAll();
                realm.beginTransaction();
                outdoorPokes.clear();
                realm.commitTransaction();

                //un-discover all pokes
                RealmResults<RealmPoke> realmResults = realm.where(RealmPoke.class).findAll();

                realm.beginTransaction();
                for (int j = 0; j < realmResults.size(); j++) {
                    RealmPoke poke = realmResults.get(j);
                    poke.setIsDiscovered(false);
                }
                realm.commitTransaction();

                RealmResults<Achievement> achievements = realm.where(Achievement.class).findAll();
                for (int j = 0; j < achievements.size(); j++) {
                    Achievement achievement = achievements.get(j);
                    achievement.setUnlocked(false);
                }

                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        }).doOnSubscribe(new UIAction(context, view, true))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(new UIAction(context, view, false))
                .doOnError(new UIError(context))
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        Toast.makeText(context, "Game cleared!", Toast.LENGTH_SHORT).show();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "call " + throwable.getMessage());
                        throwable.printStackTrace();
                    }
                });
    }
}
