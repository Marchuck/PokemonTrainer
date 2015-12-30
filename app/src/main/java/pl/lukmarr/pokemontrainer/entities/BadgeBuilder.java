package pl.lukmarr.pokemontrainer.entities;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import pl.lukmarr.pokemontrainer.R;

/**
 * Created by Łukasz Marczak
 *
 * @since 29.12.15
 */
public class BadgeBuilder {


    public static void build(Activity context, String message) {
        if (context == null) {
            Log.e("", "build with null context");

            return;
        }
        rawBuild(context, message);
    }

    private static void rawBuild(Activity context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        //this makes cardView look
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.badge_dialog);
        TextView su = (TextView) dialog.findViewById(R.id.subtitle);
        RelativeLayout ok = (RelativeLayout) dialog.findViewById(R.id.parent);
        su.setText(message);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void buildFromActivity(Activity applicationContext, String s) {
        if (applicationContext == null) return;
        rawBuild(applicationContext, s);
    }
}
