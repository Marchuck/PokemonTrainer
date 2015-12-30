package pl.lukmarr.pokemontrainer.entities;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.trnql.smart.weather.WeatherEntry;

import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.utils.dialogs.AbstractDialog;

/**
 * Created by Łukasz Marczak
 *
 * @since 30.12.15
 */
public class WeatherDisplayer extends AbstractDialog {
    public static final String TAG = WeatherDisplayer.class.getSimpleName();


    public WeatherDisplayer(FragmentActivity activity, @NonNull WeatherEntry weather) {
        super(null);
        prepareDialog(activity);
        dialog.setContentView(R.layout.weather_dialog);

        TextView weatherText = (TextView) dialog.findViewById(R.id.password);
        TextView login = (TextView) dialog.findViewById(R.id.login);
        Log.d(TAG, "Weather: ");
        Log.d(TAG, "summary: " + weather.getWeatherSummaryAsString());
        Log.d(TAG, "normalWindSpeed: " + weather.getNormalWindSpeed());
        Log.d(TAG, "gustWindSpeed: " + weather.getGustWindSpeed());
        Log.d(TAG, "windAsString: " + weather.getWindAsString());
        Log.d(TAG, "rain: " + weather.getRain());
        Log.d(TAG, "currentConditions as String: " + weather.getCurrentConditionsDescriptionAsString());
        Log.d(TAG, "forecastAsString: " + weather.getForecastAsString());
        Log.d(TAG, "HiLoAsString: " + weather.getHiLoAsString());
        Log.d(TAG, "feelsLikeAsString: " + weather.getFeelsLikeAsString());
        Log.d(TAG, "UIIndex: " + weather.getUVIndexAsString());

        weatherText.setText(weather.getCurrentConditionsDescriptionAsString());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
