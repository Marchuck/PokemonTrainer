package pl.lukmarr.pokemontrainer.connection;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import io.realm.Realm;
import pl.lukmarr.pokemontrainer.database.RealmPoke;
import pl.lukmarr.pokemontrainer.model.Pokemon;
import pl.lukmarr.pokemontrainer.utils.PokeUtils;
import pl.lukmarr.pokemontrainer.utils.interfaces.ListCallback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Łukasz Marczak
 *
 * @since 25.12.15
 */
public class DataFetcher {
    public static final String TAG = DataFetcher.class.getSimpleName();
    public static boolean fetchingInProgress = false;

    public DataFetcher() {
    }

    public Action0 onSubscribe;
    public Action0 onCompleted;
    public Action1<Throwable> onError;


    public void fetchPokes(final Activity a, List<Integer> ids, @Nullable final ListCallback<RealmPoke> listener) {
        Log.d(TAG, "fetchPokes ");
        RestAdapter pokemonAdapter = new RestAdapter.Builder()
                .setEndpoint(PokeService.POKEAPI_ENDPOINT)
                .setConverter(new GsonConverter(new Gson()))
                .build();
        final PokeService service = pokemonAdapter.create(PokeService.class);
        fetchingInProgress = true;
        Observable.from(ids).flatMap(new Func1<Integer, Observable<Pokemon>>() {
            @Override
            public Observable<Pokemon> call(Integer id) {
                return service.getPokemonById(id);
            }
        }).toList().doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        }).doOnSubscribe(onSubscribe)
                .doOnCompleted(onCompleted)
                .subscribeOn(Schedulers.trampoline())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(receivedListAction1(a, listener), new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
//                        if (throwable != null) {
//                            Log.e(TAG, throwable.getMessage());
//                            throwable.printStackTrace();
//                        }
                        fetchingInProgress = false;
                    }
                });

    }

    public static Action1<List<Pokemon>> receivedListAction1(final Context context, @Nullable final ListCallback<RealmPoke> listener) {
        return new Action1<List<Pokemon>>() {
            @Override
            public void call(final List<Pokemon> pokemons) {
                Realm realm = Realm.getInstance(context);
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (Pokemon p : pokemons) {
                            Log.d(TAG, "saving next poke");
                            RealmPoke poke = new RealmPoke();
                            poke.setName(p.name);
                            poke.setId(p.national_id);
                            String types = PokeUtils.generatePokemonTypes(p);
                            poke.setTypes(types == null ? "unknown" : types);
                            poke.setIsDiscovered(false);
                            poke.setImage(PokeSpritesManager.getMainPokeByName(p.name));
                            realm.copyToRealmOrUpdate(poke);
                        }
                    }
                });
                realm.close();
                Realm realm1 = Realm.getInstance(context);
                List<RealmPoke> realmPokeList = realm1.where(RealmPoke.class).findAllSorted("id");
                if (listener != null) listener.onListReceived(realmPokeList);
                fetchingInProgress = false;
            }
        };
    }
}
