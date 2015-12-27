package pl.lukmarr.pokemontrainer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

/**
 * Created by Łukasz Marczak
 *
 * @since 27.12.15
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.VH> {

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
        items.add(new Pair<>("Map", android.R.drawable.ic_delete));
        items.add(new Pair<>("Pokedex", android.R.drawable.ic_delete));
        items.add(new Pair<>("Badges", android.R.drawable.ic_delete));
        items.add(new Pair<>("Details", android.R.drawable.ic_delete));
        items.add(new Pair<>("Exit", android.R.drawable.ic_delete));
        this.listener = listener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item, null);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        holder.text.setText(items.get(position).first);
        Picasso.with(context).load(items.get(position).second).into(holder.icon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClicked(position);
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
