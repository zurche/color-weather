package az.colorweather.view;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import az.colorweather.R;
import az.colorweather.WeatherContract;
import az.colorweather.api.model.gson.common.Coord;
import az.colorweather.api.model.gson.current_day.CurrentWeather;
import az.colorweather.api.model.gson.five_day.WeatherForecastElement;
import az.colorweather.model.ForecastDay;
import az.colorweather.presenter.WeatherPresenter;
import az.colorweather.util.Temperature;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeatherActivity extends AppCompatActivity implements WeatherContract.View {

    private static final String TAG = WeatherActivity.class.getSimpleName();
    public static final int SECOND_DAY_INDEX = 1;
    public static final int THIRD_DAY_INDEX = 2;
    public static final int FOURTH_DAY_INDEX = 3;
    public static final int FIFTH_DAY_INDEX = 4;

    private WeatherPresenter presenter;

    private Typeface robotoRegularTypeFace;

    private Typeface robotoBlackTypeFace;

    private SimpleDateFormat dayFormat;

    private SimpleDateFormat weatherDateStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @BindView(R.id.first_forecast)  TextView first_forecast;
    @BindView(R.id.second_forecast) TextView second_forecast;
    @BindView(R.id.third_forecast)  TextView third_forecast;
    @BindView(R.id.fourth_forecast) TextView fourth_forecast;
    @BindView(R.id.fifth_forecast)  TextView fifth_forecast;

    @BindView(R.id.first_forecast_day)  TextView first_forecast_day;
    @BindView(R.id.second_forecast_day) TextView second_forecast_day;
    @BindView(R.id.third_forecast_day)  TextView third_forecast_day;
    @BindView(R.id.fourth_forecast_day) TextView fourth_forecast_day;
    @BindView(R.id.fifth_forecast_day)  TextView fifth_forecast_day;

    @BindView(R.id.current_weather) TextView current_weather;

    @BindView(R.id.today_button)    TextView today_button;
    @BindView(R.id.five_day_button) TextView five_day_button;

    @BindView(R.id.degrees_icon) TextView degrees_icon;

    @BindView(R.id.today_underline)    TextView today_underline;
    @BindView(R.id.five_day_underline) TextView five_day_underline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        ButterKnife.bind(this);

        presenter = new WeatherPresenter(this);

        robotoRegularTypeFace = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        robotoBlackTypeFace = Typeface.createFromAsset(getAssets(), "Roboto-Black.ttf");

        setupUiTypeFace();

        dayFormat = new SimpleDateFormat("EEE", getResources().getConfiguration().locale);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO: REMOVE FOLLOWING AND CHECK LATEST RETRIEVED VALUE AND SETUP UI ACCORDINGLY HERE

        setupFiveDaySelectedUi();

        Coord coordinate = new Coord();
        coordinate.setLat(32.8998091);
        coordinate.setLon(-97.0425239);

        presenter.getFiveDayForecast(coordinate);
        presenter.getCurrentDayForecast(coordinate);
    }

    @OnClick(R.id.today_button)
    public void todayButtonClick(View view) {
        setupTodaySelectedUi();

        //TODO: CALL TODAY WEATHER FULL FORECAST HERE
    }

    private void setupTodaySelectedUi() {
        five_day_underline.setVisibility(View.INVISIBLE);
        today_underline.setVisibility(View.VISIBLE);

        today_button.setTypeface(robotoBlackTypeFace);
        five_day_button.setTypeface(robotoRegularTypeFace);
    }

    @OnClick(R.id.five_day_button)
    public void fiveDayButtonClick(View view) {
        setupFiveDaySelectedUi();

        Coord coordinate = new Coord();
        coordinate.setLat(32.8998091);
        coordinate.setLon(-97.0425239);

        presenter.getFiveDayForecast(coordinate);
        presenter.getCurrentDayForecast(coordinate);
    }

    private void setupFiveDaySelectedUi() {
        five_day_underline.setVisibility(View.VISIBLE);
        today_underline.setVisibility(View.INVISIBLE);

        today_button.setTypeface(robotoRegularTypeFace);
        five_day_button.setTypeface(robotoBlackTypeFace);
    }

    private void setupUiTypeFace() {
        first_forecast_day.setTypeface(robotoBlackTypeFace);
        second_forecast_day.setTypeface(robotoRegularTypeFace);
        third_forecast_day.setTypeface(robotoRegularTypeFace);
        fourth_forecast_day.setTypeface(robotoRegularTypeFace);
        fifth_forecast_day.setTypeface(robotoRegularTypeFace);

        current_weather.setTypeface(robotoBlackTypeFace);
        first_forecast.setTypeface(robotoBlackTypeFace);
        second_forecast.setTypeface(robotoBlackTypeFace);
        third_forecast.setTypeface(robotoBlackTypeFace);
        fourth_forecast.setTypeface(robotoBlackTypeFace);
        fifth_forecast.setTypeface(robotoBlackTypeFace);

        degrees_icon.setTypeface(robotoBlackTypeFace);
    }

    //TODO: LIST OBTAINED MUST HAVE ONLY DATA USED IN THE SCREEN TO AVOID OVER ACCESSING FUNCTIONS
    @Override
    public void updateFiveDayForecast(ArrayList<WeatherForecastElement> forecastDays) {
        int roundedTemp = (int) Math.round(forecastDays.get(SECOND_DAY_INDEX).getMain().getTemp());
        String maxDegrees = String.format(getString(R.string.degrees_placeholder), roundedTemp);
        second_forecast.setText(maxDegrees);
        second_forecast.setTextColor(getColor(presenter.getColorForTemp(roundedTemp)));
        try {
            second_forecast_day.setText(dayFormat.format(weatherDateStampFormat.parse(forecastDays.get(SECOND_DAY_INDEX).getDtTxt())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        roundedTemp = (int) Math.round(forecastDays.get(THIRD_DAY_INDEX).getMain().getTemp());
        maxDegrees = String.format(getString(R.string.degrees_placeholder), roundedTemp);
        third_forecast.setText(maxDegrees);
        third_forecast.setTextColor(getColor(presenter.getColorForTemp(roundedTemp)));
        try {
            third_forecast_day.setText(dayFormat.format(weatherDateStampFormat.parse(forecastDays.get(THIRD_DAY_INDEX).getDtTxt())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        roundedTemp = (int) Math.round(forecastDays.get(FOURTH_DAY_INDEX).getMain().getTemp());
        maxDegrees = String.format(getString(R.string.degrees_placeholder), roundedTemp);
        fourth_forecast.setText(maxDegrees);
        fourth_forecast.setTextColor(getColor(presenter.getColorForTemp(roundedTemp)));
        try {
            fourth_forecast_day.setText(dayFormat.format(weatherDateStampFormat.parse(forecastDays.get(FOURTH_DAY_INDEX).getDtTxt())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        roundedTemp = (int) Math.round(forecastDays.get(FIFTH_DAY_INDEX).getMain().getTemp());
        maxDegrees = String.format(getString(R.string.degrees_placeholder), roundedTemp);
        fifth_forecast.setText(maxDegrees);
        fifth_forecast.setTextColor(getColor(presenter.getColorForTemp(roundedTemp)));
        try {
            fifth_forecast_day.setText(dayFormat.format(weatherDateStampFormat.parse(forecastDays.get(FIFTH_DAY_INDEX).getDtTxt())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCurrentWeather(CurrentWeather currentWeather) {
        int roundedTemp = (int) Math.round(currentWeather.getMain().getTemp());
        String degrees = "" + roundedTemp;
        current_weather.setText(degrees);
        current_weather.setTextColor(getColor(presenter.getColorForTemp(roundedTemp)));
        degrees_icon.setTextColor(getColor(presenter.getColorForTemp(roundedTemp)));

        first_forecast.setText(degrees);
        first_forecast.setTextColor(getColor(presenter.getColorForTemp(roundedTemp)));
        first_forecast_day.setText(getString(R.string.now));
    }

    private int getColor(Temperature colorTemp) {
        int color = 0;

        switch (colorTemp) {
            case SUPER_HOT:
                color = ContextCompat.getColor(this, R.color.super_hot);
                break;
            case MEDIUM_HOT:
                color = ContextCompat.getColor(this, R.color.medium_hot);
                break;
            case HOT:
                color = ContextCompat.getColor(this, R.color.hot);
                break;
            case OK:
                color = ContextCompat.getColor(this, R.color.ok);
                break;
            case OK_CHILL:
                color = ContextCompat.getColor(this, R.color.ok_chill);
                break;
            case COLD:
                color = ContextCompat.getColor(this, R.color.cold);
                break;
        }

        return color;
    }
}
