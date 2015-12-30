package pl.lukmarr.pokemontrainer.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.utils.Triple;

/**
 * Created by Łukasz Marczak
 *
 * @since 29.12.15
 */

public class CustomGridViewAdapter extends ArrayAdapter<Triple<Boolean, Integer, String>> {
    Context context;
    final int layoutResourceId;

    /**
     *
     */
    List<Triple<Boolean, Integer, String>> data = new ArrayList<>();

    public CustomGridViewAdapter(Context context, @LayoutRes int layoutResourceId,
                                 @NonNull List<Triple<Boolean, Integer, String>> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder;

//        if (row == null) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new RecordHolder();
        holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
        holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
        row.setTag(holder);
//        } else {
//            holder = (RecordHolder) row.getTag();
//        }

        Triple<Boolean, Integer, String> item = data.get(position);
        holder.txtTitle.setText(item.third);
        Picasso.with(context).load(item.second).into(holder.imageItem);

        if (!item.first) {
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            holder.imageItem.setColorFilter(new ColorMatrixColorFilter(matrix));
            holder.imageItem.setImageAlpha(90);
        }
        return row;
    }

    static class RecordHolder {
        TextView txtTitle;
        ImageView imageItem;
    }
}