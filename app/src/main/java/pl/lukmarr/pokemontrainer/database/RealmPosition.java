package pl.lukmarr.pokemontrainer.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Łukasz Marczak
 *
 * @since 28.12.15
 */
public class RealmPosition extends RealmObject {
    @PrimaryKey
    private String position;
    private double lat;
    private double lon;
    private long timestamp;

    public RealmPosition() {
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
