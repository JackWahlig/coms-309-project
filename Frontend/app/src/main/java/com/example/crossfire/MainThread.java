package com.example.crossfire;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.widget.Toast;

/**
 * Main thread that a game uses to run. Runs continuously and keeps track of the frame rates of the game. Calls update and draw
 * methods on the gamepanel object.
 */
public class MainThread extends Thread {
    public static final int MAX_FPS = 30;
    /**
     * Average FPS that the game is running at
     */
    private double averagefps;
    /**
     * Surfaceholder which holds the gamepanel object
     */
    private SurfaceHolder surfaceHolder;
    /**
     * Gamepanel which draws the actual game on screen
     */
    private GamePanel gamePanel;
    /**
     * Flag showing if the game is currently running or not
     */
    private boolean running;
    /**
     * Canvas to draw the game on in the Gamepanel
     */
    public static Canvas canvas;

    /**
     * Constructs a MainThread object which is passed a Surfacehodler object and the Gamepanel to draw the game on
     * @param surfaceHolder The srufaceholder of the gamepanel
     * @param gamePanel The gamepanel which the game will be drawn on
     */
    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    /**
     * Sets the game to running by setting its running flag to true
     * @param running The flag to be set to true
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * The main method of the MainThread which keeps track of frame data and calls draw() and update() methods
     * on the gamepanel every frame.
     */
    @Override
    public void run() {
        //Sleep is used for pregame message to appear
        try {
            sleep(6500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long startTime;
        long timeMillis = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 1;
        long totalTime = 0;
        long targetTime = 100/MAX_FPS;

        while(running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanel.update(frameCount);
                    this.gamePanel.draw(canvas);
                }
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                if(canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch(Exception e) {e.printStackTrace();}
                }
            }
            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMillis;

            try {
                if(waitTime > 0) {
                    this.sleep(waitTime);
                }
            } catch (Exception e) {e.printStackTrace();}

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if(frameCount == MAX_FPS) {
                averagefps = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 1;
                System.out.println(averagefps);
            }
        }
    }
}

