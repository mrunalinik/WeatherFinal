package com.example.b19_weather.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.b19_weather.services.WeatherService;

/**
 * Created by chethan on 12/1/2015.
 */
public class WeatherBR extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        //Start the service

        Intent intent2 = new Intent(context, WeatherService.class);
        context.startService(intent2);
    }


}
