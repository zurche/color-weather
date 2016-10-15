package az.colorweather.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import az.colorweather.R;
import az.colorweather.WeatherContract;
import az.colorweather.api.model.gson.common.Coord;
import az.colorweather.api.model.gson.current_day.CurrentWeather;
import az.colorweather.model.ForecastDay;
import az.colorweather.presenter.WeatherPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends AppCompatActivity implements WeatherContract.View {

    private static final String TAG = WeatherActivity.class.getSimpleName();

    @BindView(R.id.first_forecast_max)  TextView first_forecast_max;
    @BindView(R.id.second_forecast_max) TextView second_forecast_max;
    @BindView(R.id.third_forecast_max)  TextView third_forecast_max;
    @BindView(R.id.fourth_forecast_max) TextView fourth_forecast_max;
    @BindView(R.id.fifth_forecast_max)  TextView fifth_forecast_max;

    @BindView(R.id.first_forecast_min)  TextView first_forecast_min;
    @BindView(R.id.second_forecast_min) TextView second_forecast_min;
    @BindView(R.id.third_forecast_min)  TextView third_forecast_min;
    @BindView(R.id.fourth_forecast_min) TextView fourth_forecast_min;
    @BindView(R.id.fifth_forecast_min)  TextView fifth_forecast_min;

    @BindView(R.id.current_weather) TextView current_weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        ButterKnife.bind(this);

        WeatherPresenter presenter = new WeatherPresenter(this);

        Coord coordinate = new Coord();
        coordinate.setLat(-31.4245212);
        coordinate.setLon(-64.1884352);

        presenter.getFiveDayForecast(coordinate);
        presenter.getCurrentDayForecast(coordinate);
    }

    @Override
    public void updateFiveDayForecast(ArrayList<ForecastDay> forecastDays) {
        String maxDegrees = String.format(getString(R.string.degrees_placeholder),
                forecastDays.get(0).getMaxTempForecast().getMain().getTempMax().toString());
        String minDegrees = String.format(getString(R.string.degrees_placeholder),
                forecastDays.get(0).getMinTempForecast().getMain().getTempMax().toString());
        first_forecast_max.setText(maxDegrees);
        first_forecast_min.setText(minDegrees);

        maxDegrees = String.format(getString(R.string.degrees_placeholder),
                forecastDays.get(1).getMaxTempForecast().getMain().getTempMax().toString());
        minDegrees = String.format(getString(R.string.degrees_placeholder),
                forecastDays.get(1).getMinTempForecast().getMain().getTempMax().toString());
        second_forecast_max.setText(maxDegrees);
        second_forecast_min.setText(minDegrees);

        maxDegrees = String.format(getString(R.string.degrees_placeholder),
                forecastDays.get(2).getMaxTempForecast().getMain().getTempMax().toString());
        minDegrees = String.format(getString(R.string.degrees_placeholder),
                forecastDays.get(2).getMinTempForecast().getMain().getTempMax().toString());
        third_forecast_max.setText(maxDegrees);
        third_forecast_min.setText(minDegrees);

        maxDegrees = String.format(getString(R.string.degrees_placeholder),
                forecastDays.get(3).getMaxTempForecast().getMain().getTempMax().toString());
        minDegrees = String.format(getString(R.string.degrees_placeholder),
                forecastDays.get(3).getMinTempForecast().getMain().getTempMax().toString());
        fourth_forecast_max.setText(maxDegrees);
        fourth_forecast_min.setText(minDegrees);

        maxDegrees = String.format(getString(R.string.degrees_placeholder),
                forecastDays.get(4).getMaxTempForecast().getMain().getTempMax().toString());
        minDegrees = String.format(getString(R.string.degrees_placeholder),
                forecastDays.get(4).getMinTempForecast().getMain().getTempMax().toString());
        fifth_forecast_max.setText(maxDegrees);
        fifth_forecast_min.setText(minDegrees);

    }

    @Override
    public void updateCurrentWeather(CurrentWeather currentWeather) {
        Log.d(TAG, currentWeather.getMain().getTemp() + "°");
        String degrees = String.format(getString(R.string.degrees_placeholder),
                currentWeather.getMain().getTempMax().toString());
        current_weather.setText(degrees);
    }
}
