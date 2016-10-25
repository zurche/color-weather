package az.colorweather.view;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import az.colorweather.R;
import az.colorweather.WeatherContract;
import az.colorweather.model.ForecastDay;
import az.colorweather.presenter.WeatherPresenter;
import az.colorweather.util.Temperature;
import az.openweatherapi.model.gson.common.Coord;
import az.openweatherapi.model.gson.current_day.CurrentWeather;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeatherActivity extends AppCompatActivity implements WeatherContract.View,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = WeatherActivity.class.getSimpleName();
    public static final int FIRST_DAY_INDEX = 0;
    public static final int SECOND_DAY_INDEX = 1;
    public static final int THIRD_DAY_INDEX = 2;
    public static final int FOURTH_DAY_INDEX = 3;
    public static final int FIFTH_DAY_INDEX = 4;

    private WeatherPresenter presenter;

    private Typeface robotoRegularTypeFace;

    private Typeface robotoBlackTypeFace;

    private GoogleApiClient mGoogleApiClient;

    @BindView(R.id.first_forecast)
    TextView first_forecast;
    @BindView(R.id.second_forecast)
    TextView second_forecast;
    @BindView(R.id.third_forecast)
    TextView third_forecast;
    @BindView(R.id.fourth_forecast)
    TextView fourth_forecast;
    @BindView(R.id.fifth_forecast)
    TextView fifth_forecast;

    @BindView(R.id.first_forecast_day)
    TextView first_forecast_day;
    @BindView(R.id.second_forecast_day)
    TextView second_forecast_day;
    @BindView(R.id.third_forecast_day)
    TextView third_forecast_day;
    @BindView(R.id.fourth_forecast_day)
    TextView fourth_forecast_day;
    @BindView(R.id.fifth_forecast_day)
    TextView fifth_forecast_day;

    @BindView(R.id.first_forecast_day_month)
    TextView first_forecast_day_month;
    @BindView(R.id.second_forecast_day_month)
    TextView second_forecast_day_month;
    @BindView(R.id.third_forecast_day_month)
    TextView third_forecast_day_month;
    @BindView(R.id.fourth_forecast_day_month)
    TextView fourth_forecast_day_month;
    @BindView(R.id.fifth_forecast_day_month)
    TextView fifth_forecast_day_month;

    @BindView(R.id.current_weather)
    TextView current_weather;

    @BindView(R.id.today_button)
    TextView today_button;
    @BindView(R.id.five_day_button)
    TextView five_day_button;

    @BindView(R.id.today_underline)
    TextView today_underline;
    @BindView(R.id.five_day_underline)
    TextView five_day_underline;

    @BindView(R.id.loading_weather_progress)
    ProgressBar loading_weather_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        presenter = new WeatherPresenter(this, getResources().getConfiguration().locale);
        robotoRegularTypeFace = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        robotoBlackTypeFace = Typeface.createFromAsset(getAssets(), "Roboto-Black.ttf");
        setupUiTypeFace();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupFiveDaySelectedUi();

        loading_weather_progress.setVisibility(View.VISIBLE);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @OnClick(R.id.today_button)
    public void todayButtonClick(View view) {
        setupTodaySelectedUi();

        presenter.getCurrentDayExtendedForecast();
    }

    private void setupTodaySelectedUi() {
        five_day_underline.setVisibility(View.INVISIBLE);
        today_underline.setVisibility(View.VISIBLE);

        today_button.setTypeface(robotoBlackTypeFace);
        five_day_button.setTypeface(robotoRegularTypeFace);

        first_forecast_day_month.setVisibility(View.INVISIBLE);
        second_forecast_day_month.setVisibility(View.INVISIBLE);
        third_forecast_day_month.setVisibility(View.INVISIBLE);
        fourth_forecast_day_month.setVisibility(View.INVISIBLE);
        fifth_forecast_day_month.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.five_day_button)
    public void fiveDayButtonClick(View view) {
        setupFiveDaySelectedUi();

        Coord coordinate = new Coord();
        coordinate.setLat(-31.3993437);
        coordinate.setLon(-64.3344292);

        presenter.getFiveDayForecast(coordinate);
    }

    private void setupFiveDaySelectedUi() {
        five_day_underline.setVisibility(View.VISIBLE);
        today_underline.setVisibility(View.INVISIBLE);

        today_button.setTypeface(robotoRegularTypeFace);
        five_day_button.setTypeface(robotoBlackTypeFace);

        first_forecast_day_month.setVisibility(View.VISIBLE);
        second_forecast_day_month.setVisibility(View.VISIBLE);
        third_forecast_day_month.setVisibility(View.VISIBLE);
        fourth_forecast_day_month.setVisibility(View.VISIBLE);
        fifth_forecast_day_month.setVisibility(View.VISIBLE);

        first_forecast_day_month.setText("");
        second_forecast_day_month.setText("");
        third_forecast_day_month.setText("");
        fourth_forecast_day_month.setText("");
        fifth_forecast_day_month.setText("");
    }

    private void setupUiTypeFace() {
        first_forecast_day.setTypeface(robotoBlackTypeFace);
        second_forecast_day.setTypeface(robotoBlackTypeFace);
        third_forecast_day.setTypeface(robotoBlackTypeFace);
        fourth_forecast_day.setTypeface(robotoBlackTypeFace);
        fifth_forecast_day.setTypeface(robotoBlackTypeFace);

        current_weather.setTypeface(robotoBlackTypeFace);
        first_forecast.setTypeface(robotoBlackTypeFace);
        second_forecast.setTypeface(robotoBlackTypeFace);
        third_forecast.setTypeface(robotoBlackTypeFace);
        fourth_forecast.setTypeface(robotoBlackTypeFace);
        fifth_forecast.setTypeface(robotoBlackTypeFace);
    }

    @Override
    public void updateFiveDayForecast(ArrayList<ForecastDay> forecastDays) {
        enableModeButtons();

        updateForecastInUi(forecastDays);
    }

    private void updateForecastInUi(ArrayList<ForecastDay> forecastDays) {
        String tempWithDegrees = String.format(getString(R.string.degrees_placeholder), forecastDays.get(FIRST_DAY_INDEX).getTemperature());
        first_forecast_day_month.setText(forecastDays.get(FIRST_DAY_INDEX).getDayMonth());

        first_forecast.setText(tempWithDegrees);
        first_forecast.setTextColor(getColor(presenter.getColorForTemp(forecastDays.get(FIRST_DAY_INDEX).getTemperature())));

        tempWithDegrees = String.format(getString(R.string.degrees_placeholder), forecastDays.get(SECOND_DAY_INDEX).getTemperature());
        second_forecast.setText(tempWithDegrees);
        second_forecast.setTextColor(getColor(presenter.getColorForTemp(forecastDays.get(SECOND_DAY_INDEX).getTemperature())));
        second_forecast_day.setText(forecastDays.get(SECOND_DAY_INDEX).getDay());
        second_forecast_day_month.setText(forecastDays.get(SECOND_DAY_INDEX).getDayMonth());

        tempWithDegrees = String.format(getString(R.string.degrees_placeholder), forecastDays.get(THIRD_DAY_INDEX).getTemperature());
        third_forecast.setText(tempWithDegrees);
        third_forecast.setTextColor(getColor(presenter.getColorForTemp(forecastDays.get(THIRD_DAY_INDEX).getTemperature())));
        third_forecast_day.setText(forecastDays.get(THIRD_DAY_INDEX).getDay());
        third_forecast_day_month.setText(forecastDays.get(THIRD_DAY_INDEX).getDayMonth());

        tempWithDegrees = String.format(getString(R.string.degrees_placeholder), forecastDays.get(FOURTH_DAY_INDEX).getTemperature());
        fourth_forecast.setText(tempWithDegrees);
        fourth_forecast.setTextColor(getColor(presenter.getColorForTemp(forecastDays.get(FOURTH_DAY_INDEX).getTemperature())));
        fourth_forecast_day.setText(forecastDays.get(FOURTH_DAY_INDEX).getDay());
        fourth_forecast_day_month.setText(forecastDays.get(FOURTH_DAY_INDEX).getDayMonth());

        tempWithDegrees = String.format(getString(R.string.degrees_placeholder), forecastDays.get(FIFTH_DAY_INDEX).getTemperature());
        fifth_forecast.setText(tempWithDegrees);
        fifth_forecast.setTextColor(getColor(presenter.getColorForTemp(forecastDays.get(FIFTH_DAY_INDEX).getTemperature())));
        fifth_forecast_day.setText(forecastDays.get(FIFTH_DAY_INDEX).getDay());
        fifth_forecast_day_month.setText(forecastDays.get(FIFTH_DAY_INDEX).getDayMonth());
    }

    private void enableModeButtons() {
        five_day_button.setEnabled(true);
        today_button.setEnabled(true);
    }

    @Override
    public void updateCurrentDayExtendedForecast(ArrayList<ForecastDay> currentWeather) {
        String degrees = String.format(getString(R.string.degrees_placeholder), currentWeather.get(FIRST_DAY_INDEX).getTemperature());
        current_weather.setText(degrees);
        current_weather.setTextColor(getColor(presenter.getColorForTemp(currentWeather.get(FIRST_DAY_INDEX).getTemperature())));

        first_forecast.setText(degrees);
        first_forecast.setTextColor(getColor(presenter.getColorForTemp(currentWeather.get(FIRST_DAY_INDEX).getTemperature())));

        degrees = String.format(getString(R.string.degrees_placeholder), currentWeather.get(SECOND_DAY_INDEX).getTemperature());
        second_forecast.setText(degrees);
        second_forecast.setTextColor(getColor(presenter.getColorForTemp(currentWeather.get(SECOND_DAY_INDEX).getTemperature())));
        second_forecast_day.setText(currentWeather.get(SECOND_DAY_INDEX).getHourOfForecast());

        degrees = String.format(getString(R.string.degrees_placeholder), currentWeather.get(THIRD_DAY_INDEX).getTemperature());
        third_forecast.setText(degrees);
        third_forecast.setTextColor(getColor(presenter.getColorForTemp(currentWeather.get(THIRD_DAY_INDEX).getTemperature())));
        third_forecast_day.setText(currentWeather.get(THIRD_DAY_INDEX).getHourOfForecast());

        degrees = String.format(getString(R.string.degrees_placeholder), currentWeather.get(FOURTH_DAY_INDEX).getTemperature());
        fourth_forecast.setText(degrees);
        fourth_forecast.setTextColor(getColor(presenter.getColorForTemp(currentWeather.get(FOURTH_DAY_INDEX).getTemperature())));
        fourth_forecast_day.setText(currentWeather.get(FOURTH_DAY_INDEX).getHourOfForecast());

        degrees = String.format(getString(R.string.degrees_placeholder), currentWeather.get(FIFTH_DAY_INDEX).getTemperature());
        fifth_forecast.setText(degrees);
        fifth_forecast.setTextColor(getColor(presenter.getColorForTemp(currentWeather.get(FIFTH_DAY_INDEX).getTemperature())));
        fifth_forecast_day.setText(currentWeather.get(FIFTH_DAY_INDEX).getHourOfForecast());
    }

    @Override
    public void updateTodayForecast(CurrentWeather currentWeather) {
        loading_weather_progress.setVisibility(View.INVISIBLE);

        int roundedTemp = (int) Math.round(currentWeather.getMain().getTemp());
        String tempWithDegrees = String.format(getString(R.string.degrees_placeholder), roundedTemp);
        current_weather.setText(tempWithDegrees);
        current_weather.setTextColor(getColor(presenter.getColorForTemp(roundedTemp)));
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        Coord coordinates = new Coord();

        if (mLastLocation != null) {
            coordinates.setLat(mLastLocation.getLatitude());
            coordinates.setLon(mLastLocation.getLongitude());

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putFloat(getString(R.string.saved_latitude), (float) mLastLocation.getLatitude());
            editor.putFloat(getString(R.string.saved_longitude), (float) mLastLocation.getLongitude());
            editor.commit();
        } else {
            float defaultLatitude = getResources().getInteger(R.integer.latitude_default);
            float defaultLongitude = getResources().getInteger(R.integer.longitude_default);

            float savedLatitude = sharedPref.getFloat(getString(R.string.saved_latitude), defaultLatitude);
            float savedLongitude = sharedPref.getFloat(getString(R.string.saved_longitude), defaultLongitude);

            coordinates.setLat((double) savedLatitude);
            coordinates.setLon((double) savedLongitude);
        }

        presenter.getFiveDayForecast(coordinates);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Could not retrieve location\nCheck GPS", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Could not retrieve location\nCheck GPS", Toast.LENGTH_LONG).show();
    }
}
