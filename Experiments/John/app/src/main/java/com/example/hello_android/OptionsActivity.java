package com.example.hello_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class OptionsActivity extends AppCompatActivity {

    SeekBar music, fx, brightness;
    TextView t1, t2, t3;
    Switch hints, notifications;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        music = (SeekBar) findViewById(R.id.MusicVolume);
        fx = (SeekBar) findViewById(R.id.FXVolume);
        brightness = (SeekBar) findViewById(R.id.Brightness);

        t2 = (TextView) findViewById(R.id.FXVolumeNum);
        t1 = (TextView) findViewById(R.id.MusicVolumeNum);
        t3 = (TextView) findViewById(R.id.BrightnessNum);

        hints = (Switch) findViewById(R.id.hints);
        notifications = (Switch) findViewById(R.id.notifications);

        music.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(OptionsActivity.this, "Music Volume bar progress is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
                t1.setText(progressChangedValue + "");
            }
        });

        fx.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(OptionsActivity.this, "FX Volume bar progress is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
                t2.setText(progressChangedValue + "");
            }
        });

        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(OptionsActivity.this, "Brightness bar progress is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
                t3.setText(progressChangedValue + "");
            }
        });

        hints.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    Toast.makeText(OptionsActivity.this, "Hints have turned on", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OptionsActivity.this, "Hints have turned off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    Toast.makeText(OptionsActivity.this, "Notifications have turned on", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OptionsActivity.this, "Notifications have turned off", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
