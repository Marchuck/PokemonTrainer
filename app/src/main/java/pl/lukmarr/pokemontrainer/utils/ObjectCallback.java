package pl.lukmarr.pokemontrainer.utils;

/**
 * Created by Łukasz Marczak
 *
 * @since 27.12.15
 */
public interface ObjectCallback<T> {

    void onObjectReceived(T t);
}
