package pl.lukmarr.pokemontrainer.connection;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import pl.lukmarr.pokemontrainer.model.Pokemon;
import pl.lukmarr.pokemontrainer.model.PokemonDescription;
import pl.lukmarr.pokemontrainer.model.PokeDetail;
import pl.lukmarr.pokemontrainer.utils.RandUtils;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.util.Log.d;

/**
 * Created by Łukasz Marczak
 *
 * @since 30.12.15
 */
public class RxDetails {
    public static final String TAG = RxDetails.class.getSimpleName();

    private RxDetails() {
    }

    public static void fetchDescriptionForId(int id, @NonNull Action1<PokemonDescription> onReceived) {
        d(TAG, "inside fetchDescriptionForId ");
        GenericAdapter<Pokemon> adapter = new GenericAdapter<>(PokeService.POKEAPI_ENDPOINT, Pokemon.class);
        adapter.adapter.create(PokeService.class).getPokemonById(id)
                .flatMap(new Func1<Pokemon, Observable<PokemonDescription>>() {
                    @Override
                    public Observable<PokemonDescription> call(Pokemon pokemon) {
                        List<PokeDetail> descriptionList = pokemon.descriptions;
                        String resourceUri = (RandUtils.randListObject(descriptionList)).resource_uri;
                        Log.d(TAG, "call ");
                        String[] splittedUri = resourceUri.split("/");
                        Log.d(TAG, "calling: ");
                        for (String s : splittedUri) {
                            Log.d(TAG, "slices : " + s);
                        }
                        GenericAdapter<PokemonDescription> a =
                                new GenericAdapter<>(PokeService.POKEAPI_ENDPOINT, PokemonDescription.class);
                        int descriptionId = Integer.valueOf(splittedUri[splittedUri.length - 1]);
                        return a.adapter.create(PokeService.class).getPokemonDescription(descriptionId);
                    }
                })
                .observeOn(Schedulers.trampoline())
                .subscribe(onReceived, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "error to fetch data " + throwable.getMessage());
                        throwable.printStackTrace();
                    }
                });
    }
}