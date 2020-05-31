package com.barlis.dailytopnews.Fragments;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.barlis.dailytopnews.R;
import com.barlis.dailytopnews.ReminderBroadcast;

public class SettingsFragment extends Fragment
{
    SwitchCompat switchCompat;
    LinearLayout linearLayout;
    RadioGroup radioGroup;
    RadioButton radioButtonMin, radioButtonHour, radioButtonDay;
    Button saveBtn;
    int time, radioId;
    static boolean switchChecked = false;
    SharedPreferences.Editor editor;
    AlarmManager alarmManager;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();

        switchChecked = sharedPreferences.getBoolean("check", false);

        createNotificationChannel();

        switchCompat = v.findViewById(R.id.switchNotif);

        linearLayout = v.findViewById(R.id.notifyAlert);

        radioGroup = v.findViewById(R.id.radioGroup);
        radioButtonMin = v.findViewById(R.id.radioBtnMin);
        radioButtonHour = v.findViewById(R.id.radioBtnHour);
        radioButtonDay = v.findViewById(R.id.radioBtnDay);

        saveBtn = v.findViewById(R.id.saveBtn);

        if(switchChecked)
        {
            switchCompat.setChecked(true);
            turnOn();
        }

        switchCompat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switchChecked = switchCompat.isChecked();
                if(switchChecked)
                {
                    turnOn();
                    editor.putBoolean("check", true);
                    editor.apply();
                }
                else
                {
                    radioGroup.clearCheck();

                    linearLayout.setVisibility(View.GONE);

                    saveBtn.setVisibility(View.GONE);

                    time = 0;

                    setNotif();

                    editor.putBoolean("check", false);
                    editor.apply();
                }
            }
        });

        return v;
    }

    private void turnOn()
    {
        linearLayout.setVisibility(View.VISIBLE);

        saveBtn.setVisibility(View.VISIBLE);

        saveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                radioId = radioGroup.getCheckedRadioButtonId();

                if(radioId == -1)
                {
                    Toast.makeText(getActivity(), "Time not selected", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("check", false);
                    editor.apply();
                }
                else
                {
                    findRadioButton(radioId);

                    setNotif();

                    Toast.makeText(getActivity(), "Settings saved", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("check", true);
                    editor.apply();
                }
            }
        });
    }

    private void setNotif()
    {
        Intent intent = new Intent(getContext(), ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.getIntentSender();

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        long timeAtClicked = System.currentTimeMillis();

        long sec = 1000 * time;

        if(sec == 0)
        {
            alarmManager.cancel(pendingIntent);
        }
        else
        {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    timeAtClicked + sec,
                    sec,
                    pendingIntent);
        }
    }

    private void findRadioButton(int radioId)
    {
        switch (radioId)
        {
            case R.id.radioBtnMin:
            {
                time = 60;
                break;
            }
            case R.id.radioBtnHour:
            {
                time = 3600;
                break;
            }
            case R.id.radioBtnDay:
            {
                time = 86400;
                break;
            }
        }
    }

    private void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "NotifyReminderChannel";
            String description = "Channel for notify Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyApp", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
