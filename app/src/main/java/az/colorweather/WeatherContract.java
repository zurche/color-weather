package az.colorweather;

import java.util.ArrayList;

import az.openweatherapi.model.gson.common.Coord;
import az.colorweather.model.ForecastDay;
import az.colorweather.util.Temperature;

/**
 * Created by az on 13/10/16.
 */

public interface WeatherContract {

    interface View {
        void updateFiveDayForecast(ArrayList<ForecastDay> weatherForecastElement);

        void updateCurrentDayWeather(ArrayList<ForecastDay> currentWeather);
    }

    interface Presenter {
        void getFiveDayForecast(final Coord coordinate);

        void getCurrentDayForecast();

        Temperature getColorForTemp(int temp);
    }
}
