package com.dicoding.picodiploma.githubuserapi.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import com.dicoding.picodiploma.githubuserapi.R;
import com.dicoding.picodiploma.githubuserapi.alarm.AlarmReceiver;

public class SettingActivity extends AppCompatActivity {
    Switch dailyReminder;
    SharedPreferences sharedPreferences;
    private AlarmReceiver alarmReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        alarmReceiver = new AlarmReceiver();

        dailyReminder = findViewById(R.id.daily_switch);
        checkSharedPreference();
        dailyReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked){
                    Log.d("onCheckedChanged: ","true");
                    saveSharedPreference(1);
                    setAlarmReceiver();
                    Toast.makeText(SettingActivity.this, "Setting Daily Notification Success", Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("onCheckedChanged: ","false");
                    saveSharedPreference(0);
                    cancelAlarmReceiver();
                    Toast.makeText(SettingActivity.this, "Cancel Daily Notification Success", Toast.LENGTH_SHORT).show();
                }

            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Setting Reminder");
        }
    }

    private void setAlarmReceiver(){
        alarmReceiver.setTimeAlarm(getApplicationContext());
    }

    private void cancelAlarmReceiver(){
        alarmReceiver.cancelAlarm(getApplicationContext());
    }

    private void saveSharedPreference(int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("notification_reminder",value);
        editor.apply();
    }

    private void checkSharedPreference(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int statusChecked = sharedPreferences.getInt("notification_reminder",0);
        if (statusChecked  ==1) {
            dailyReminder.setChecked(true);
        }else {
            dailyReminder.setChecked(false);
        }
    }
}
