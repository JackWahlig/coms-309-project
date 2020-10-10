package com.example.hello_android;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class VolleyDemo extends AppCompatActivity {

    Button button1, button2, button3, button4;
    TextView textView, name, email, phone;
    ImageView imageView;
    String server_url_text = "https://api.androidhive.info/volley/string_response.html";
    String server_url_image = "https://api.androidhive.info/volley/volley-image.jpg";
    String server_url_json_object = "https://api.androidhive.info/volley/person_object.json";


    //RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley_demo);

        button1 = (Button)findViewById(R.id.bn1);
        button2 = (Button)findViewById(R.id.bn2);
        button3 = (Button)findViewById(R.id.bn3);
        button4 = (Button)findViewById(R.id.bn4);
        textView = (TextView)findViewById(R.id.txt);
        name = (TextView)findViewById(R.id.name);
        email = (TextView)findViewById(R.id.email);
        phone = (TextView)findViewById(R.id.phone);
        imageView = (ImageView)findViewById(R.id.img);
//        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
//        Network network = new BasicNetwork(new HurlStack());
//        requestQueue = new RequestQueue(cache, network);
//        requestQueue.start();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringRequest stringRequest = new StringRequest(Request.Method.GET, server_url_text,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                textView.setText(response);
                                //requestQueue.stop();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText("Something went wrong...");
                        error.printStackTrace();
                        //requestQueue.stop();
                    }
                });
                //requestQueue.add(stringRequest);
                Mysingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageRequest imageRequest = new ImageRequest(server_url_image, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VolleyDemo.this, "Sometheing went wrong", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });

                Mysingleton.getInstance(getApplicationContext()).addToRequestque(imageRequest);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, server_url_json_object, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            name.setText(response.getString("name"));
                            email.setText(response.getString("email"));
                            phone.setText(response.getJSONObject("phone").getString("home"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VolleyDemo.this, "Sometheing went wrong", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });
                Mysingleton.getInstance(VolleyDemo.this).addToRequestque(jsonObjectRequest);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   startActivity(new Intent(VolleyDemo.this, DisplayList.class));
            }
        });
    }
}
