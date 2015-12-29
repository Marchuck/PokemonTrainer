package pl.lukmarr.pokemontrainer.adapters;

import android.app.Activity;
import android.content.Context;
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
import pl.lukmarr.pokemontrainer.connection.PicassoGrayed;
import pl.lukmarr.pokemontrainer.utils.GenericTrio;

/**
 * Created by Łukasz Marczak
 *
 * @since 29.12.15
 */

public class CustomGridViewAdapter extends ArrayAdapter<GenericTrio<Boolean, Integer, String>> {
    Context context;
    int layoutResourceId;
    List<GenericTrio<Boolean, Integer, String>> data = new ArrayList<>();

    public CustomGridViewAdapter(Context context, @LayoutRes int layoutResourceId,
                                 @NonNull List<GenericTrio<Boolean, Integer, String>> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
            holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        GenericTrio<Boolean, Integer, String> item = data.get(position);
        holder.txtTitle.setText(item.third);

        if (item.first) {
            Picasso.with(context).load(item.second).into(holder.imageItem);
        } else {
            PicassoGrayed grayed = new PicassoGrayed(holder.imageItem, 80, 80);
            Picasso.with(context).load(item.second).into(grayed);
        }
        return row;
    }

    static class RecordHolder {
        TextView txtTitle;
        ImageView imageItem;
    }
}