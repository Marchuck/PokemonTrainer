package pl.lukmarr.pokemontrainer.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Łukasz Marczak
 *
 * @since 29.12.15
 */
public class OutdoorPoke extends RealmObject {

    @PrimaryKey
    private String name;
    private double lat;
    private double lon;
    private RealmPoke poke;

    public OutdoorPoke() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public RealmPoke getPoke() {
        return poke;
    }

    public void setPoke(RealmPoke poke) {
        this.poke = poke;
    }
}
