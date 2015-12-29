package pl.lukmarr.pokemontrainer.utils.interfaces;

import com.trnql.smart.location.AddressEntry;
import com.trnql.smart.weather.WeatherEntry;

/**
 * Created by Łukasz Marczak
 *
 * @since 29.12.15
 */
public interface EnvironmentConnector {
    void onWeatherChange(WeatherEntry weatherEntry);
    void onAddressChange(AddressEntry addressEntry);
}
