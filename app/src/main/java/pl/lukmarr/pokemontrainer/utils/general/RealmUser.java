package pl.lukmarr.pokemontrainer.utils.general;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Łukasz Marczak
 *
 * @since 30.12.15
 */
public class RealmUser extends RealmObject {
    @PrimaryKey
    private String name;

    public RealmUser() {
    }

    public RealmUser(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
