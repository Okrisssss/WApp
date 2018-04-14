package com.example.apple.myweatherapp.controllers;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apple.weatherapplication.R;

import java.util.ArrayList;
import java.util.List;


public class FragmentClass extends Fragment {

    private TextView weatherTextView;
    List<String> weatherIcinForFiveDay;
    List<String> temperatureForFiveDay;
    List<String> dataForFiveDay;
/*
    private String[] weatherIcinForFiveDay;
    private String[] temperatureForFiveDay;
    private String[] dataForFiveDay;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_fragment, container ,false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        weatherTextView = (TextView)view.findViewById(R.id.weatherTextView);

        String city = getArguments().getString("city");

        weatherIcinForFiveDay = getArguments().getStringArrayList("weatherIconForFiveDays");
        temperatureForFiveDay = getArguments().getStringArrayList("weatherIconForFiveDays");
        dataForFiveDay = getArguments().getStringArrayList("weatherIconForFiveDays");

        weatherTextView.setText(String.valueOf(temperatureForFiveDay.get(0)));
    }
}
