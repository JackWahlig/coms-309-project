package com.example.crossfire;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Acvtivity for Logging in users into the app
 */
public class LoginActivity extends Activity {
    /**
     * Button used to navigate to Regsiter Acvitity
     */
    Button register;
    /**
     * Button used to submit login credentials and attempt to login in via server
     */
    Button login;
    /**
     * Field for entering username
     */
    EditText Username;
    /**
     * Field for entering user's password
     */
    EditText Password;
    /**
     * Text holding the user's entered username
     */
    String username;
    /**
     * Text holding the user's entered password
     */
    String password;
    /**
     * URL for connecting to server to check for valud login credentials
     */
    String login_url = "http://cs309-lr-1.misc.iastate.edu:8080/players/login";
    /**
     * Builder used to create alerts to notify the user of errors while logging in
     */
    AlertDialog.Builder builder;

    /**
     * God method of Login Activity. Sets views and buttons for the the activity screen. Listens for clicks on eith erthe login
     * or register button. If login button is clicked, a request is senst to the server to validate the username and password credentials the user
     * has entered. If login is successful, the user is taken to the Main Menu Activity, otherwise a dialog informs them of an error that occured.
     * If register button is clicked, the user is taken to the Register Activity.
     * @param savedInstanceState Saved instance state of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = (Button)findViewById(R.id.bn_register);
        login = (Button)findViewById(R.id.bn_login);

        Username = (EditText)findViewById(R.id.login_name);
        Password = (EditText)findViewById(R.id.login_password);

        builder = new AlertDialog.Builder(LoginActivity.this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = Username.getText().toString();
                password = Password.getText().toString();

                if(username.equals("") || password.equals("")) {
                    builder.setTitle("Something went wrong...");
                    displayAlert("All fields must be filled");
                }
                else {
                    JSONObject jsonbody = new JSONObject();
                    try {
                        jsonbody.put("username", username);
                        jsonbody.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, login_url, jsonbody,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String loginFailure = response.optString("playerInvalid", "false");
                                        if(loginFailure.equals("true")) {
                                            builder.setTitle("Login Error...");
                                            displayAlert("Username does not exists.");
                                        }
                                        else {
                                            Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("username", response.getString("username"));
                                            bundle.putInt("playerId", response.getInt("playerId"));
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    }){
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            Map<String, String> params = new HashMap<String, String>();
//                            params.put("user_name", username);
//                            params.put("password", password);
//
//                            return params;
//                        }
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json; charset=utf-8");
                            return headers;
                        }
                    };
                    MySingleton.getInstance(LoginActivity.this).addToRequestque(jsonObjectRequest);
                }
            }
        });
    }

    /**
     * Displays message toasts on screen, informing the user of why a login attempt failed
     * @param message Message to show the user from the server
     */
    public void displayAlert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Username.setText("");
                Password.setText("");
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
