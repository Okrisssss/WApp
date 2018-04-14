package com.example.apple.myweatherapp.geolocation;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.example.apple.myweatherapp.controllers.MainActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyLocationManager {

  private Context context;

  public MyLocationManager(Context context) {
    this.context = context;
  }

  public Address getCurrentLocation(Location location) {
    Log.i("LocationInfo", location.toString());
    Geocoder geocoder = new Geocoder(context, Locale.getDefault());

    try {
      List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
      if (listAddresses != null && listAddresses.size() > 0) {
        return listAddresses.get(0);
      } else {
        Log.i(MainActivity.TAG, "Address object is empty");
        return null;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    Log.i(MainActivity.TAG, "Address object is null");
    return null;
  }


}
