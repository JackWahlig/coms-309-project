package com.example.basicanimations;

import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class Animation extends AppCompatActivity {

    //Screen Size
    private int screenWidth;
    private int screenHeight;

    //Images
    private ImageView aUp;
    private ImageView aDown;
    private ImageView aLeft;
    private ImageView aRight;

    //Positions
    private float aUpX;
    private float aUpY;
    private float aDownX;
    private float aDownY;
    private float aLeftX;
    private float aLeftY;
    private float aRightX;
    private float aRightY;

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        aUp = (ImageView) findViewById(R.id.aUp);
        aDown = (ImageView) findViewById(R.id.aDown);
        aLeft = (ImageView) findViewById(R.id.aLeft);
        aRight = (ImageView) findViewById(R.id.aRight);

        WindowManager wm = getWindowManager();
        Display dis = wm.getDefaultDisplay();
        Point size = new Point();
        dis.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        aUp.setX(-800.0f);
        aUp.setY(-800.0f);
        aDown.setX(-800.0f);
        aDown.setY(screenHeight + 800.0f);
        aRight.setX(screenWidth + 800.0f);
        aRight.setY(-800.0f);
        aLeft.setX(-800.f);
        aLeft.setY(-800.f);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        changePos();
                    }
                });
            }
        }, 0,20);




    }

    public void changePos(){

        aUpY -= 10;
        if(aUp.getY() + aUp.getHeight() < 0){
            aUpX = (float) Math.floor(Math.random() * (screenWidth - aUp.getWidth()));
            aUpY = screenHeight + 100f;
        }
        aUp.setX(aUpX);
        aUp.setY(aUpY);

        aDownY += 10;
        if(aDown.getY() > screenHeight){
            aDownX = (float) Math.floor(Math.random() * (screenWidth - aDown.getWidth()));
            aDownY = -1000f;
        }
        aDown.setX(aDownX);
        aDown.setY(aDownY);

        aLeftX -= 10;
        if(aLeft.getX() + aLeft.getWidth() < 0){
            aLeftY = (float) Math.floor(Math.random() * (screenHeight - aLeft.getWidth()));
            aLeftX = screenWidth + 100f;
        }
        aLeft.setX(aLeftX);
        aLeft.setY(aLeftY);

        aRightX += 10;
        if(aRight.getX() > screenWidth){
            aRightY = (float) Math.floor(Math.random() * (screenHeight - aRight.getWidth()));
            aRightX = -1000f;
        }
        aRight.setX(aRightX);
        aRight.setY(aRightY);
    }
}
