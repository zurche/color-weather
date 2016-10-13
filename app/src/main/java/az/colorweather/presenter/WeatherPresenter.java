package az.colorweather.presenter;

import android.util.Log;

import az.colorweather.WeatherContract;
import az.colorweather.model.ExtendedWeather;
import az.colorweather.utils.OWSupportedLanguages;
import az.colorweather.utils.OWSupportedUnits;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by az on 13/10/16.
 */

public class WeatherPresenter implements WeatherContract.Presenter, Callback<ExtendedWeather> {

    private static final String TAG = WeatherPresenter.class.getSimpleName();
    private WeatherContract.View view;

    public WeatherPresenter(WeatherContract.View view) {
        this.view = view;
    }

    @Override
    public void getFiveDayForecast() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeatherAPI openWeatherAPI = retrofit.create(OpenWeatherAPI.class);
        Call<ExtendedWeather> call = openWeatherAPI.loadExtendedWeather(
                -31.416,
                -64.186,
                "b97081fb50c5b5c19841ec6ae4f5daec",
                OWSupportedUnits.METRIC.getUnit(),
                OWSupportedLanguages.SPANISH.getLanguageLocale());
        call.enqueue(this);
    }

    @Override
    public void getCurrentDayForecast() {

    }

    @Override
    public void onResponse(Response<ExtendedWeather> response, Retrofit retrofit) {
        ExtendedWeather extendedWeather = response.body();
        Log.d(TAG, "Got Weather!: " + extendedWeather.getCity().getName());
        view.updateFiveDayForecast(extendedWeather.getList());
    }

    @Override
    public void onFailure(Throwable t) {
        Log.d(TAG, "Five Day Forecast request failed: " + t.getMessage());
    }
}
