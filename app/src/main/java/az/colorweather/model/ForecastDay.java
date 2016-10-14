package az.colorweather.model;

/**
 * Created by az on 14/10/16.
 */

public class ForecastDay {
    private String maxTemp;
    private String minTemp;
    private String day;

    public ForecastDay(String maxTemp, String minTemp, String day) {
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.day = day;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
