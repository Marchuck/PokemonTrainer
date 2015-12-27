package pl.lukmarr.pokemontrainer.connection;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;
import pl.lukmarr.pokemontrainer.config.Config;
import pl.lukmarr.pokemontrainer.database.RealmPoke;
import pl.lukmarr.pokemontrainer.model.Pokemon;
import pl.lukmarr.pokemontrainer.utils.ListCallback;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by Łukasz Marczak
 *
 * @since 25.12.15
 */
public class DataFetcher {
    public DataFetcher() {
    }

    public Subject<List<Pokemon>, List<Pokemon>> subject = PublishSubject.create();

    public void fetch(final Activity a, List<Integer> ids) {
        GenericAdapter<Pokemon> pokemonAdapter = new GenericAdapter<>(PokeService.POKEAPI_ENDPOINT, Pokemon.class);
        final PokeService service = pokemonAdapter.adapter.create(PokeService.class);

        Observable.from(ids).flatMap(new Func1<Integer, Observable<Pokemon>>() {
            @Override
            public Observable<Pokemon> call(Integer id) {
                return service.getPokemonById(id);
            }
        }).toList().doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(Config.rxJavaError, "error");
                throwable.printStackTrace();
                Toast.makeText(a, "error to download data", Toast.LENGTH_SHORT).show();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subject);

    }

    public Action1<List<Pokemon>> receivedListAction1(final Context context,final ListCallback<RealmPoke> listener) {
        return new Action1<List<Pokemon>>() {
            @Override
            public void call(final List<Pokemon> pokemons) {
                Realm realm = Realm.getInstance(context);
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (Pokemon p : pokemons) {
                            RealmPoke poke = new RealmPoke();
                            poke.setName(p.name);
                            poke.setId(p.national_id);
                            poke.setIsDiscovered(false);
                            poke.setImage(PokeSpritesManager.getMainPokeByName(p.name));
                            realm.copyToRealmOrUpdate(poke);
                        }
                    }
                });
                realm.close();
                Realm realm1 = Realm.getInstance(context);
                List<RealmPoke> realmPokeList = realm.where(RealmPoke.class).findAllSorted("id");
                listener.onListReceived(realmPokeList);
                realm1.close();
            }
        };
    }
}
