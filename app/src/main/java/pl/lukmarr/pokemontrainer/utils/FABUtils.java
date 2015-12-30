package pl.lukmarr.pokemontrainer.utils;

import android.content.res.ColorStateList;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;

import pl.lukmarr.pokemontrainer.R;

import static android.util.Log.d;

/**
 * Created by Łukasz Marczak
 *
 * @since 30.12.15
 */
public class FABUtils {
    public static final String TAG = FABUtils.class.getSimpleName();

    private FABUtils() {
    }

    public static void setupFAB(FragmentActivity activity, FloatingActionButton fab, @DrawableRes int imageResource) {
        d(TAG, "inside setupFAB ");
        int color = activity.getResources().getColor(R.color.accent_color);
        int whenPressedColor = activity.getResources().getColor(R.color.accent_color_darker);
        fab.setImageResource(imageResource);
        fab.setBackgroundTintList(ColorStateList.valueOf(color));
        fab.setRippleColor(whenPressedColor);

    }
}
