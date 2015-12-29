package pl.lukmarr.pokemontrainer.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import pl.lukmarr.pokemontrainer.entities.activities.NewPokemonActivity;

/**
 * Created by lukasz on 26.11.15.
 */
public class ProximityIntentReceiver extends BroadcastReceiver {

    int pokemonId = -1;

    public ProximityIntentReceiver(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    @Override
    public void onReceive(Context context, Intent xintent) {
        Log.d("ProximityIntentReceiver", "onReceive ");
//        String key = LocationManager.KEY_PROXIMITY_ENTERING;
        //Boolean entering = xintent.getBooleanExtra(key, false);
//        int productId = xintent.getIntExtra("productId", 0);
//        Log.d("productId", "" + productId);

        //if (entering) {


        Intent i = new Intent(context, NewPokemonActivity.class);
        i.putExtra("POKEMON_ID", pokemonId);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
        //}

    }
}
