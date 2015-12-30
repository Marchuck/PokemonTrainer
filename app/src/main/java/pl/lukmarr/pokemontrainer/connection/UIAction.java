package pl.lukmarr.pokemontrainer.connection;

import android.app.Activity;
import android.view.View;

import rx.functions.Action0;

/**
 * Created by Łukasz Marczak
 *
 * @since 23.12.15
 */
public class UIAction implements Action0 {
    Activity a;
    View v;
    boolean s;

    public UIAction(Activity a, View v, boolean s) {
        this.a = a;
        this.v = v;
        this.s = s;
    }

    @Override
    public void call() {
        if (v != null && v.isShown())
            a.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    v.setVisibility(s ? View.VISIBLE : View.GONE);
                }
            });
    }
}
