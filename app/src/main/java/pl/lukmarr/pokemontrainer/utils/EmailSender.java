package pl.lukmarr.pokemontrainer.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Łukasz Marczak
 *
 * @since 29.12.15
 */
public class EmailSender {

    public static void fire(Activity activity) {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "lukmardev@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pokemon Trainer App");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, Lucas!");
        activity.startActivity(Intent.createChooser(emailIntent, "Send email..."));

//        Intent intent = new Intent(Intent.ACTION_SENDTO);
//        intent.setType("text/html");
//        intent.putExtra(Intent.EXTRA_EMAIL, "lukmardev@gmail.com");
//        intent.putExtra(Intent.EXTRA_SUBJECT, "PokemonTrainer app");
//        intent.putExtra(Intent.EXTRA_TEXT, "Hello, Lucas!");
//
//        activity.startActivity(Intent.createChooser(intent, "Send Email"));
    }
}
