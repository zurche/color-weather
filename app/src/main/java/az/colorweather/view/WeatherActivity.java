package az.colorweather.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import az.colorweather.WeatherContract;
import az.colorweather.model.current_day.CurrentWeather;
import az.colorweather.model.five_day.WeatherForecastElement;
import az.colorweather.R;
import az.colorweather.presenter.WeatherPresenter;

public class WeatherActivity extends AppCompatActivity implements WeatherContract.View {

    private static final String TAG = WeatherActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        WeatherPresenter presenter = new WeatherPresenter(this);

        presenter.getFiveDayForecast();
        presenter.getCurrentDayForecast();
    }

    @Override
    public void updateFiveDayForecast(List<WeatherForecastElement> weatherForecastElement) {
        for (WeatherForecastElement weather : weatherForecastElement) {
            Log.d(TAG, weather.getDtTxt() + " - Got Temp: " + weather.getMain().getTemp() + "°");
        }

    }

    @Override
    public void updateCurrentWeather(CurrentWeather currentWeather) {

    }
}
