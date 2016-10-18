package az.colorweather.model;

/**
 * Created by az on 14/10/16.
 */

public class ForecastDay {

    String day;
    String dayString;
    int temperature;

    public ForecastDay(String day, String dayString, int temperature) {
        this.day = day;
        this.dayString = dayString;
        this.temperature = temperature;
    }

    public String getDay() {
        return day;
    }

    public String getDayMonth() {
        return dayString;
    }

    public int getTemperature() {
        return temperature;
    }
}
