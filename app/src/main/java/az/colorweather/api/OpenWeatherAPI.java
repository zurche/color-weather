package az.colorweather.api;

import az.colorweather.api.model.current_day.CurrentWeather;
import az.colorweather.api.model.five_day.ExtendedWeather;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by az on 11/10/16.
 */

public interface OpenWeatherAPI {
    @GET("forecast?")
    Call<ExtendedWeather> getFiveDayExtendedWeather(@Query("lat") double lat,
                                                    @Query("lon") double lon,
                                                    @Query("appid") String appId,
                                                    @Query("units") String units,
                                                    @Query("lang") String lang);

    @GET("weather?")
    Call<CurrentWeather> getCurrentWeather(@Query("lat") double lat,
                                           @Query("lon") double lon,
                                           @Query("appid") String appId,
                                           @Query("units") String units,
                                           @Query("lang") String lang);
}
