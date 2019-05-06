package com.dream.somnipotent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;

import java.util.Calendar;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final SharedPreferences sharedPref = getSharedPreferences("com.dream.somnipotent.settings",MODE_PRIVATE);
        final SharedPreferences.Editor prefEditor = sharedPref.edit();

        final TextView waketext = findViewById(R.id.wakeuptime);
        waketext.setText(getFormattedTime(sharedPref.getInt("wakehour", 8), sharedPref.getInt("wakeminute", 0)));

        Button wakebutton = findViewById(R.id.wakeuptimechange);
        wakebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                String ampm;
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Settings.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        prefEditor.putInt("wakehour", selectedHour);
                        prefEditor.putInt("wakeminute", selectedMinute);
                        prefEditor.commit();
                        waketext.setText(getFormattedTime(selectedHour, selectedMinute));
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        final TextView sleeptext = findViewById(R.id.sleeptime);
        sleeptext.setText(getFormattedTime(sharedPref.getInt("sleephour", 21), sharedPref.getInt("sleepminute", 0)));

        Button sleepbutton = findViewById(R.id.sleeptimechange);
        sleepbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Settings.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        prefEditor.putInt("sleephour", selectedHour);
                        prefEditor.putInt("sleepminute", selectedMinute);
                        prefEditor.commit();
                        sleeptext.setText(getFormattedTime(selectedHour, selectedMinute));
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        final Switch rcSwitch = findViewById(R.id.rcSwitch);
        rcSwitch.setChecked(sharedPref.getBoolean("rcswitch", true));
        rcSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefEditor.putBoolean("rcswitch", isChecked);
                prefEditor.commit();
            }
        });

        Switch ldSwitch = findViewById(R.id.ldSwitch);
        ldSwitch.setChecked(sharedPref.getBoolean("ldswitch", true));
        ldSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefEditor.putBoolean("ldswitch", isChecked);
                prefEditor.commit();
            }
        });

        Switch debugSwitch = findViewById(R.id.debugSwitch);
        debugSwitch.setChecked(sharedPref.getBoolean("debugswitch", false));
        debugSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefEditor.putBoolean("debugswitch", isChecked);
                prefEditor.commit();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public String getFormattedTime(int hour, int minute) {
        if (hour > 12) {
            return hour-12 + ":" + String.format("%02d", minute) + " PM";
        }
        if (hour == 12) {
            return hour + ":" + String.format("%02d", minute) + " PM";
        }
        if (hour < 12) {
            return hour + ":" + String.format("%02d", minute) + " AM";
        }
        if (hour == 0) {
            return hour+12 + ":" + String.format("%02d", minute) + " AM";
        }
        return "";
    }
}
