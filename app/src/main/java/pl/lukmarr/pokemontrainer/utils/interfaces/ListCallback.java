package pl.lukmarr.pokemontrainer.utils.interfaces;

import java.util.List;

/**
 * Created by Łukasz Marczak
 *
 * @since 27.12.15
 */
public interface ListCallback<T> {

    void onListReceived(List<T>list);
}
