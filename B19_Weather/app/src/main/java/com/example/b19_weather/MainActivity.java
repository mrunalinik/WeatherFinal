package com.example.b19_weather;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.b19_weather.fragments.DisplayDetailsFragment;
import com.example.b19_weather.fragments.EnterDetailsFragment;
import com.example.b19_weather.interfaces.FragmentCallback;
import com.example.b19_weather.parser.Info;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        EnterDetailsFragment enterDetailsFragment = new EnterDetailsFragment();
        enterDetailsFragment.setFragmentCallback(fragmentCallback);
        transaction.replace(R.id.mainContainer, enterDetailsFragment);
        transaction.commit();

    }


    FragmentCallback fragmentCallback = new FragmentCallback() {
        @Override
        public void weatherData(Info info) {
            //Pass Info object to DisplayDetailsFragment
            //Remove Enter details fragment
            //Attach DisplayDetails Fragment


            DisplayDetailsFragment displayDetailsFragment = new DisplayDetailsFragment();
            displayDetailsFragment.setInfo(info);


            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainContainer, displayDetailsFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        }
    };

}
