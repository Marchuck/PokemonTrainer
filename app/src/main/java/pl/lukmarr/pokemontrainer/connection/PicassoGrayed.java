package pl.lukmarr.pokemontrainer.connection;

import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by Łukasz Marczak
 *
 * @since 26.12.15
 */
public class PicassoGrayed implements Target {
    ImageView imageView;
    int width = 100;
    int height = 100;

    public PicassoGrayed(ImageView imageView, int x, int y) {
        this.imageView = imageView;
        width = x;
        height = y;
    }

    @Override
    public int hashCode() {
        return imageView.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PicassoGrayed) {
            ImageView marker = ((PicassoGrayed) o).imageView;
            return imageView.equals(marker);
        } else {
            return false;
        }
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        imageView.setImageDrawable(new BitmapDrawable(bitmap));
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        imageView.setColorFilter(new ColorMatrixColorFilter(matrix));
        imageView.setImageAlpha(90);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
