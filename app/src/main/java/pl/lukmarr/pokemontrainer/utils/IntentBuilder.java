package pl.lukmarr.pokemontrainer.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import pl.lukmarr.pokemontrainer.entities.activities.NewPokemonActivity;

/**
 * Created by Łukasz Marczak
 *
 * @since 29.12.15
 */
public class IntentBuilder {
    public static final String TAG = IntentBuilder.class.getSimpleName();

    public static Intent NewPokemonActivityBuilder(Context c, int pokemonId) {

        Intent intent = new Intent(c, NewPokemonActivity.class);
        intent.putExtra("POKEMON_ID", pokemonId);
        Log.d(TAG, "NewPokemonActivityBuilder called with id = " + pokemonId);
        return intent;
    }
}
