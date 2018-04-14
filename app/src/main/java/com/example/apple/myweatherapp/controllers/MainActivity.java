package com.example.apple.myweatherapp.controllers;

import android.Manifest;
import android.app.Activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.myweatherapp.geolocation.MyLocationManager;
import com.example.apple.myweatherapp.networking.NetworkingManager;
import com.example.apple.weatherapplication.BuildConfig;
import com.example.apple.weatherapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements NetworkingManager.WeatherCallback, NetworkingManager.WeatherCallbackForFiveDays {

  public static final String TAG = MainActivity.class.getSimpleName();
  private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

  private TextView mLatitudeText;
  private TextView mLongitudeText;
  private TextView mAddressText;
  private TextView temperatureTextView;
  private EditText mCityEditText;
  private ImageView mWeatherIconImageView;
  private LocationManager mLocationManager;
  private LocationListener mLocationListener;

  private MyLocationManager myLocationManager;
  private NetworkingManager mNetworkingManager;

  private  String cityForSearch;

  private String temperature;
  private String[] weatherIcinForFiveDay;
  private String[] temperatureForFiveDay;
  private String[] dataForFiveDay;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initViews();

    myLocationManager = new MyLocationManager(getApplicationContext());
    mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    mNetworkingManager = new NetworkingManager(getApplicationContext());
    mNetworkingManager.setWeatherCallback(this);
    mNetworkingManager.setWeatherCallbackForFiveDays(this);

    mLocationListener = new LocationListener() {
      @Override
      public void onLocationChanged(final Location location) {
        if (location != null) {
          Address address = myLocationManager.getCurrentLocation(location);
          if (address != null) {
            String country = address.getCountryName();
            String city = address.getLocality();
            mAddressText.setText(getResources().getString(R.string.location_information, country, city));

            /*mLatitudeText.setText(getResources().getString(R.string.latitude_label, String.valueOf(location.getLatitude())));
            mLongitudeText.setText(getResources().getString(R.string.longitude_label, String.valueOf(location.getLongitude())));
*/
            mNetworkingManager.getCurrentTemperatureForCity(myLocationManager.getCurrentLocation(location).getLocality());
          } else {
            showError();
          }
        } else {
          mLatitudeText.setText(R.string.latitude_null);
          mLongitudeText.setText(R.string.longitude_null);
        }
      }

      @Override
      public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO something
      }

      @Override
      public void onProviderEnabled(String provider) {
        // TODO something
      }

      @Override
      public void onProviderDisabled(String provider) {
        // TODO something
      }
    };
  }

  private void showError() {
    Toast.makeText(MainActivity.this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
  }

  @Override
  public void onStart() {
    super.onStart();
    if (!checkPermissions()) {
      requestPermissions();
    } else {
      getLastLocation();
    }
  }

  @SuppressWarnings("MissingPermission")
  private void getLastLocation() {
    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
            10, mLocationListener);
  }

  /**
   * Shows a {@link Snackbar}.
   *
   * @param mainTextStringId The id for the string resource for the Snackbar text.
   * @param actionStringId   The text of the action item.
   * @param listener         The listener associated with the Snackbar action.
   */

  private void showSnackbar(final int mainTextStringId, final int actionStringId,
                            View.OnClickListener listener) {
    Snackbar.make(findViewById(android.R.id.content),
            getString(mainTextStringId),
            Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(actionStringId), listener).show();
  }

  /**
   * Return the current state of the permissions needed.
   */
  private boolean checkPermissions() {
    int permissionStateCoarse = ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION);
    int permissionStateFine = ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION);
    return permissionStateCoarse == PackageManager.PERMISSION_GRANTED && permissionStateFine == PackageManager.PERMISSION_GRANTED;
  }

  private void startLocationPermissionRequest() {
    ActivityCompat.requestPermissions(MainActivity.this,
            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
            REQUEST_PERMISSIONS_REQUEST_CODE);
  }

  private void requestPermissions() {
    boolean shouldProvideRationaleCoarse =
            ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);

    boolean shouldProvideRationaleFine =
            ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);

    if (shouldProvideRationaleCoarse || shouldProvideRationaleFine) {
      Log.i(TAG, "Displaying permission rationale to provide additional context.");

      showSnackbar(R.string.permission_rationale, android.R.string.ok,
              new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  // Request permission
                  startLocationPermissionRequest();
                }
              });

    } else {
      Log.i(TAG, "Requesting permission");
      startLocationPermissionRequest();
    }
  }

  /**
   * Callback received when a permissions request has been completed.
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    Log.i(TAG, "onRequestPermissionResult");
    if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
      if (grantResults.length <= 0) {
        Log.i(TAG, "User interaction was cancelled.");
      } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // Permission granted.
        getLastLocation();
      } else {
        showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                    // Build intent that displays the App settings screen.
                    Intent intent = new Intent();
                    intent.setAction(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package",
                            BuildConfig.APPLICATION_ID, null);
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                  }
                });
      }
    }
  }

  private void initViews() {
    mLatitudeText = (TextView) findViewById((R.id.latitude_text));
    mLongitudeText = (TextView) findViewById((R.id.longitude_text));
    mAddressText = (TextView) findViewById(R.id.addres_text);
    temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);
    mWeatherIconImageView = (ImageView) findViewById(R.id.weatherIconImageView);
    mCityEditText = (EditText) findViewById(R.id.cityEditTExt);
  }
/*
  public void weatherIconDwonload(String weatherIcon){

    String iconUrl = "http://openweathermap.org/img/w/" + weatherIcon + ".png";

    Picasso.get(getApplicationContext().
  }*/


  @Override
  public void onWeatherLoaded(String temperature, String weatherIcon) {
    temperatureTextView.setText(temperature);
    if (temperature != null) {
      this.temperature = temperature;
    }

   String iconUrl = "http://openweathermap.org/img/w/" + weatherIcon + ".png";
   Picasso.get().load(iconUrl).into(mWeatherIconImageView);
  }

  @Override
  public void onFailedWeatherLoading(Throwable throwable) {
    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
  }

  public void hideKAyboard(View view) {
    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
/*    String str1 = "str1";
    String str2 = "str2";
    blabla(s);*/
  }

  public void findWeather(View view) {
    cityForSearch = mCityEditText.getText().toString();
    mNetworkingManager.getCurrentTemperatureForCityForFiveDays(cityForSearch);

/*    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }*/

    /*Bundle bundle = new Bundle();
    bundle.putStringArrayList("weatherIconForFiveDays", new ArrayList<>(Arrays.asList(weatherIcinForFiveDay)));
    bundle.putStringArrayList("temperatureForFiveDays", new ArrayList<>(Arrays.asList(temperatureForFiveDay)));
    bundle.putStringArrayList("dataForFiveDays", new ArrayList<>(Arrays.asList(dataForFiveDay)));
    // set Fragmentclass Argument
    FragmentClass fragobj = new FragmentClass();
    fragobj.setArguments(bundle);

    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
    fragmentTransaction.add(R.id.weatherContainer, fragobj);
    fragmentTransaction.commit();

*/


  }

  @Override
  public void onWeatherLoadedForFiveDays(String[] weatherIcinForFiveDay, String[] temperatureForFiveDay, String[] dataForFiveDay) {
    this.weatherIcinForFiveDay = weatherIcinForFiveDay;
    this.temperatureForFiveDay = temperatureForFiveDay;
    this.dataForFiveDay = dataForFiveDay;
  }

  @Override
  public void onFailedWeatherLoadingForFiveDays(Throwable throwable) {

  }
}


