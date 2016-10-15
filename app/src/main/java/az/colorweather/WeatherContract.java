package az.colorweather;

import java.util.ArrayList;

import az.colorweather.api.model.gson.common.Coord;
import az.colorweather.api.model.gson.current_day.CurrentWeather;
import az.colorweather.api.model.gson.five_day.WeatherForecastElement;

/**
 * Created by az on 13/10/16.
 */

public interface WeatherContract {

    interface View {
        void updateFiveDayForecast(ArrayList<WeatherForecastElement> weatherForecastElement);

        void updateCurrentWeather(CurrentWeather currentWeather);
    }

    interface Presenter {
        void getFiveDayForecast(final Coord coordinate);

        void getCurrentDayForecast(final Coord coordinate);
    }
}
