package com.example.apple.myweatherapp.background;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadTask extends AsyncTask<String, Void, String> {


  @Override
  protected String doInBackground(String... urls) {

    String result = "";
    URL url;
    HttpURLConnection urlConnection = null;


    try {
      url = new URL(urls[0]);
      urlConnection = (HttpURLConnection) url.openConnection();
      InputStream in = urlConnection.getInputStream();
      InputStreamReader reader = new InputStreamReader(in);
      int data = reader.read();
      while (data != -1) {
        char current = (char) data;
        result += current;
        data = reader.read();
      }
      return result;


    } catch (Exception e) {

      e.printStackTrace();

    }


    return null;
  }

  @Override
  protected void onPostExecute(String result) {
    super.onPostExecute(result);

    Log.i("Info", result);

    try {
      JSONObject jsonObject = new JSONObject(result);
      JSONObject weatherData = new JSONObject(jsonObject.getString("main"));
      JSONObject placeData = new JSONObject(jsonObject.getString("sys"));
      Double temperature = Double.parseDouble(weatherData.getString("temp"));
      int temperatureFarengate = (int) (temperature * 1);
      String city = jsonObject.getString("name");
      String country = placeData.getString("country");
      Log.i("WeatherInfo", String.valueOf(temperatureFarengate));
      Log.i("WeatherInfo", country);
      Log.i("WeatherInfo", city);
/*

            MainActivity.nameTextView.setText(city);
            MainActivity.temperatureTextView.setText(String.valueOf(temperatureFarengate));
*/


    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
