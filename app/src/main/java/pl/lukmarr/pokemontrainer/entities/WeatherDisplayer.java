package pl.lukmarr.pokemontrainer.entities;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trnql.smart.weather.WeatherEntry;

import java.util.Random;

import pl.lukmarr.pokemontrainer.R;
import pl.lukmarr.pokemontrainer.utils.dialogs.AbstractDialog;

/**
 * Created by Łukasz Marczak
 *
 * @since 30.12.15
 */
public class WeatherDisplayer extends AbstractDialog {
    public static final String TAG = WeatherDisplayer.class.getSimpleName();

    public WeatherDisplayer(Activity activity, @NonNull String weather) {
        super(null);
        prepareDialog(activity);
        dialog.setContentView(R.layout.weather_dialog);

        TextView weatherText = (TextView) dialog.findViewById(R.id.password);
        weatherText.setText(weather);
        TextView login = (TextView) dialog.findViewById(R.id.login);
        ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancel.setImageResource(getRandRes());
        dialog.show();
    }

    public WeatherDisplayer(Activity activity, @NonNull WeatherEntry weather) {
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
        ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel);
        cancel.setImageResource(getRandRes());
        weatherText.setText(weather.getCurrentConditionsDescriptionAsString());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static float farentheit2Celcius(float value) {
        return 32 + 9 * value / 5;
    }

    public static float celcius2Farentheit(float value) {
        return 5 * (value - 32) / 9;
    }

    @DrawableRes
    public static int getRandRes() {
        int[] res = new int[]{
                R.drawable.weather_cloudy,
                R.drawable.weather_fog,
                R.drawable.weather_hail,
                R.drawable.weather_lightning,
                R.drawable.weather_partlycloudy,
                R.drawable.weather_pouring,
                R.drawable.weather_rainy,
                R.drawable.weather_snowy,
                R.drawable.weather_sunny,
                R.drawable.weather_windy,
                R.drawable.weather_windy_variant,
        };

        return res[new Random().nextInt(res.length)];
    }
}
