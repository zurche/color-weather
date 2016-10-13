package az.colorweather;

import java.util.List;

import az.colorweather.model.WeatherForecastElement;

/**
 * Created by az on 13/10/16.
 */

public interface WeatherContract {

    interface View {
        void updateFiveDayForecast(List<WeatherForecastElement> weatherForecastElement);

        void updateCurrentForecast();
    }

    interface Presenter {
        void getFiveDayForecast();

        void getCurrentDayForecast();
    }
}
