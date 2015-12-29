package pl.lukmarr.pokemontrainer.adapters;

/**
 * Created by Łukasz Marczak
 *
 * @since 29.12.15
 */

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trnql.smart.people.PersonEntry;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.lukmarr.pokemontrainer.R;

public class TrainersAdapter extends RecyclerView.Adapter<TrainersAdapter.TrainersAdapterHolder> {
    public static final String TAG = TrainersAdapter.class.getSimpleName();

    public List<PersonEntry> dataSet = new ArrayList<>();
    @NonNull
    android.content.Context context;

    public TrainersAdapter(@NonNull android.content.Context context,
                           @Nullable List<PersonEntry> dataSet) {
        this.context = context;
        if (dataSet != null)
            this.dataSet.addAll(dataSet);
        setHasStableIds(false);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public TrainersAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item,
                parent, false);
        return new TrainersAdapterHolder(view);
    }


    @Override
    public void onBindViewHolder(final TrainersAdapterHolder holder, final int position) {
        PersonEntry item = dataSet.get(position);

        int dist = item.getDistanceFromUser();
        holder.name.setText("Opponent is " + dist + " meters far");
        if (position % 2 == 0)
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.smoothlyDarker));
    }

    public void refresh(@NonNull List<PersonEntry> newDataSet) {
        dataSet.clear();
        dataSet.addAll(newDataSet);
        notifyItemRangeChanged(0, getItemCount());
        notifyDataSetChanged();
    }

    static class TrainersAdapterHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.name)
        TextView name;

        public TrainersAdapterHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
