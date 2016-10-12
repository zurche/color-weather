package az.colorweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import az.colorweather.model.ExtendedWeather;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class WeatherActivity extends AppCompatActivity implements Callback<ExtendedWeather> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeatherAPI openWeatherAPI = retrofit.create(OpenWeatherAPI.class);

        Call<ExtendedWeather> call = openWeatherAPI.loadExtendedWeather(35,139, "b97081fb50c5b5c19841ec6ae4f5daec");

        call.enqueue(this);
    }

    @Override
    public void onResponse(Response<ExtendedWeather> response, Retrofit retrofit) {
        ExtendedWeather extendedWeather = response.body();
        System.out.println("Got Weather!: " + extendedWeather.getCity().getName());
    }

    @Override
    public void onFailure(Throwable t) {
        System.out.println("FAILED! " + t.getMessage());
    }
}
