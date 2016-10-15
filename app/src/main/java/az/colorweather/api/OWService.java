package az.colorweather.api;

import az.colorweather.api.model.gson.common.Coord;
import az.colorweather.api.model.gson.current_day.CurrentWeather;
import az.colorweather.api.model.gson.five_day.ExtendedWeather;
import az.colorweather.api.utils.OWSupportedLanguages;
import az.colorweather.api.utils.OWSupportedUnits;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by az on 15/10/16.
 */

public class OWService {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    private final OpenWeatherAPI mOpenWeatherAPI;

    private String mToken;

    private OWSupportedUnits mSelectedUnits = OWSupportedUnits.FAHRENHEIT;

    private OWSupportedLanguages mSelectedLanguage = OWSupportedLanguages.ENGLISH;

    /**
     * Main Service constructor.
     * @param token the Open Weather Token to be used for the API calls.
     */
    public OWService(final String token) {
        mToken = token;

        Retrofit mRetrofitOWInstance = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mOpenWeatherAPI = mRetrofitOWInstance.create(OpenWeatherAPI.class);
    }

    /**
     * This configures the Metric Units in which the results of the requests need to be in.
     * @param units a valid OWSupportedUnits object.
     */
    public void setMetricUnits(OWSupportedUnits units) {
        mSelectedUnits = units;
    }

    public void setLanguage(OWSupportedLanguages language) {
        mSelectedLanguage = language;
    }

    /**
     * Obtain five days forecast for any given Latitude/Longitude pair.
     * @param coordinate the latitude/longitude pair of the location you need the weather for.
     * @param callback the callback of the request result.
     */
    public void getFiveDayForecast(final Coord coordinate,
                                   final Callback<ExtendedWeather> callback){
        Call<ExtendedWeather> fiveDayForecastCall = mOpenWeatherAPI.getFiveDayExtendedWeather(
                coordinate.getLat(),
                coordinate.getLon(),
                mToken,
                mSelectedUnits.getUnit(),
                mSelectedLanguage.getLanguageLocale());
        fiveDayForecastCall.enqueue(callback);
    }

    /**
     * Obtain current day forecast for any given Latitude/Longitude pair.
     * @param coordinate the latitude/longitude pair of the location you need the weather for.
     * @param callback the callback of the request result.
     */
    public void getCurrentDayForecast(final Coord coordinate,
                                      final Callback<CurrentWeather> callback) {
        Call<CurrentWeather> currentDayForecastCall = mOpenWeatherAPI.getCurrentWeather(
                coordinate.getLat(),
                coordinate.getLon(),
                mToken,
                mSelectedUnits.getUnit(),
                mSelectedLanguage.getLanguageLocale());
        currentDayForecastCall.enqueue(callback);
    }

}