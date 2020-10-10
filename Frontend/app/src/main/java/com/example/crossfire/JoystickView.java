package com.example.crossfire;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * A custom view of a virtual joystick. This is the way in which users interact with the game and control their turret and shield.
 */
public class JoystickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    /**
     * X position of the center of the joystick
     */
    private float centerX;
    /**
     * Y position of the center of the joystick
     */
    private float centerY;
    /**
     * Radius of the base circle of the joystick
     */
    private float baseRadius;
    /**
     * Radius of the hat of the joystick
     */
    private float hatRadius;
    /**
     * Listener which responds to joystick movements
     */
    private JoystickListener joystickCallback;


    /**
     * Constructs a JoystickView
     * @param context The context of the parent activity
     */
    public JoystickView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof  JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
    }

    /**
     * Constructs a JoystickView
     * @param context The context of the parent activity
     * @param attributes Attribute set
     * @param style Style
     */
    public JoystickView(Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof  JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
    }

    /**
     * Constructs a JoystickView
     * @param context the context of the parent activity
     * @param attributes Attribute set
     */
    public JoystickView(Context context, AttributeSet attributes) {
        super(context, attributes);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof  JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
    }

    /**
     * Sets the X and Y values of the center point of the JoystickView, the radius of its base
     * and the radius of its hat based on the view's dimensions
     */
    private void setupDimensions() {
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        baseRadius = (float) (Math.min(getWidth(), getHeight()) / 3.5);
        hatRadius = (float) (Math.min(getWidth(), getHeight()) / 5.5);
    }

    /**
     * Draws the joystick on screen based on where the hat is being directed by the user's interaction
     * @param newX The new X position of the hat
     * @param newY The new Y position of the hat
     */
    private void drawJoystick(float newX, float newY) {
        if(getHolder().getSurface().isValid()) {
            Canvas myCanvas = this.getHolder().lockCanvas();
            Paint colors = new Paint();
            myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            myCanvas.drawColor(Color.WHITE);
            colors.setARGB(255, 50, 50, 50);
            myCanvas.drawCircle(centerX, centerY, baseRadius, colors);
            colors.setARGB(255, 0 , 0, 255);
            myCanvas.drawCircle(newX, newY, hatRadius, colors);
            getHolder().unlockCanvasAndPost(myCanvas);
        }
    }

    /**
     * Initializes the Joystick view by calling setupDimenstions() method and the initial call to drawJoystick()
     * @param holder The surfaceholder of the joystick view
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setupDimensions();
        drawJoystick(centerX, centerY);
    }

    /**
     * Required method for creating a custom view. Not implemented
     * @param holder
     * @param format
     * @param width
     * @param height
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * Required method for creating a custom view. Not implemented
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * Calls joystickCallback and sends the information about the joystick when it is moved. Draws the joystick based on the new position of the hat
     * that the user has moved it to and calculates the X and Y displacement of the hat.
     * @param v
     * @param e
     * @return True.
     */
    public boolean onTouch(View v, MotionEvent e) {
        if(v.equals(this)) {
            if(e.getAction() != e.ACTION_UP) {
                float displacement = (float) Math.sqrt(Math.pow(e.getX() - centerX, 2) +
                        Math.pow(e.getY() - centerY, 2));
                if(displacement <= baseRadius) {
                    drawJoystick(e.getX(), e.getY());
//                    joystickCallback.onJoystickMoved((e.getX() - centerX) / baseRadius,
//                            (e.getY() - centerY) / baseRadius, getId());
                    joystickCallback.onJoystickMoved((e.getX() - centerX), (e.getY() - centerY), baseRadius, getId());
                }
                else {
                    float ratio = baseRadius / displacement;
                    float constrainedX = centerX + (e.getX() - centerX) * ratio;
                    float constrainedY = centerY + (e.getY() - centerY) * ratio;
                    drawJoystick(constrainedX, constrainedY);
//                    joystickCallback.onJoystickMoved((constrainedX - centerX) / baseRadius,
//                            (constrainedY - centerY) / baseRadius, getId());
                    joystickCallback.onJoystickMoved((constrainedX - centerX), (constrainedY - centerY), baseRadius, getId());
                }
            }
            else {
                drawJoystick(centerX, centerY);
                joystickCallback.onJoystickMoved(0, 0, baseRadius, getId());
            }
        }
        return true;
    }

    /**
     * Interface for objects which implement a JoystickView and want to view changes in the joystick's position
     */
    public interface JoystickListener {

        /**
         * Triggers when the joystick is moved. Receives the X and Y displacement of the joystick as well as the radius of its base and the id
         * of the joystick that moved.
         * @param xDisplacement The X displacement of the joystick from its resting position
         * @param yDisplacement The Y displacement of the joystcik from its resting position
         * @param baseRadius The radius of the base of the joystick
         * @param id The id of the joystick that moved
         */
        void onJoystickMoved(float xDisplacement, float yDisplacement, float baseRadius, int id);
    }
}