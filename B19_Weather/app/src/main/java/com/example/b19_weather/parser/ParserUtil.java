package com.example.b19_weather.parser;

import com.google.gson.Gson;

/**
 * Created by chethan on 11/20/2015.
 */
public class ParserUtil {

    public static Info getParsedData(String rawJson){

        Gson gson = new Gson();

        Info info = gson.fromJson(rawJson, Info.class);

        return info;
    }
}
