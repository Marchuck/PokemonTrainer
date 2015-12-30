package pl.lukmarr.pokemontrainer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
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
import pl.lukmarr.pokemontrainer.utils.IntentBuilder;
import pl.lukmarr.pokemontrainer.utils.RandUtils;

/**
 * Created by Łukasz Marczak
 *
 * @since 27.12.15
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.VH> {
    private static int lastPosition = -1;
    private final Listener listener;
    List<Pair<String, Integer>> items = new ArrayList<>();
    Context context;

    public interface Listener {
        void onClicked(int j);
    }

    public static DrawerAdapter create(Context c, Listener listener) {
        return new DrawerAdapter(c, listener);
    }

    private DrawerAdapter(Context c, Listener listener) {
        context = c;
        items.add(new Pair<>("Map", R.drawable.map));
        items.add(new Pair<>("Pokedex", R.drawable.cat));
        items.add(new Pair<>("Badges", R.drawable.star_icon));
        items.add(new Pair<>("Trainers nearby", R.drawable.walk));
        items.add(new Pair<>("Details", R.drawable.dots_horizontal));
        items.add(new Pair<>("About", R.drawable.message_text));
        items.add(new Pair<>("Exit", R.drawable.exit_to_app));
        this.listener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item, null);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        if (position == lastPosition) {
            holder.text.setTextColor(context.getResources().getColor(android.R.color.black));
        } else {
            holder.text.setTextColor(context.getResources().getColor(android.R.color.white));
        }

        holder.text.setText(items.get(position).first);
        Picasso.with(context).load(items.get(position).second).into(holder.icon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClicked(position);
                lastPosition = position;
                notifyDataSetChanged();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (position == 3) {
                    Log.d("", "onLongClick ");
                    context.startActivity(IntentBuilder
                            .NewPokemonActivityBuilder(context,
                                    RandUtils.create().randomPokeButUnique(context)));
                }
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {

        @Bind(R.id.image)
        ImageView icon;
        @Bind(R.id.name)
        TextView text;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
