package az.colorweather.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import az.colorweather.WeatherContract;
import az.colorweather.api.model.current_day.CurrentWeather;
import az.colorweather.api.model.five_day.WeatherForecastElement;
import az.colorweather.R;
import az.colorweather.presenter.WeatherPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherActivity extends AppCompatActivity implements WeatherContract.View {

    private static final String TAG = WeatherActivity.class.getSimpleName();

    @BindView(R.id.first_forecast)  TextView first_forecast;
    @BindView(R.id.second_forecast) TextView second_forecast;
    @BindView(R.id.third_forecast)  TextView third_forecast;
    @BindView(R.id.fourth_forecast) TextView fourth_forecast;
    @BindView(R.id.fifth_forecast)  TextView fifth_forecast;

    @BindView(R.id.current_weather) TextView current_weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        ButterKnife.bind(this);

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
        Log.d(TAG, currentWeather.getMain().getTemp() + "°");
        String degrees = String.format(getString(R.string.degrees_placeholder),
                currentWeather.getMain().getTemp().toString());
        current_weather.setText(degrees);
    }
}
