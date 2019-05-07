package com.dream.somnipotent;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
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
                final Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Settings.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mcurrentTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        mcurrentTime.set(Calendar.MINUTE, selectedMinute);
                        mcurrentTime.set(Calendar.SECOND, 1);
                        prefEditor.putInt("wakehour", selectedHour);
                        prefEditor.putInt("wakeminute", selectedMinute);
                        prefEditor.commit();
                        waketext.setText(getFormattedTime(selectedHour, selectedMinute));
                        sendChannelOne(mcurrentTime);
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
                final Calendar mcurrentTime = Calendar.getInstance();
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
                        if(sharedPref.getBoolean("ldswitch", true)) {
                            mcurrentTime.set(Calendar.HOUR_OF_DAY, selectedHour - 1);
                            mcurrentTime.set(Calendar.MINUTE, selectedMinute);
                            mcurrentTime.set(Calendar.SECOND, 1);
                            sendChannelTwo(mcurrentTime);
                        }
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
                if(isChecked) {
                    final Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    int randomhour = (int) (Math.random() * (sharedPref.getInt("sleephour", 21) - 1) + (sharedPref.getInt("wakehour", 8) + 1));
                    int randomminute = (int) (Math.random() * 59 + 0);
                    mcurrentTime.set(Calendar.HOUR_OF_DAY, randomhour);
                    mcurrentTime.set(Calendar.MINUTE, randomminute);
                    mcurrentTime.set(Calendar.SECOND, 1);
                    sendChannelThree(mcurrentTime);
                }
                else{
                    cancelChannelThree();
                }
            }
        });

        Switch ldSwitch = findViewById(R.id.ldSwitch);
        ldSwitch.setChecked(sharedPref.getBoolean("ldswitch", true));
        ldSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefEditor.putBoolean("ldswitch", isChecked);
                prefEditor.commit();
                if(isChecked){
                    final Calendar mcurrentTime = Calendar.getInstance();
                    mcurrentTime.set(Calendar.HOUR_OF_DAY, sharedPref.getInt("sleephour", 9) - 1);
                    mcurrentTime.set(Calendar.MINUTE, sharedPref.getInt("sleepminute", 0));
                    mcurrentTime.set(Calendar.SECOND, 1);
                    sendChannelTwo(mcurrentTime);
                }
                else{
                    cancelChannelTwo();
                }
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

    private void sendChannelOne(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ChannelOneAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if(c.before(Calendar.getInstance()))
            c.add(Calendar.DATE,1);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void sendChannelTwo(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ChannelTwoAlaram.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if(c.before(Calendar.getInstance()))
            c.add(Calendar.DATE,1);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void sendChannelThree(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ChannelThreeAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if(c.before(Calendar.getInstance()))
            c.add(Calendar.DATE,1);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelChannelOne(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ChannelOneAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    private void cancelChannelTwo(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ChannelTwoAlaram.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    private void cancelChannelThree(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ChannelThreeAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.cancel(pendingIntent);
    }
}
