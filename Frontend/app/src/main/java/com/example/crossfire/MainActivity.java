package com.example.crossfire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * The initial activity of the application. Entrance portal to the rest of the app.
 */
public class MainActivity extends Activity {

    /**
     * Simply loads the screen of the activity. Contains a single button that when pressed, takes the user to
     * the Login Activity.
     * @param savedInstanceState Saved instance state of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    Button b1 = (Button) findViewById(R.id.enterGame);

    b1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }
    });

    }
}
