Scale Java
package com.example.grace.supercrutch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PainRating extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Pain = "painKey";
    public final static String PREFS = "PrefsFile";
    public static final String DEFAULT="N/A";
    public ImageButton image1;
    public ImageButton image2;
    public ImageButton image3;
    public ImageButton image4;
    public ImageButton image5;
    public ImageButton image6;
    String pain_level;
    String doctor_email;
    String patient_name;
    SharedPreferences sharedpreference;
    SharedPreferences settings;
    File pain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pain_rating);

        sharedpreference = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());

        // home button

        Button button = (Button) findViewById(R.id.home);

        View.OnClickListener a = new View.OnClickListener() {
            public void onClick(View v) {
                Intent new_activity = new Intent(PainRating.this, MyActivity.class);
                startActivity(new_activity);
            }
        };

        button.setOnClickListener(a);

        //Image Buttons
        image1 = (ImageButton) findViewById(R.id.myButton);
        image2 = (ImageButton) findViewById(R.id.imageButton);
        image3 = (ImageButton) findViewById(R.id.imageButton2);
        image4 = (ImageButton) findViewById(R.id.imageButton3);
        image5 = (ImageButton) findViewById(R.id.imageButton4);
        image6 = (ImageButton) findViewById(R.id.imageButton5);

        image1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                image1.setImageResource(R.drawable.pain0_select);
                image2.setImageResource(R.drawable.pain1);
                image3.setImageResource(R.drawable.pain2);
                image4.setImageResource(R.drawable.pain3);
                image5.setImageResource(R.drawable.pain4);
                image6.setImageResource(R.drawable.pain5);
                pain_level = "No Pain";
            }
        });

        image2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                image2.setImageResource(R.drawable.pain1_select);
                image1.setImageResource(R.drawable.pain0);
                image3.setImageResource(R.drawable.pain2);
                image4.setImageResource(R.drawable.pain3);
                image5.setImageResource(R.drawable.pain4);
                image6.setImageResource(R.drawable.pain5);
                pain_level = "Barely Noticeable";
            }
        });

        image3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                image3.setImageResource(R.drawable.pain2_select);
                image1.setImageResource(R.drawable.pain0);
                image2.setImageResource(R.drawable.pain1);
                image4.setImageResource(R.drawable.pain3);
                image5.setImageResource(R.drawable.pain4);
                image6.setImageResource(R.drawable.pain5);
                pain_level = "Mild Discomfort";
            }
        });

        image4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                image4.setImageResource(R.drawable.pain3_select);
                image1.setImageResource(R.drawable.pain0);
                image2.setImageResource(R.drawable.pain1);
                image3.setImageResource(R.drawable.pain2);
                image5.setImageResource(R.drawable.pain4);
                image6.setImageResource(R.drawable.pain5);
                pain_level = "Some Pain";
            }
        });

        image5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                image5.setImageResource(R.drawable.pain4_select);
                image1.setImageResource(R.drawable.pain0);
                image2.setImageResource(R.drawable.pain1);
                image3.setImageResource(R.drawable.pain2);
                image4.setImageResource(R.drawable.pain3);
                image6.setImageResource(R.drawable.pain5);
                pain_level = "Bad Pain";
            }
        });

        image6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                image6.setImageResource(R.drawable.pain5_select);
                image1.setImageResource(R.drawable.pain0);
                image2.setImageResource(R.drawable.pain1);
                image3.setImageResource(R.drawable.pain2);
                image4.setImageResource(R.drawable.pain3);
                image5.setImageResource(R.drawable.pain4);
                pain_level = "Very Bad Pain";
            }
        });


        //OK Button
        Button Button1 = (Button) findViewById(R.id.button);
        sharedpreference = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        settings = getSharedPreferences(PainRating.PREFS, MODE_PRIVATE);


        Button1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                if (System.currentTimeMillis() - settings.getLong("lastTimeActionDone", 0) < 86400000)
                {
                    //prevent pain rating from saving
                    Toast.makeText(getBaseContext(),"Sorry, you have already rated your pain today!", Toast.LENGTH_LONG).show();
                }
                else if (System.currentTimeMillis() >= 54000000)

                {
                    OutputStreamWriter outputWriter = null;
                    try {
                        FileOutputStream file = openFileOutput("pain.txt",Context.MODE_APPEND);
                        outputWriter = new OutputStreamWriter(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //save pain to shared preferences
                    SharedPreferences.Editor editor = sharedpreference.edit();
                    editor.putString(Pain, pain_level);
                    editor.apply();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    String time;
                    time=sdf1.format(new Date());
                    String temp ="Date: "+time+" Pain: "+pain_level;

                    try {
                        outputWriter.append(temp);
                        outputWriter.append("\n\r");
                        outputWriter.flush();
                        outputWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getBaseContext(),"Thank You!", Toast.LENGTH_LONG).show();

                    //save date and time of the saved pain to shared preferences
                    SharedPreferences.Editor editor2 = settings.edit();
                    editor2.putLong("lastTimeActionDone", System.currentTimeMillis());
                    editor2.apply();
                }
                else
                {
                    Toast.makeText(getBaseContext(),"Sorry, this feature is not available until 9pm!", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button button2 = (Button) findViewById(R.id.emailbutton);

        button2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                SharedPreferences sharedPreferences_my_value1 = getSharedPreferences("My_Current_Patient", Context.MODE_WORLD_READABLE);
                doctor_email = sharedPreferences_my_value1.getString("email", DEFAULT);
                SharedPreferences sharedPreferences_my_value2 = getSharedPreferences("My_Current_Patient", Context.MODE_WORLD_READABLE);
                patient_name = sharedPreferences_my_value2.getString("name", DEFAULT);

                //open pain text file
                String content_body ="-";
                try {
                    FileInputStream inputStream = openFileInput("pain.txt");
                    if (inputStream!=null){
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String receiveString = "";
                        StringBuilder stringBuilder = new StringBuilder();
                        while ( (receiveString = bufferedReader.readLine()) != null ) {
                            stringBuilder.append(receiveString);
                        }
                        inputStream.close();
                        content_body = stringBuilder.toString();
                    }
                }

                catch (FileNotFoundException e) {
                    Log.e("login activity", "File not found: " + e.toString());
                } catch (IOException e) {
                    Log.e("login activity", "Can not read file: " + e.toString());
                }

                //open over high threshold text file
                String content_body2 ="-";
                try {
                    FileInputStream inputStream = openFileInput("over_threshold.txt");
                    if (inputStream!=null){
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String receiveString = "";
                        StringBuilder stringBuilder = new StringBuilder();
                        while ( (receiveString = bufferedReader.readLine()) != null ) {
                            stringBuilder.append(receiveString);
                        }
                        inputStream.close();
                        content_body2 = stringBuilder.toString();
                    }
                }

                catch (FileNotFoundException e) {
                    Log.e("login activity", "File not found: " + e.toString());
                } catch (IOException e) {
                    Log.e("login activity", "Can not read file: " + e.toString());
                }

                //open over low threshold text file
                String content_body3 ="-";
                try {
                    FileInputStream inputStream = openFileInput("under_threshold.txt");
                    if (inputStream!=null){
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String receiveString = "";
                        StringBuilder stringBuilder = new StringBuilder();
                        while ( (receiveString = bufferedReader.readLine()) != null ) {
                            stringBuilder.append(receiveString);
                        }
                        inputStream.close();
                        content_body3 = stringBuilder.toString();
                    }
                }

                catch (FileNotFoundException e) {
                    Log.e("login activity", "File not found: " + e.toString());
                } catch (IOException e) {
                    Log.e("login activity", "Can not read file: " + e.toString());
                }

                //send email
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setType("message/rfc822");
                emailIntent.setData(Uri.parse("mailto: " + doctor_email));
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Patient: " + patient_name + "Information");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, patient_name + "'s pain rating is as follows: \r" + content_body + "\r\rNumber of times " + patient_name + " went over the high threshold: " + content_body2 + "\r\r Number of times " + patient_name + " went below the low threshold: " + content_body3);
                try{
                    startActivity(Intent.createChooser(emailIntent, "Send email using..."));
                } catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(PainRating.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
