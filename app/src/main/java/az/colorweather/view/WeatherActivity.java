package az.colorweather.view;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

import az.colorweather.R;
import az.colorweather.util.Temperature;
import az.colorweather.WeatherContract;
import az.colorweather.api.model.gson.common.Coord;
import az.colorweather.api.model.gson.current_day.CurrentWeather;
import az.colorweather.model.ForecastDay;
import az.colorweather.presenter.WeatherPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends AppCompatActivity implements WeatherContract.View {

    private static final String TAG = WeatherActivity.class.getSimpleName();

    private WeatherPresenter presenter;

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

        presenter = new WeatherPresenter(this);

        Coord coordinate = new Coord();
        coordinate.setLat(32.8998091);
        coordinate.setLon(-97.0425239);

        presenter.getFiveDayForecast(coordinate);
        presenter.getCurrentDayForecast(coordinate);
    }

    @Override
    public void updateFiveDayForecast(ArrayList<ForecastDay> forecastDays) {
        int roundedMaxTemp = (int) Math.round(forecastDays.get(0).getMaxTempForecast().getMain().getTempMax());
        String maxDegrees = String.format(getString(R.string.degrees_placeholder), roundedMaxTemp);
        first_forecast_max.setText(maxDegrees);
        first_forecast_max.setTextColor(getColor(presenter.getColorForTemp(roundedMaxTemp)));

        int roundedMinTemp = (int) Math.round(forecastDays.get(0).getMinTempForecast().getMain().getTempMax());
        String minDegrees = String.format(getString(R.string.degrees_placeholder),roundedMinTemp);
        first_forecast_min.setText(minDegrees);
        first_forecast_min.setTextColor(getColor(presenter.getColorForTemp(roundedMinTemp)));

        roundedMaxTemp = (int) Math.round(forecastDays.get(1).getMaxTempForecast().getMain().getTempMax());
        maxDegrees = String.format(getString(R.string.degrees_placeholder), roundedMaxTemp);
        second_forecast_max.setText(maxDegrees);
        second_forecast_max.setTextColor(getColor(presenter.getColorForTemp(roundedMaxTemp)));

        roundedMinTemp = (int) Math.round(forecastDays.get(1).getMinTempForecast().getMain().getTempMax());
        minDegrees = String.format(getString(R.string.degrees_placeholder),roundedMinTemp);
        second_forecast_min.setText(minDegrees);
        second_forecast_min.setTextColor(getColor(presenter.getColorForTemp(roundedMinTemp)));

        roundedMaxTemp = (int) Math.round(forecastDays.get(2).getMaxTempForecast().getMain().getTempMax());
        maxDegrees = String.format(getString(R.string.degrees_placeholder), roundedMaxTemp);
        third_forecast_max.setText(maxDegrees);
        third_forecast_max.setTextColor(getColor(presenter.getColorForTemp(roundedMaxTemp)));

        roundedMinTemp = (int) Math.round(forecastDays.get(2).getMinTempForecast().getMain().getTempMax());
        minDegrees = String.format(getString(R.string.degrees_placeholder),roundedMinTemp);
        third_forecast_min.setText(minDegrees);
        third_forecast_min.setTextColor(getColor(presenter.getColorForTemp(roundedMinTemp)));

        roundedMaxTemp = (int) Math.round(forecastDays.get(3).getMaxTempForecast().getMain().getTempMax());
        maxDegrees = String.format(getString(R.string.degrees_placeholder), roundedMaxTemp);
        fourth_forecast_max.setText(maxDegrees);
        fourth_forecast_max.setTextColor(getColor(presenter.getColorForTemp(roundedMaxTemp)));

        roundedMinTemp = (int) Math.round(forecastDays.get(3).getMinTempForecast().getMain().getTempMax());
        minDegrees = String.format(getString(R.string.degrees_placeholder),roundedMinTemp);
        fourth_forecast_min.setText(minDegrees);
        fourth_forecast_min.setTextColor(getColor(presenter.getColorForTemp(roundedMinTemp)));

        roundedMaxTemp = (int) Math.round(forecastDays.get(4).getMaxTempForecast().getMain().getTempMax());
        maxDegrees = String.format(getString(R.string.degrees_placeholder), roundedMaxTemp);
        fifth_forecast_max.setText(maxDegrees);
        fifth_forecast_max.setTextColor(getColor(presenter.getColorForTemp(roundedMaxTemp)));

        roundedMinTemp = (int) Math.round(forecastDays.get(4).getMinTempForecast().getMain().getTempMax());
        minDegrees = String.format(getString(R.string.degrees_placeholder),roundedMinTemp);
        fifth_forecast_min.setText(minDegrees);
        fifth_forecast_min.setTextColor(getColor(presenter.getColorForTemp(roundedMinTemp)));

    }

    @Override
    public void updateCurrentWeather(CurrentWeather currentWeather) {
        int roundedTemp = (int) Math.round(currentWeather.getMain().getTemp());
        String degrees = String.format(getString(R.string.degrees_placeholder), roundedTemp);
        current_weather.setText(degrees);
        current_weather.setTextColor(getColor(presenter.getColorForTemp(roundedTemp)));
    }

    private int getColor(Temperature colorTemp) {
        int color = 0;

        switch (colorTemp) {
            case SUPER_HOT:
                color = ContextCompat.getColor(this, R.color.super_hot);
                break;
            case MEDIUM_HOT:
                color = ContextCompat.getColor(this, R.color.medium_hot);
                break;
            case HOT:
                color = ContextCompat.getColor(this, R.color.hot);
                break;
            case OK:
                color = ContextCompat.getColor(this, R.color.ok);
                break;
            case OK_CHILL:
                color = ContextCompat.getColor(this, R.color.ok_chill);
                break;
            case COLD:
                color = ContextCompat.getColor(this, R.color.cold);
                break;
        }

        return color;
    }
}
