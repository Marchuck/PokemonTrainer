package pl.lukmarr.pokemontrainer.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Łukasz Marczak
 *
 * @since 29.12.15
 */
public class Achievement extends RealmObject {
    @PrimaryKey
    private int id;
    private boolean unlocked;
    private String message;

    public Achievement() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
