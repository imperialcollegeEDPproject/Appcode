package com.example.grace.supercrutch;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class Steps extends Bluetooth {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        // home button

        Button button = (Button) findViewById(R.id.home);

        View.OnClickListener a= new View.OnClickListener() {
            public void onClick(View v) {
                Intent new_activity = new Intent(Steps.this, MyActivity.class);
                startActivity(new_activity);
            }
        };


        button.setOnClickListener(a);

        // get gyroscope data from crutch, put in an array called gyroscope
        int x = 0;
        int NumberSteps =0;
        float dist = 0;
        int numsteps = 0;
        int data=0;
        int data2=0;
        String getStepString[] = new String[8000];
        String getDistanceString[] = new String[8000];
        String StepString[] = new String[8000];
        int step[] = new int[8000]; // needs changing, get data from crutch
        float distance[] = new float[8000]; // needs changing, get data from crutch

        DatabaseHelper myDb = new DatabaseHelper(this);
        Cursor result_2 = myDb.getAllData();
        /*if(result_2.getCount() == 0){
            Toast.makeText(getBaseContext(), "Error, Nothing Found", Toast.LENGTH_LONG).show();
            return;
        }

        else{
            Toast.makeText(getBaseContext(),"There is data",Toast.LENGTH_LONG).show();
        }*/

        String trick= "\r";
        while(result_2.moveToNext()){
            getDistanceString[x] = result_2.getString(2);
            getStepString[x] = result_2.getString(3);
            if(getStepString[x].equals(trick)){
                getStepString[x] = "0\r";
            }
            x++;
        }


        for(int y=0;y<x;y++){
            distance[y] = Float.parseFloat(getDistanceString[y]);
            StepString[y] = getStepString[y].split("\r")[0];
            step[y] = Integer.parseInt(StepString[y]);
        }


        while (data < step.length) {

            if(step[data]==1)// current reading from the array
            {
                NumberSteps=NumberSteps +1;
            }

            data++;

        }

        while(data2 < distance.length ) {

            dist = dist+distance[data2];
            data2++;
        }

        // display on screen

        //NumberSteps goes to box id nostep

        numsteps = NumberSteps *2;
        String message = "You have taken " + numsteps + " steps";
        TextView tv = (TextView) findViewById(R.id.nosteps);
        tv.setText(message);

        //Distance goes to box id distance
        float km;
        float miles;
        km = dist/1000;
        miles = 5*km/8;

        DecimalFormat df = new DecimalFormat("#.##");

        String message2 = "You have walked " + df.format(miles)+ " miles";
        TextView tv2 = (TextView) findViewById(R.id.distance);
        tv2.setText(message2);

        String message3 = "You have walked " + df.format(km)+ " km";
        TextView tv3 = (TextView) findViewById(R.id.distance2);
        tv3.setText(message3);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
