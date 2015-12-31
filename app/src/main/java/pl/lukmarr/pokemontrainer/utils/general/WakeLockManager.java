package pl.lukmarr.pokemontrainer.utils.general;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by Łukasz Marczak
 *
 * @since 31.12.15
 */
public class WakeLockManager {
    public static final String TAG = WakeLockManager.class.getSimpleName();

    private WakeLockManager() {
    }

    public static void acquire(PowerManager.WakeLock w, Context c) {
        Log.d(TAG, "acquireWakeLock ");
        PowerManager pm = (PowerManager) c.getSystemService(Context.POWER_SERVICE);
        w = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        w.acquire();
    }

    public static void releaseWakeLock(PowerManager.WakeLock w) {
        Log.d(TAG, "releaseWakeLock ");
        if (w != null)
            w.release();
        else Log.e(TAG, "releaseWakeLock null");
    }
}