package pl.lukmarr.pokemontrainer.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

/**
 * Created by lukasz on 14.11.15.
 */
public abstract class AbstractDialog {
    public interface DialogInteractionListener{
        void afterOk(String s);
        void afterCancel();

    }
    public static final String TAG = AbstractDialog.class.getSimpleName();
    protected DialogInteractionListener listener;
    protected Dialog dialog;
    private boolean notPrepared = true;
    protected boolean isVisible = false;


    public AbstractDialog(DialogInteractionListener listener) {
        this.listener = listener;
    }

    public void prepareDialog(Context context) {
        dialog = new Dialog(context);
        //disables title
        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        //this makes cardView look
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        notPrepared = false;
    }

    /**
     * remember to call prepareDialog at beginning of this method
     */
    public void show() {
        if (notPrepared) {
            String errorMessage = "Dialog not prepared. Call prepareDialog(context) first";
            Log.e(TAG, errorMessage);
            throw new IllegalStateException(errorMessage);
        }
        Log.d(TAG, "show dialog");
        dialog.show();
        isVisible = true;
    }
}
