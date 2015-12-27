package pl.lukmarr.pokemontrainer.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.connection.PicassoGrayed;
import pl.lukmarr.pokemontrainer.database.RealmPoke;


public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ItemViewHolder> {
    public static final String TAG = PokemonAdapter.class.getSimpleName();
    List<RealmPoke> mItems = new ArrayList<>();
    ColorMatrixColorFilter filter;
    Context context;

    private void setupGrayMatrix() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        filter = new ColorMatrixColorFilter(matrix);
    }

    public PokemonAdapter(@NonNull List<RealmPoke> dataSet, @NonNull Activity context) {
        mItems.addAll(dataSet);
        this.context = context;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_pokedex_item, parent, false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        RealmPoke pokemon = mItems.get(position);
        if (!pokemon.isDiscovered()) {
            PicassoGrayed gr  = new PicassoGrayed(holder.image);
            Picasso.with(context).load(pokemon.getImage()).into(gr);
            holder.name.setText("???");
            Log.d(TAG, "onBindViewHolder " + position + ", " + pokemon.getName());
        }else{
            Picasso.with(context).load(pokemon.getImage()).fit().into(holder.image);
            holder.name.setText(pokemon.getName());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        public ImageView image;
        @Bind(R.id.name)
        public TextView name;

        public ItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
