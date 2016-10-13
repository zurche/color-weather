package az.colorweather.presenter;

import az.colorweather.model.ExtendedWeather;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by az on 11/10/16.
 */

public interface OpenWeatherAPI {
    @GET("forecast?")
    Call<ExtendedWeather> loadExtendedWeather(@Query("lat") double lat,
                                              @Query("lon") double lon,
                                              @Query("appid") String appId,
                                              @Query("units") String units,
                                              @Query("lang") String lang);
}
