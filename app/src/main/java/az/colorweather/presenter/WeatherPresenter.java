package az.colorweather.presenter;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import az.colorweather.util.Temperature;
import az.colorweather.WeatherContract;
import az.colorweather.api.OWService;
import az.colorweather.api.listener.OWRequestListener;
import az.colorweather.api.model.OWResponse;
import az.colorweather.api.model.gson.common.Coord;
import az.colorweather.api.model.gson.current_day.CurrentWeather;
import az.colorweather.api.model.gson.five_day.ExtendedWeather;
import az.colorweather.api.model.gson.five_day.WeatherForecastElement;
import az.colorweather.api.utils.OWSupportedLanguages;
import az.colorweather.api.utils.OWSupportedUnits;
import az.colorweather.model.ForecastDay;

/**
 * Created by az on 13/10/16.
 */

public class WeatherPresenter implements WeatherContract.Presenter {

    private static final String TAG = WeatherPresenter.class.getSimpleName();
    private WeatherContract.View view;
    private SimpleDateFormat weatherDateStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private OWService mOWService;

    public WeatherPresenter(WeatherContract.View view) {
        this.view = view;
        mOWService = new OWService("b97081fb50c5b5c19841ec6ae4f5daec");
        mOWService.setLanguage(OWSupportedLanguages.ENGLISH);
        mOWService.setMetricUnits(OWSupportedUnits.METRIC);
    }

    @Override
    public void getFiveDayForecast(final Coord coordinate) {
        mOWService.getFiveDayForecast(coordinate, new OWRequestListener<ExtendedWeather>() {
            @Override
            public void onResponse(OWResponse<ExtendedWeather> response) {
                ExtendedWeather extendedWeather = response.body();

                for (WeatherForecastElement weather : extendedWeather.getList()) {
                    Log.d(TAG, weather.getDtTxt() + " - Got Temp: " + weather.getMain().getTemp() + "°");
                }

                ArrayList<ForecastDay> curatedList = filterTemps(extendedWeather);
                view.updateFiveDayForecast(curatedList);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "Five Day Forecast request failed: " + t.getMessage());
            }
        });
    }

    @Override
    public void getCurrentDayForecast(final Coord coordinate) {
        mOWService.getCurrentDayForecast(coordinate, new OWRequestListener<CurrentWeather>() {
            @Override
            public void onResponse(OWResponse<CurrentWeather> response) {
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

    @Override
    public Temperature getColorForTemp(int temp) {
        Temperature tempColor = Temperature.OK;
        if(mOWService.getSelectedMetricSystem() == OWSupportedUnits.METRIC){
            if(temp > 28){
                tempColor = Temperature.SUPER_HOT;
            } else if (temp > 26 & temp < 28) {
                tempColor = Temperature.MEDIUM_HOT;
            } else if (temp > 23 & temp < 26) {
                tempColor = Temperature.HOT;
            } else if (temp > 21 & temp < 23) {
                tempColor = Temperature.OK;
            } else if (temp > 15 & temp < 21) {
                tempColor = Temperature.OK_CHILL;
            } else if (temp < 15) {
                tempColor = Temperature.COLD;
            }
        } else if (mOWService.getSelectedMetricSystem() == OWSupportedUnits.FAHRENHEIT){
            if(temp > 84){
                tempColor = Temperature.SUPER_HOT;
            } else if (temp > 80 & temp < 84) {
                tempColor = Temperature.MEDIUM_HOT;
            } else if (temp > 74 & temp < 79) {
                tempColor = Temperature.HOT;
            } else if (temp > 70 & temp < 74) {
                tempColor = Temperature.OK;
            } else if (temp > 60 & temp < 70) {
                tempColor = Temperature.OK_CHILL;
            } else if (temp < 60) {
                tempColor = Temperature.COLD;
            }
        }
        return tempColor;
    }

    private ArrayList<ForecastDay> filterTemps(ExtendedWeather extendedWeather) {
        ArrayList<ForecastDay> curatedFiveDayforecast = new ArrayList();

        WeatherForecastElement tmpMax = null;
        WeatherForecastElement tmpMin = null;
        Date parsedDate = null;
        int tmpDay = 0;

        for (WeatherForecastElement element :
                extendedWeather.getList()) {

            parsedDate = new Date();
            try {
                parsedDate = weatherDateStampFormat.parse(element.getDtTxt());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

            //If current day is bigger than tmpDay it means we are checking a new day, add new
            //forecast day with current max and min values here
            if (currentDay > tmpDay) {
                if (tmpDay != 0) {
                    curatedFiveDayforecast.add(new ForecastDay(tmpMax, tmpMin, parsedDate));
                }
                tmpDay = currentDay;
                tmpMax = null;
                tmpMin = null;
            }

            if (null == tmpMax) {
                tmpMax = element;
            }

            if (null == tmpMin) {
                tmpMin = element;
            }

            if (element.getMain().getTempMax() > tmpMax.getMain().getTempMax()) {
                tmpMax = element;
            }

            if (element.getMain().getTempMax() < tmpMin.getMain().getTempMin()) {
                tmpMin = element;
            }

        }

        //This case is for the last day scenario
        curatedFiveDayforecast.add(new ForecastDay(tmpMax, tmpMin, parsedDate));

        return curatedFiveDayforecast;
    }

}
