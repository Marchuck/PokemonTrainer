package pl.lukmarr.pokemontrainer.connection;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.util.List;

import pl.lukmarr.pokemontrainer.config.Config;
import pl.lukmarr.pokemontrainer.model.Pokemon;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Łukasz Marczak
 *
 * @since 25.12.15
 */
public class DataFetcher {

    public static void fetch(Activity a, View v, List<Integer> ids, Action1<List<Pokemon>> onReceived, Action1<Throwable> onError) {
        GenericAdapter<Pokemon> pokemonAdapter = new GenericAdapter<>(PokeService.POKEAPI_ENDPOINT, Pokemon.class);
        final PokeService service = pokemonAdapter.adapter.create(PokeService.class);

        Observable.from(ids).flatMap(new Func1<Integer, Observable<Pokemon>>() {
            @Override
            public Observable<Pokemon> call(Integer id) {
                return service.getPokemonById(id);
            }
        }).toList()
                .doOnSubscribe(new UIAction(a, v, true))
                .doOnCompleted(new UIAction(a, v, false))
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(Config.rxJavaError, "error");
                        throwable.printStackTrace();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onReceived, onError);

    }
}
