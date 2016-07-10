package com.example.b19_weather.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.b19_weather.MainActivity;
import com.example.b19_weather.R;
import com.example.b19_weather.common.StringUtil;
import com.example.b19_weather.common.WConstants;
import com.example.b19_weather.interfaces.WeatherTaskCallback;
import com.example.b19_weather.parser.Info;
import com.example.b19_weather.tasks.WeatherTask;

/**
 * Created by chethan on 12/2/2015.
 */
public class WeatherService extends Service{

    private Notification notif;

    @Override
    public void onCreate() {
        super.onCreate();

        String state = "IL";
        String city = "Chicago";

        state = StringUtil.getValidString(state);
        city = StringUtil.getValidString(city);

        String url = WConstants.URL_HEAD + state + "/" + city + WConstants.URL_TAIL;
        String[] urlArray = new String[]{url};

        WeatherTask weatherTask = new WeatherTask(taskCallback);
        weatherTask.execute(urlArray);

       PreferenceManager.getDefaultSharedPreferences(WeatherService.this).edit().putString(WConstants.ALARM_KEY, "").commit();
    }

    WeatherTaskCallback taskCallback = new WeatherTaskCallback() {
        @Override
        public void getWeatherData(Info info) {
            //Show the notification
            String message = "Temp in f = "+info.getCurrent_observation().getTemp_f();
            createNotification(message);
            showNotification();
        }
    };

    private void createNotification(String message){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(WeatherService.this);
        builder.setContentTitle("Weather data");
        builder.setContentText(message);
        builder.setSmallIcon(R.drawable.and);

        Intent intent = new Intent(WeatherService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(WeatherService.this, 234532, intent, 0);

        builder.setContentIntent(pendingIntent);

        notif = builder.build();
    }

    private void showNotification(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notif.defaults = Notification.DEFAULT_ALL;
        notificationManager.notify(10, notif);

        //notificationManager.cancel(10);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
