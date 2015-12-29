package pl.lukmarr.pokemontrainer.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Łukasz Marczak
 *
 * @since 25.12.15
 */
public class RealmPoke extends RealmObject {

    private int id;
    @PrimaryKey
    private String name;
    private String image;
    private String types;
    private boolean isDiscovered;

    public RealmPoke() {
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
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
