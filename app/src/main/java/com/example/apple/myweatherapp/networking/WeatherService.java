package com.example.apple.myweatherapp.networking;

import com.example.apple.myweatherapp.model.WeatherInfo;
import com.example.apple.myweatherapp.model2.Example;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface WeatherService {

  @GET("/data/2.5/weather")
  Call<WeatherInfo> getWeatherByCity(@Query("q") String city, @Query("appid") String appid);

  @GET("data/2.5/forecast/daily")
  Call<Example> getWeatherByCityForFiveDays(@Query("q") String city, @Query("units") String units, @Query("cnt") String cnt, @Query("mode") String mode, @Query("apikey") String apikey);



}


