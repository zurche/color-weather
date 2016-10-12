package az.colorweather;

import az.colorweather.model.ExtendedWeather;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by az on 11/10/16.
 */

public interface OpenWeatherAPI {
    @GET("forecast?")
    Call<ExtendedWeather> loadExtendedWeather(@Query("lat") int lat,
                                              @Query("lon") int lon,
                                              @Query("appid") String appId);
}
