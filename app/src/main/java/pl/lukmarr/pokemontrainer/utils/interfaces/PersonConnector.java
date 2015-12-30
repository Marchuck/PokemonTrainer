package pl.lukmarr.pokemontrainer.utils.interfaces;

import com.trnql.smart.people.PersonEntry;

import java.util.List;

/**
 * Created by Łukasz Marczak
 *
 * @since 29.12.15
 */
public interface PersonConnector {
    void onPersonReceived(List<PersonEntry> personEntries);
    void closeDrawer();
    boolean isDrawerOpened();
}
