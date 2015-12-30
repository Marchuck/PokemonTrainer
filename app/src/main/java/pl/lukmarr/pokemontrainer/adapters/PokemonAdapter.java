package pl.lukmarr.pokemontrainer.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import pl.lukmarr.pokemontrainer.config.Config;
import pl.lukmarr.pokemontrainer.connection.DataFetcher;
import pl.lukmarr.pokemontrainer.connection.UIAction;
import pl.lukmarr.pokemontrainer.connection.UIError;
import pl.lukmarr.pokemontrainer.database.RealmPoke;
import pl.lukmarr.pokemontrainer.entities.activities.NewPokemonActivity;
import pl.lukmarr.pokemontrainer.utils.interfaces.ListCallback;


public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ItemViewHolder> {
    public static final String TAG = PokemonAdapter.class.getSimpleName();
    List<RealmPoke> mItems = new ArrayList<>();
    ColorMatrixColorFilter filter;
    Activity activity;
    public static int lastScrolledPosition = 0;

    DataFetcher morePokesFetcher;
    View progressView, recyclerView;

    public PokemonAdapter(@NonNull List<RealmPoke> dataSet, @NonNull Activity context,
                          @NonNull View progressView, @NonNull RecyclerView recyclerView) {
        mItems.addAll(dataSet);
        this.activity = context;
        this.progressView = progressView;
        this.recyclerView = recyclerView;
        morePokesFetcher = new DataFetcher();
        setHasStableIds(false);
        if (lastScrolledPosition <= getItemCount() && lastScrolledPosition >= 0)
            recyclerView.scrollToPosition(lastScrolledPosition);
    }

    public void refresh(@NonNull List<RealmPoke> dataSet) {
        Log.d(TAG, "refresh with" + dataSet.size() + " pokes");
        mItems.clear();
        mItems.addAll(dataSet);
        notifyItemRangeChanged(0, getItemCount());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_pokedex_item,
                parent, false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        final RealmPoke pokemon = mItems.get(position);
        Log.d(TAG, "onBindViewHolder " + position + ", " + pokemon.getName()
                + ", lastScroll = " + lastScrolledPosition);

        if (pokemon.isDiscovered()) {
            Picasso.with(activity).load(pokemon.getImage()).fit().into(holder.image);
            holder.name.setText(pokemon.getName());
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startDetailActivity(pokemon);
                }
            });
            lastScrolledPosition = position;
        } else {
            Picasso.with(activity).load(R.drawable.question_mark).fit().into(holder.image);
            holder.name.setText("Undiscovered");
        }
        //load more pokemons
        if (position == 150) {
            progressView.setVisibility(View.GONE);
            Snackbar.make(recyclerView, "No more pokes in demo version!", Snackbar.LENGTH_SHORT).show();
        } else if (position < 151 && position == mItems.size() - 1) {
            fetchMorePokes(position);
        }
    }

    private void startDetailActivity(RealmPoke pokemon) {
        Intent intent = NewPokemonActivity.buildIntent(pokemon, "", activity);
        activity.startActivity(intent);
    }

    private void fetchMorePokes(int position) {
        Log.d(TAG, "fetchMorePokes ");
        List<Integer> morePokes = new ArrayList<>();
        int defaultSize = position + 1 + Config.INCREMENT_POKES;
        int maxSize = defaultSize < 152 ? defaultSize : 152;
        for (int j = position + 1; j < maxSize; j++)
            morePokes.add(j);
        if (morePokes.size() == 0) return;
        morePokesFetcher.onError = new UIError(activity);
        morePokesFetcher.onSubscribe = new UIAction(activity, progressView, true);
        morePokesFetcher.onCompleted = new UIAction(activity, progressView, false);
        morePokesFetcher.fetchPokes(activity, morePokes, new ListCallback<RealmPoke>() {
            @Override
            public void onListReceived(List<RealmPoke> list) {
                mItems.clear();
                mItems.addAll(list);
                notifyItemRangeChanged(0, getItemCount());
                notifyDataSetChanged();
            }
        });
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
