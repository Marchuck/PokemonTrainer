package pl.lukmarr.pokemontrainer.connection;

/**
 * Created by Lukasz Marczak on 2015-08-25.
 */
public class PokeSpritesManager {
//    public static final String TAG = PokeSpritesManager.class.getSimpleName();

    public static String getMainPokeByName(String pokemonName) {
        return "http://img.pokemondb.net/artwork/" + lowerCaseName(pokemonName) + ".jpg";
    }

    public static String lowerCaseName(String pokemonName) {
        if (pokemonName == null || pokemonName.length() < 3)
            pokemonName = "pikachu";
        return pokemonName.toLowerCase();
    }

    public static String camelCaseName(String pokemonName) {
        if (pokemonName == null || pokemonName.length() < 3)
            pokemonName = "Pikachu";
        String firstLetter = String.valueOf(pokemonName.charAt(0)).toUpperCase();
        String lowercase = pokemonName.toLowerCase();
        String reti = firstLetter + lowercase.substring(1);
//        Log.d(TAG, "camelCaseName " + reti);
        return reti;
    }


    //493 pokemons available
    public static String getPokemonBackByName(String pokomonName) {
        return "http://img.pokemondb.net/sprites/heartgold-soulsilver/back-normal/" + lowerCaseName(pokomonName) + ".png";
    }

    //493 pokemons available
    public static String getPokemonFrontByName(String pokomonName) {
        return "http://img.pokemondb.net/sprites/heartgold-soulsilver/normal/" + lowerCaseName(pokomonName) + ".png";
    }

    public static String getPokemonIconByNameAndId(int id, String name) {
        String fixedId = getFixedId(id);
        String fixedName = camelCaseName(name);
//        Log.d(TAG, "id = " + fixedId + "," + fixedName);
        if (id == 29) {
            return "http://icons.iconarchive.com/icons/hektakun/pokemon/72/" + fixedId + "-Nidoran-icon.png";
        } else if (id == 30) {
            return "http://icons.iconarchive.com/icons/hektakun/pokemon/72/" + fixedId + "-Nidorina-icon.png";
        } else if (id == 31) {
            return "http://icons.iconarchive.com/icons/hektakun/pokemon/72/" + fixedId + "-Nidoqueen-icon.png";
        } else if (id == 32) {
            return "http://icons.iconarchive.com/icons/hektakun/pokemon/72/" + fixedId + "-Nidorano-icon.png";
        } else if (id == 33) {
            return "http://icons.iconarchive.com/icons/hektakun/pokemon/72/" + fixedId + "-Nidorino-icon.png";
        } else if (id == 34) {
            return "http://icons.iconarchive.com/icons/hektakun/pokemon/72/" + fixedId + "-Nidoking-icon.png";
        } else if (id == 122) {
            return "http://icons.iconarchive.com/icons/hektakun/pokemon/72/122-Mr-Mime-icon.png";
        }
        return "http://icons.iconarchive.com/icons/hektakun/pokemon/72/" + fixedId + "-" + fixedName + "-icon.png";
    }

    private static String getFixedId(int id) {
        if (id < 10) {
            return "00" + id;
        } else if (id < 100) {
            return "0" + id;
        } else return String.valueOf(id);
    }

}
