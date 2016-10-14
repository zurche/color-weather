package az.colorweather.presenter;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import az.colorweather.WeatherContract;
import az.colorweather.api.OpenWeatherAPI;
import az.colorweather.api.model.current_day.CurrentWeather;
import az.colorweather.api.model.five_day.ExtendedWeather;
import az.colorweather.api.model.five_day.WeatherForecastElement;
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

public class WeatherPresenter implements WeatherContract.Presenter {

    private static final String TAG = WeatherPresenter.class.getSimpleName();
    private WeatherContract.View view;
    private SimpleDateFormat weatherDateStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
        Call<ExtendedWeather> call = openWeatherAPI.getFiveDayExtendedWeather(
                -31.416,
                -64.186,
                "b97081fb50c5b5c19841ec6ae4f5daec",
                OWSupportedUnits.METRIC.getUnit(),
                OWSupportedLanguages.SPANISH.getLanguageLocale());
        call.enqueue(new Callback<ExtendedWeather>() {
            @Override
            public void onResponse(Response<ExtendedWeather> response, Retrofit retrofit) {
                ExtendedWeather extendedWeather = response.body();

                for (WeatherForecastElement weather : extendedWeather.getList()) {
                    Log.d(TAG, weather.getDtTxt() + " - Got Temp: " + weather.getMain().getTemp() + "°");
                }

                ArrayList<WeatherForecastElement> curatedList = filterMidDayWeatherForecasts(extendedWeather);
                view.updateFiveDayForecast(curatedList);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "Five Day Forecast request failed: " + t.getMessage());
            }
        });
    }

    private ArrayList<WeatherForecastElement> filterMidDayWeatherForecasts(ExtendedWeather extendedWeather) {

        ArrayList<Integer> addedDays = new ArrayList();
        ArrayList<WeatherForecastElement> curatedFiveDayforecast = new ArrayList();

        for (int i = 0; i < extendedWeather.getList().size(); i++) {
            Date parsedDate = new Date();
            try {
                parsedDate = weatherDateStampFormat.parse(extendedWeather.getList().get(i).getDtTxt());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

            if (!addedDays.contains(currentDay)) {
                addedDays.add(currentDay);
                curatedFiveDayforecast.add(extendedWeather.getList().get(i));
            }
        }

        //TODO: Filter Max and Min temperature of any given day and create ArrayList<FrecastDay>
        //that contains the 5 day forecast with min and max values for each day.

        return curatedFiveDayforecast;
    }

    @Override
    public void getCurrentDayForecast() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeatherAPI openWeatherAPI = retrofit.create(OpenWeatherAPI.class);
        Call<CurrentWeather> call = openWeatherAPI.getCurrentWeather(
                -31.416,
                -64.186,
                "b97081fb50c5b5c19841ec6ae4f5daec",
                OWSupportedUnits.METRIC.getUnit(),
                OWSupportedLanguages.SPANISH.getLanguageLocale());
        call.enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Response<CurrentWeather> response, Retrofit retrofit) {
                CurrentWeather currentWeather = response.body();
                Log.d(TAG, "Got Current Weather!: " + currentWeather.getWeather().get(0).getDescription());
                view.updateCurrentWeather(currentWeather);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "Current Weather request failed: " + t.getMessage());
            }
        });
    }
}
