package az.colorweather.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import az.colorweather.WeatherContract;
import az.colorweather.presenter.OpenWeatherAPI;
import az.colorweather.R;
import az.colorweather.model.ExtendedWeather;
import az.colorweather.presenter.WeatherPresenter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class WeatherActivity extends AppCompatActivity implements WeatherContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        WeatherPresenter presenter = new WeatherPresenter(this);

        presenter.getFiveDayForecast();
    }

    @Override
    public void updateFiveDayForecast() {

    }

    @Override
    public void updateCurrentForecast() {

    }
}
