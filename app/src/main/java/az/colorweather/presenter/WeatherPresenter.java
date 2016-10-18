package az.colorweather.presenter;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import az.colorweather.WeatherContract;
import az.colorweather.api.OWService;
import az.colorweather.api.listener.OWRequestListener;
import az.colorweather.api.model.OWResponse;
import az.colorweather.api.model.gson.common.Coord;
import az.colorweather.api.model.gson.five_day.ExtendedWeather;
import az.colorweather.api.model.gson.five_day.WeatherForecastElement;
import az.colorweather.api.utils.OWSupportedLanguages;
import az.colorweather.api.utils.OWSupportedUnits;
import az.colorweather.model.ForecastDay;
import az.colorweather.util.Temperature;

/**
 * Created by az on 13/10/16.
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

    public WeatherPresenter(WeatherContract.View view, Locale locale) {
        this.view = view;
        mOWService = new OWService("b97081fb50c5b5c19841ec6ae4f5daec");
        mOWService.setLanguage(OWSupportedLanguages.SPANISH);
        mOWService.setMetricUnits(OWSupportedUnits.FAHRENHEIT);
        mLocale = locale;
        dayFormat = new SimpleDateFormat("EEE", mLocale);
    }

    @Override
    public void getFiveDayForecast(final Coord coordinate) {

        if(canUpdateForecast()) {
            mOWService.getFiveDayForecast(coordinate, new OWRequestListener<ExtendedWeather>() {
                @Override
                public void onResponse(OWResponse<ExtendedWeather> response) {
                    ExtendedWeather extendedWeather = response.body();
                    mFiveDayForecast = filterTemps(extendedWeather);
                    mDayForecast = filterUpcomingFiveForecasts(extendedWeather);
                    view.updateFiveDayForecast(mFiveDayForecast);
                    mLastRetrievedDateStamp = new Date(System.currentTimeMillis());
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, "Five Day Forecast request failed: " + t.getMessage());
                }
            });
        } else {
            view.updateFiveDayForecast(mFiveDayForecast);
        }
    }

    private boolean canUpdateForecast() {
        if(mLastRetrievedDateStamp != null && mFiveDayForecast != null) {
            if(System.currentTimeMillis() - mLastRetrievedDateStamp.getTime() > TEN_MINUTES) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void getCurrentDayForecast(final Coord coordinate) {
        view.updateCurrentDayWeather(mDayForecast);
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
        ArrayList<ForecastDay> curatedFiveDayForecast = new ArrayList();

        int tmpDay = 0;

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

            if (currentDay != tmpDay) {
                int roundedTemp = (int) Math.round(element.getMain().getTemp());
                String dayName = dayFormat.format(parsedDate);
                curatedFiveDayForecast.add(
                        new ForecastDay(
                                dayName,
                                "" + currentDay + "/" + currentMonth,
                                currentHour,
                                roundedTemp));
                tmpDay = currentDay;
            }
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

            if(currentDayCount == 5) {
                break;
            }
        }

        return upcomingFiveForecasts;
    }

}
