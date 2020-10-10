package com.example.hello_android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.util.AttributeSet;



import java.util.jar.Attributes;

public class HomeActivity extends Activity implements JoystickView.JoystickListener{

    private ViewGroup mainLayout;
    private ImageView image;
    private JoystickView joystickleft;
    private JoystickView joystickright;

    private int xDelta;
    private int yDelta;

    private float centX;
    private float centY;
    private float radius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Image Experiment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mainLayout = (RelativeLayout) findViewById(R.id.main);
        image = (ImageView) findViewById(R.id.image);

        image.setOnTouchListener(onTouchListener());

        //Joystick Experiment
        joystickleft = (JoystickView) findViewById(R.id.joystickLeft);
        joystickright = (JoystickView) findViewById(R.id.joystickRight);

    }

    private OnTouchListener onTouchListener() {
        return new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                view.getLayoutParams();

                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;
                        break;

                    case MotionEvent.ACTION_UP:
                        Toast.makeText(HomeActivity.this,
                                "thanks for new location!", Toast.LENGTH_SHORT)
                                .show();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                .getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        view.setLayoutParams(layoutParams);
                        break;
                }
                mainLayout.invalidate();
                return true;
            }
        };
    }

    public void onJoystickMoved(float xPercent, float yPercent, int id) {
        switch (id) {
            case R.id.joystickLeft:
                Log.d("Left Joystick", "X percent: " + xPercent + "Y Percent: " + yPercent);
                break;
            case R.id.joystickRight:
                Log.d("Right Joystick", "X percent: " + xPercent + "Y Percent: " + yPercent);
            break;
        }

        float imageX = image.getX();
        float imageY = image.getY();

        image.setX(imageX + 10 * xPercent);
        image.setY(imageY + 10 * yPercent);
    }
}