package com.example.grace.supercrutch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;


public class MyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        startService(new Intent(getBaseContext(), PushNotification.class));

        // find the ID of the buttons
        Button Button1 = (Button) findViewById(R.id.button);
        Button Button2 = (Button) findViewById(R.id.button2);
        Button Button3 = (Button) findViewById(R.id.button3);
        Button Button4 = (Button) findViewById(R.id.button4);
        Button Button5 = (Button) findViewById(R.id.button5);

        //go to the appropriate page after clicking button, clicks identified further down
        View.OnClickListener a = new View.OnClickListener() {
            public void onClick(View v) { // v is the button clicked
                if (v.getId() == R.id.button) { // compares ID of button clicked found using Button Button1 and compares to ID of clicked button. always TRUE
                    Intent new_activity = new Intent(MyActivity.this, Stats.class); // sets up link to desired page and assigns to new_activity
                   startActivity(new_activity); // changes to desired page
                }
            }
        };

        View.OnClickListener b = new View.OnClickListener() {
            public void onClick(View v) {
                if (v.getId() == R.id.button2) {
                    Intent new_activity = new Intent(MyActivity.this, Steps.class);
                    startActivity(new_activity);
                }
            }
        };

        View.OnClickListener c = new View.OnClickListener() {
            public void onClick(View v) {
                if (v.getId() == R.id.button3) {
                    Intent new_activity = new Intent(MyActivity.this, PainRating.class);
                    startActivity(new_activity);
                }
            }
        };

        View.OnClickListener d = new View.OnClickListener() {
            public void onClick(View v) {
                if (v.getId() == R.id.button4) {
                    Intent new_activity = new Intent(MyActivity.this, settings.class);
                    startActivity(new_activity);
                }
            }
        };

        View.OnClickListener e = new View.OnClickListener() {
            public void onClick(View v) {
                if (v.getId() == R.id.button5) {
                    Intent new_activity = new Intent(MyActivity.this, Bluetooth.class);
                    startActivity(new_activity);
                }
            }
        };

        //detect which button is clicked,
        Button1.setOnClickListener(a);
        Button2.setOnClickListener(b);
        Button3.setOnClickListener(c);
        Button4.setOnClickListener(d);
        Button5.setOnClickListener(e);
    }
}

