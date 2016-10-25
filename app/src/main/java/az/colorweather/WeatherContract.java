package az.colorweather;

import java.util.ArrayList;

import az.colorweather.model.ForecastDay;
import az.colorweather.util.Temperature;
import az.openweatherapi.model.gson.common.Coord;
import az.openweatherapi.model.gson.current_day.CurrentWeather;

/**
 * Actions that can should be performed by both the View and its Presenter.
 */
public interface WeatherContract {

    interface View {
        void updateFiveDayForecast(ArrayList<ForecastDay> weatherForecastElement);

        void updateCurrentDayExtendedForecast(ArrayList<ForecastDay> currentWeather);

        void updateTodayForecast(CurrentWeather currentWeather);
    }

    interface Presenter {
        void getFiveDayForecast(final Coord coordinate);

        void getCurrentDayExtendedForecast();

        Temperature getColorForTemp(int temp);
    }
}
