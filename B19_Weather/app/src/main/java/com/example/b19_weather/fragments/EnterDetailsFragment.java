package com.example.b19_weather.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.b19_weather.R;
import com.example.b19_weather.common.StringUtil;
import com.example.b19_weather.common.WConstants;
import com.example.b19_weather.interfaces.FragmentCallback;
import com.example.b19_weather.interfaces.WeatherTaskCallback;
import com.example.b19_weather.parser.Info;
import com.example.b19_weather.receivers.WeatherBR;
import com.example.b19_weather.tasks.WeatherTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by chethan on 11/24/2015.
 */
public class EnterDetailsFragment extends Fragment{

    private Button getDataBtn;
    private EditText cityET, stateET;
    private FragmentCallback fragmentCallback;
    private CheckBox alarmCheckBox;
    private SharedPreferences sharedPreferences;

    public void setFragmentCallback(FragmentCallback fragmentCallback){
        this.fragmentCallback = fragmentCallback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.enter_details_layout, container, false);

        getDataBtn = (Button) view.findViewById(R.id.getDataBtn);
        cityET = (EditText) view.findViewById(R.id.cityET);
        stateET = (EditText) view.findViewById(R.id.stateET);
        alarmCheckBox = (CheckBox) view.findViewById(R.id.alarmCheckBox);

        getDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = stateET.getText().toString();
                String city = cityET.getText().toString();

                state = StringUtil.getValidString(state);
                city = StringUtil.getValidString(city);

                String url = WConstants.URL_HEAD + state + "/" + city + WConstants.URL_TAIL;
                String[] urlArray = new String[]{url};

                WeatherTask weatherTask = new WeatherTask(taskCallback);
                weatherTask.execute(urlArray);
            }
        });

        alarmCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alarmCheckBox.isChecked()){ //true
                    //Show time picker dialog
                    showTimePickerDialog();
                }
                else{ //false
                    //remove any existing alarm

                    Intent intent = new Intent(getActivity(), WeatherBR.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 24352365, intent, 0);

                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);

                    alarmCheckBox.setText(getString(R.string.alarmString));

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(WConstants.ALARM_KEY, "");
                    editor.commit();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());


        String value = sharedPreferences.getString(WConstants.ALARM_KEY, "");

        if(value != null && value.length() > 0){
            alarmCheckBox.setText(value);
            alarmCheckBox.setChecked(true);
        }
        else {
            alarmCheckBox.setChecked(false);
            alarmCheckBox.setText(getString(R.string.alarmString));
        }

    }

    private void showTimePickerDialog(){

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {


                SimpleDateFormat sdf = new SimpleDateFormat("H:mm");

                try {
                    Date date = sdf.parse(timePicker.getCurrentHour()+":"+timePicker.getCurrentMinute());


                    SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");

                    String time = sdf2.format(date);

                    alarmCheckBox.setText("Alarm set at " + time);

                    //Set data in shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(WConstants.ALARM_KEY, "Alarm set at " + time);
                    editor.commit();

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());

                    setAlarm(cal);

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        }, hour, minute, false);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private void setAlarm(Calendar cal){
        Intent intent = new Intent(getActivity(), WeatherBR.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 24352365, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }


    WeatherTaskCallback taskCallback = new WeatherTaskCallback() {
        @Override
        public void getWeatherData(Info info) {
            fragmentCallback.weatherData(info);
        }
    };
}
