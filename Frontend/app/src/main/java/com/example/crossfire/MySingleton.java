package com.example.crossfire;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Singleton pattern Volley request queue used to take server requests
 */
public class MySingleton {
    /**
     * The sinlge instance of the Singleton object
     */
    private static MySingleton mInstance;
    /**
     * The Volley server request queue
     */
    private RequestQueue requestQueue;
    /**
     * The context of the application sending a request to the queue
     */
    private static Context mCtx;

    /**
     * Constructs a MySingleton object using the context of the parent application and createing a request queue
     * @param context Activity of the parent activity
     */
    public MySingleton(Context context) {
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    /**
     * Creates a new request queue if one does not exist. It then returns the Volley request queue of the MySingleton object
     * @return Volley request queue of the MySingleton object
     */
    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }

        return requestQueue;
    }

    /**
     * Returns this instance of the MySingleton object
     * @param context Context of the parent activity
     * @return The single instance of the MySingleton object
     */
    public static synchronized MySingleton getInstance(Context context){
        if(mInstance == null) {
            mInstance = new MySingleton(context);
        }

        return mInstance;
    }

    /**
     * Adds a Volley server request to the request queue
     * @param request The Volley request to be added to the queue
     * @param <T> The generic type of the server request
     */
    public<T> void addToRequestque(Request<T> request) {
        requestQueue.add(request);
    }
}
