package pl.lukmarr.pokemontrainer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lukasz Marczak on 2015-09-13.
 */
public class Pokemon {

    public List<Ability> abilities = new ArrayList<Ability>();
    public Integer attack;
    public Integer catch_rate;
    public String created;
    public Integer defense;
    public List<Description> descriptions = new ArrayList<Description>();
    public Integer egg_cycles;
    public List<EggGroup> egg_groups = new ArrayList<EggGroup>();
    public String ev_yield;
    public List<Evolution> evolutions = new ArrayList<Evolution>();
    public Integer exp;
    public String growth_rate;
    public Integer happiness;
    public String height;
    public Integer hp;
    public String male_female_ratio;
    public String modified;
    public List<Move> moves = new ArrayList<Move>();
    public String name;
    public Integer national_id;
    public Integer pkdx_id;
    public String resource_uri;
    public Integer sp_atk;
    public Integer sp_def;
    public String species;
    public Integer speed;
    public List<Sprite> sprites = new ArrayList<Sprite>();
    public Integer total;
    public List<Type> types = new ArrayList<Type>();
    public String weight;
}