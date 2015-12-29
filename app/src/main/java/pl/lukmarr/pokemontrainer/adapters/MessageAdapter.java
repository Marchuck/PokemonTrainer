package pl.lukmarr.pokemontrainer.adapters;

/**
 * Created by Łukasz Marczak
 *
 * @since 29.12.15
 */

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trnql.smart.location.AddressEntry;
import com.trnql.smart.weather.WeatherEntry;

import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.utils.interfaces.EnvironmentConnector;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterHolder>
        implements EnvironmentConnector {
    public static final String TAG = MessageAdapter.class.getSimpleName();
    public View behindView;
    String lastAddressEntry = "";
    String lastWeatherEntry = "";


    @NonNull
    android.content.Context context;

    public MessageAdapter(@NonNull android.content.Context context, View behindView) {
        Log.d(TAG, "MessageAdapter ");
        this.context = context;
        this.behindView = behindView;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;
        return 1;
    }

    @Override
    public MessageAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder ");
        View view;
        if (viewType == 0) {
            RelativeLayout view1 = new RelativeLayout(context);
//            view1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    pl.lukmarr.pokemontrainer.config.Config.transparentLayoutHeight));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    pl.lukmarr.pokemontrainer.config.Config.transparentLayoutHeight);

//            params.addRule(RelativeLayout.ALIGN_RIGHT);
            view1.setLayoutParams(params);
            view = view1;
        } else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.long_description_item, parent, false);
        return new MessageAdapterHolder(view, viewType, behindView, context);
    }

    @Override
    public void onBindViewHolder(final MessageAdapterHolder holder, final int position) {
        if (position == 1) {
            holder.address.setText(lastAddressEntry);
            holder.weather.setText(lastWeatherEntry);
        }
    }

    @Override
    public void onWeatherChange(WeatherEntry weatherEntry) {
        lastWeatherEntry = weatherEntry.getWeatherSummaryAsString();
        notifyDataSetChanged();
    }

    @Override
    public void onAddressChange(AddressEntry addressEntry) {
        lastAddressEntry = addressEntry.getFeatureName();
        notifyDataSetChanged();
    }


    static class MessageAdapterHolder extends RecyclerView.ViewHolder {

        public TextView address, weather;

        public MessageAdapterHolder(View v, int viewType, final View behindView, Context context) {
            super(v);
            if (viewType == 1) {
                address = (TextView) v.findViewById(R.id.address);
                weather = (TextView) v.findViewById(R.id.weather);
            } else {
                v.setBackgroundColor(Color.TRANSPARENT);
                v.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        return behindView.dispatchTouchEvent(event);
                    }
                });
            }
        }
    }
}
