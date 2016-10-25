package az.colorweather.presenter;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import az.colorweather.WeatherContract;
import az.colorweather.model.ForecastDay;
import az.colorweather.util.Temperature;
import az.openweatherapi.OWService;
import az.openweatherapi.listener.OWRequestListener;
import az.openweatherapi.model.OWResponse;
import az.openweatherapi.model.gson.common.Coord;
import az.openweatherapi.model.gson.current_day.CurrentWeather;
import az.openweatherapi.model.gson.five_day.ExtendedWeather;
import az.openweatherapi.model.gson.five_day.WeatherForecastElement;
import az.openweatherapi.utils.OWSupportedUnits;

/**
 * Presenter of the ColorWeather main screen.
 */
public class WeatherPresenter implements WeatherContract.Presenter {

    private static final String TAG = WeatherPresenter.class.getSimpleName();
    public static final int TEN_MINUTES = 600000;
    private WeatherContract.View view;
    private SimpleDateFormat weatherDateStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private OWService mOWService;
    private Locale mLocale;
    private SimpleDateFormat dayFormat;
    private ArrayList<ForecastDay> mFiveDayForecast;
    private ArrayList<ForecastDay> mDayForecast;
    private Date mLastRetrievedDateStamp;
    private CurrentWeather mCurrentWeather;

    public WeatherPresenter(WeatherContract.View view, Locale locale) {
        this.view = view;
        mOWService = new OWService("b97081fb50c5b5c19841ec6ae4f5daec");
        mOWService.setLanguage(locale);
        mOWService.setMetricUnits(OWSupportedUnits.METRIC);
        mLocale = locale;
        dayFormat = new SimpleDateFormat("EEE", mLocale);
    }

    @Override
    public void getFiveDayForecast(final Coord coordinate) {

        if (canUpdateForecast()) {
            mOWService.getFiveDayForecast(coordinate, new OWRequestListener<ExtendedWeather>() {
                @Override
                public void onResponse(OWResponse<ExtendedWeather> response) {
                    ExtendedWeather extendedWeather = response.body();
                    mFiveDayForecast = filterTemps(extendedWeather);
                    mDayForecast = filterUpcomingFiveForecasts(extendedWeather);
                    view.updateFiveDayForecast(mFiveDayForecast);
                    mLastRetrievedDateStamp = new Date(System.currentTimeMillis());

                    getCurrentForecast(coordinate);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, "Five Day Forecast request failed: " + t.getMessage());
                }
            });
        } else {
            view.updateFiveDayForecast(mFiveDayForecast);
            view.updateTodayForecast(mCurrentWeather);
        }
    }

    private boolean canUpdateForecast() {
        if (mLastRetrievedDateStamp != null && mFiveDayForecast != null) {
            if (System.currentTimeMillis() - mLastRetrievedDateStamp.getTime() > TEN_MINUTES) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * Retrieves extended forecast for today.
     */
    @Override
    public void getCurrentDayExtendedForecast() {
        view.updateCurrentDayExtendedForecast(mDayForecast);
    }

    /**
     * Retrieves current moment forecast.
     */
    private void getCurrentForecast(final Coord coordinate) {
        mOWService.getCurrentDayForecast(coordinate, new OWRequestListener<CurrentWeather>() {
            @Override
            public void onResponse(OWResponse<CurrentWeather> response) {
                mCurrentWeather = response.body();
                view.updateTodayForecast(mCurrentWeather);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "Current Day Forecast request failed: " + t.getMessage());
            }
        });
    }

    @Override
    public Temperature getColorForTemp(int temp) {
        Temperature tempColor = Temperature.OK;
        if (mOWService.getSelectedMetricSystem() == OWSupportedUnits.METRIC) {
            if (temp > 28) {
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
        } else if (mOWService.getSelectedMetricSystem() == OWSupportedUnits.FAHRENHEIT) {
            if (temp > 84) {
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
        ArrayList<ForecastDay> curatedFiveDayForecast = new ArrayList<>();
        Map<Integer, ForecastDay> sortedTemperaturesMap = new TreeMap<>();

        for (WeatherForecastElement element :
                extendedWeather.getList()) {

            Date parsedDate = new Date();
            try {
                parsedDate = weatherDateStampFormat.parse(element.getDtTxt());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

            int roundedTemp = (int) Math.round(element.getMain().getTemp());
            String dayName = dayFormat.format(parsedDate);

            ForecastDay tempForecast = new ForecastDay(dayName,
                    "" + currentDay + "/" + currentMonth,
                    currentHour,
                    roundedTemp);

            //If it is the first temp of the day, then add it
            if(sortedTemperaturesMap.get(currentDay) == null) {
                sortedTemperaturesMap.put(currentDay, tempForecast);
                //Otherwise check if the current checked temp for this day is greater than
                //the already stored one, if thats true, then replace the current day greatest temp.
            } else if (roundedTemp > sortedTemperaturesMap.get(currentDay).getTemperature()) {
                sortedTemperaturesMap.put(currentDay, tempForecast);
            }
        }

        for (Map.Entry<Integer, ForecastDay> entry : sortedTemperaturesMap.entrySet()) {
            curatedFiveDayForecast.add(entry.getValue());
        }

        return curatedFiveDayForecast;
    }

    private ArrayList<ForecastDay> filterUpcomingFiveForecasts(ExtendedWeather extendedWeather) {
        ArrayList<ForecastDay> upcomingFiveForecasts = new ArrayList();

        int currentDayCount = 0;

        for (WeatherForecastElement element : extendedWeather.getList()) {

            Date parsedDate = new Date();
            try {
                parsedDate = weatherDateStampFormat.parse(element.getDtTxt());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

            int roundedTemp = (int) Math.round(element.getMain().getTemp());
            String dayName = dayFormat.format(parsedDate);
            upcomingFiveForecasts.add(
                    new ForecastDay(dayName,
                            "" + currentDay + "/" + currentMonth,
                            currentHour,
                            roundedTemp));

            currentDayCount++;

            if (currentDayCount == 5) {
                break;
            }
        }

        return upcomingFiveForecasts;
    }

}
