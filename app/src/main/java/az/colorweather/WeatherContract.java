package az.colorweather;

/**
 * Created by az on 13/10/16.
 */

public interface WeatherContract {

    interface View {
        void updateFiveDayForecast();
        void updateCurrentForecast();
    }

    interface Presenter {
        void getFiveDayForecast();
        void getCurrentDayForecast();
    }
}
