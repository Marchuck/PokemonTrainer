package pl.lukmarr.pokemontrainer.utils;

import pl.lukmarr.pokemontrainer.model.Pokemon;
import pl.lukmarr.pokemontrainer.model.Type;

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
}
