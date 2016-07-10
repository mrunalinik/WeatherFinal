package com.example.b19_weather.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.b19_weather.interfaces.WeatherTaskCallback;
import com.example.b19_weather.parser.Info;
import com.example.b19_weather.parser.ParserUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by chethan on 11/19/2015.
 */
public class WeatherTask extends AsyncTask<String, Void, Info>{

    private final String TAG = "WeatherTask";
    private WeatherTaskCallback taskCallback;


    public WeatherTask(WeatherTaskCallback taskCallback){
        this.taskCallback = taskCallback;
    }

    @Override
    protected Info doInBackground(String... params) {
        //Network related code here

        String link = params[0];

        Log.i(TAG, "link = " + link);


        try {
            URL url = new URL(link);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(15000);

            conn.setRequestMethod("GET");
            conn.connect();

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String rawJson = "", data = "";

            while( (data = reader.readLine()) != null){
                rawJson = rawJson + data + "\n";
            }


            Log.i(TAG,"rawJson = "+rawJson);

            //Parse the json here
            Info info = ParserUtil.getParsedData(rawJson);
            return info;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Info info) {
        taskCallback.getWeatherData(info);
    }
}
