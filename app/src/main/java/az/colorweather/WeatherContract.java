package az.colorweather;

import java.util.List;

import az.colorweather.api.model.current_day.CurrentWeather;
import az.colorweather.api.model.five_day.WeatherForecastElement;

/**
 * Created by az on 13/10/16.
 */

public interface WeatherContract {

    interface View {
        void updateFiveDayForecast(List<WeatherForecastElement> weatherForecastElement);

        void updateCurrentWeather(CurrentWeather currentWeather);
    }

    interface Presenter {
        void getFiveDayForecast();

        void getCurrentDayForecast();
    }
}
