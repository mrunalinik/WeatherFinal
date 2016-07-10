package com.example.b19_weather.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.b19_weather.R;
import com.example.b19_weather.parser.Info;

/**
 * Created by chethan on 11/24/2015.
 */
public class DisplayDetailsFragment extends Fragment{

    private TextView resultTv;
    private Info info;

    public void setInfo(Info info){
        this.info = info;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_details_layout, container, false);

        resultTv = (TextView) view.findViewById(R.id.resultTv);
        return view;

    }


    @Override
    public void onResume() {
        super.onResume();

        String message = "Temp in f = "+info.getCurrent_observation().getTemp_f();
        resultTv.setText(message);
    }
}
