package com.example.crossfire;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
 * Activity where the user can register and create an account for the game. Saves the user's new account information via the server
 * in the datatbase.
 */
public class RegisterActivity extends Activity {
    /**
     * Button the user clicks to submit their account information and register
     */
    Button reg_bn;
    /**
     * Field for the user to enter their desired username
     */
    EditText Username;
    /**
     * Field for the user to enter their desired password
     */
    EditText Password;
    /**
     * Field for the user to enter their desired password again for confirmation
     */
    EditText ConPassword;
    /**
     * The username the user has entered
     */
    String username;
    /**
     * The password the user has entered
     */
    String password;
    /**
     * The password the user has entered again to confirm
     */
    String conPassword;
    /**
     * Builder used to display dialog in the event of a user error when registering
     */
    AlertDialog.Builder builder;
    /**
     * URL for server request to create the new user
     */
    String reg_url = "http://cs309-lr-1.misc.iastate.edu:8080/players/register";

    /**
     * God method for Register Activity. Takes information in UI elements of the screen and sends a server request to try and create a new account
     * for the user. If account creation fails, an alert is displayed and the user may try again. Server request is sent on the register button click
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg_bn = (Button)findViewById(R.id.bn_reg);

        Username = (EditText)findViewById(R.id.reg_user);
        Password = (EditText)findViewById(R.id.reg_password);
        ConPassword = (EditText)findViewById(R.id.reg_con_password);

        builder = new AlertDialog.Builder(RegisterActivity.this);

        reg_bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = Username.getText().toString();
                password = Password.getText().toString();
                conPassword = ConPassword.getText().toString();

                if(username.equals("") || password.equals("") || conPassword.equals("")) {
                    builder.setTitle("Something went wrong...");
                    builder.setMessage("Please fill all fields");
                    displayAlert("input_error");
                }
                else if(!(password.equals(conPassword))) {
                    builder.setTitle("Something went wrong...");
                    builder.setMessage("Passwords must match");
                    displayAlert("input_error");
                }
                else {
                    JSONObject jsonbody = new JSONObject();
                    try {
                        jsonbody.put("username", username);
                        jsonbody.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, reg_url, jsonbody, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    String regFailure = response.optString("registration", "success");
                                    if(regFailure.equals("failure")) {
                                        builder.setTitle("Server Response...");
                                        builder.setMessage("User already exists. Please choose a different Username.");
                                        displayAlert("reg_failure");
                                    }
                                    else {
                                        builder.setTitle("Server Response...");
                                        builder.setMessage("Thank you for registering. You may now may login.");
                                        displayAlert("reg_success");
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    }){
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//                            Map<String, String> params = new HashMap<String, String>();
//                            params.put("username", username);
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
                    MySingleton.getInstance(RegisterActivity.this).addToRequestque(stringRequest);
                }
            }
        });
    }

    /**
     * Displays message toasts to the user if an error occurs while registering such as the username selected already being taken
     * or the passwords selected not matching.
     * @param str The string message to show the user
     */
    public void displayAlert(final String str) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(str.equals("input_error")) {
                    Password.setText("");
                    ConPassword.setText("");
                }
                else if(str.equals("reg_success")) {
                    finish();
                }
                else if(str.equals("reg_failed")) {
                    Username.setText("");
                    Password.setText("");
                    ConPassword.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
