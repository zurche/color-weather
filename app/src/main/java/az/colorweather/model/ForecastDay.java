package az.colorweather.model;

/**
 * This class is used to simplify the data obtained from the forecast into something
 * with less useless information for the UI.
 */
public class ForecastDay {

    private String day;
    private String dayString;
    private String hourOfForecast;
    private int temperature;

    public ForecastDay(String day, String dayString, int hourOfForecast, int temperature) {
        this.day = day;
        this.dayString = dayString;
        this.temperature = temperature;
        this.hourOfForecast = hourOfForecast + ":00";
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

    public String getHourOfForecast() {
        return hourOfForecast;
    }
}
