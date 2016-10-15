package az.colorweather.model;

import java.util.Date;

import az.colorweather.api.model.gson.five_day.WeatherForecastElement;

/**
 * Created by az on 14/10/16.
 */

public class ForecastDay {
    private WeatherForecastElement maxTempForecast;
    private WeatherForecastElement minTempForecast;
    private Date day;

    public ForecastDay(WeatherForecastElement maxTempForecast, WeatherForecastElement minTempForecast, Date day) {
        this.maxTempForecast = maxTempForecast;
        this.minTempForecast = minTempForecast;
        this.day = day;
    }

    public WeatherForecastElement getMaxTempForecast() {
        return maxTempForecast;
    }

    public void setMaxTempForecast(WeatherForecastElement maxTempForecast) {
        this.maxTempForecast = maxTempForecast;
    }

    public WeatherForecastElement getMinTempForecast() {
        return minTempForecast;
    }

    public void setMinTempForecast(WeatherForecastElement minTempForecast) {
        this.minTempForecast = minTempForecast;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }
}
