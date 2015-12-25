package pl.lukmarr.pokemontrainer.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Łukasz Marczak
 *
 * @since 25.12.15
 */
public class RealmPoke extends RealmObject {
    @PrimaryKey
    private String uuid;
    private int id;
    private String name;
    private String image;
    private boolean isDiscovered;

    public RealmPoke() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isDiscovered() {
        return isDiscovered;
    }

    public void setIsDiscovered(boolean isDiscovered) {
        this.isDiscovered = isDiscovered;
    }
}
