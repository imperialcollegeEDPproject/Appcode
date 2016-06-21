//Settings Java
package com.example.grace.supercrutch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class settings extends AppCompatActivity {

    public EditText editText_name, editText_doctor_email, editText_crutch, editText_weight, editText_high_threshold, editText_low_threshold;
    public Button save, load;
    public static final String DEFAULT="N/A";

    // I should initialize the array here not in the on create method


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        //home button

        Button button = (Button) findViewById(R.id.home);

        View.OnClickListener a= new View.OnClickListener() {
            public void onClick(View v) {
                Intent new_activity = new Intent(settings.this, MyActivity.class);
                startActivity(new_activity);
            }
        };

        button.setOnClickListener(a);

        editText_name=(EditText) findViewById(R.id.input_name);
        editText_weight=(EditText) findViewById(R.id.input_weight);
        editText_doctor_email=(EditText) findViewById(R.id.input_arm);
        editText_crutch=(EditText) findViewById(R.id.input_crutch);
        editText_high_threshold=(EditText) findViewById(R.id.input_high_threshold);
        editText_low_threshold=(EditText) findViewById(R.id.input_low_threshold);

        save=(Button) findViewById(R.id.button_save);
        load=(Button) findViewById(R.id.button_load);

        save.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            button_save(view);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        load.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_load(view);
                    }
                }
        );
    }
    public void button_save (View view) throws IOException {

        OutputStreamWriter outputWriter = null;
        try {
            FileOutputStream file = openFileOutput("index.txt",Context.MODE_APPEND);
            outputWriter = new OutputStreamWriter(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // setting up the date string
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime;
        currentTime = sdf.format(new Date());

        // retrieving the value of the high_threshold
        String high_threshold = editText_high_threshold.getText().toString();

        // setting up the final string
        String temp_1 = "Date: " + currentTime + " - " + "High Threshold: " + high_threshold;
        String temp_2 =temp_1+',';
        outputWriter.append(temp_2);
        outputWriter.flush();
        outputWriter.close();

        // remember to initialise two more values as arm and crutch as float or maybe just cast them in the other code...
        SharedPreferences sharedPreferences = getSharedPreferences("My_Current_Patient", Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", editText_name.getText().toString());
        editor.putString("email", editText_doctor_email.getText().toString());
        editor.putString("crutch", editText_crutch.getText().toString());
        editor.putString("weight", editText_weight.getText().toString());
        editor.putString("high_threshold", editText_high_threshold.getText().toString());
        editor.putString("low_threshold", editText_low_threshold.getText().toString());
        editor.apply();

        Toast.makeText(this, "Data was saved successfully", Toast.LENGTH_LONG).show();
    }

    public void button_load (View view){
        Intent in = new Intent(settings.this,Load.class);
        startActivity(in);
    }

}

//Storing & Loading Settings Data Java
package com.example.grace.supercrutch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by francesengland on 8/3/2016.
 */
public class Load extends AppCompatActivity {

    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        // home button

        Button button = (Button) findViewById(R.id.home);

        View.OnClickListener a= new View.OnClickListener() {
            public void onClick(View v) {
                Intent new_activity = new Intent(Load.this, MyActivity.class);
                startActivity(new_activity);
            }
        };

        button.setOnClickListener(a);

        listView = (ListView) findViewById(R.id.list);

        String ret ="-";
        try {
            FileInputStream inputStream = openFileInput("index.txt");
            if (inputStream!=null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }

        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        String[] trick = ret.split(",");
        // build adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,trick);
        // Set the List
        listView.setAdapter(adapter);

    }
}
