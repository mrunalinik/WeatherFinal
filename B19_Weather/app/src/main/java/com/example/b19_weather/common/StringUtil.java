package com.example.b19_weather.common;

/**
 * Created by chethan on 12/1/2015.
 */
public class StringUtil {

    public static String getValidString(String city){
        if(city != null && city.length() > 0){
            city = city.trim(); //Remove space in the beginning and the end
            city = city.replace(" ", "_"); //Replace spaces between the words with _
        }
        return city;
    }
}
