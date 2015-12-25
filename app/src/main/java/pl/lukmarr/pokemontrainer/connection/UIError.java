package pl.lukmarr.pokemontrainer.connection;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import pl.lukmarr.pokemontrainer.config.Config;
import rx.functions.Action1;

/**
 * Created by Łukasz Marczak
 *
 * @since 23.12.15
 */
public class UIError implements Action1<Throwable> {
    @NonNull
    Activity a;
    @Nullable
    View v = null;
    @Nullable
    String msg = null;

    /**
     * displays snackbar with custom message
     *
     * @param a
     * @param v
     * @param msg
     */
    public UIError(@NonNull Activity a, @Nullable View v, @Nullable String msg) {
        this(a, v);
        this.msg = msg;
    }

    /**
     * displays SnackBar with default error message
     *
     * @param a
     * @param v
     */
    public UIError(@NonNull Activity a, @Nullable View v) {
        this(a);
        this.v = v;
    }

    /**
     * displays Toast with custom message
     *
     * @param a
     * @param msg
     */
    public UIError(@NonNull Activity a, @Nullable String msg) {
        this(a);
        this.msg = msg;
    }

    /**
     * displays Toast with throwable message
     *
     * @param a
     */
    public UIError(@NonNull Activity a) {
        this.a = a;
    }

    @Override
    public void call(final Throwable throwable) {
        boolean nullMessage = throwable.getMessage() == null;


        Log.e(Config.rxJavaError, throwable.getMessage());
        throwable.printStackTrace();
        a.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message = msg == null ? throwable.getMessage() : msg;
                if (v == null) {
                    Toast.makeText(a, message, Toast.LENGTH_LONG).show();
                } else {
                    Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }
}
