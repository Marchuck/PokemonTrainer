package pl.lukmarr.pokemontrainer.utils.interfaces;

import java.util.List;

import pl.lukmarr.pokemontrainer.database.RealmPoke;

/**
 * Created by Łukasz Marczak
 *
 * @since 28.12.15
 */
public interface PokemonRefreshable {
    void refreshPokes(List<RealmPoke> pokes);
}
