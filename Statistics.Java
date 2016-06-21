Java
package com.example.grace.supercrutch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.grace.supercrutch.DatabaseHelper;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Stats extends AppCompatActivity {

    String variable1, variable_weight;
    float high_threshold, weight;
    String variable2;
    float low_threshold;
    public static final String DEFAULT="N/A";
    String getDataString[] = new String[4000];
    float data[] = new float[4000];
    int x = 0;
    int w = 0;
    double x_value = 0;
    int OverThreshold = 0;
    int UnderThreshold = 0;
    SharedPreferences threshold;
    SharedPreferences threshold2;
    public static final String AboveThreshold = "highKey";
    public static final String BelowThreshold = "lowKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // home button

        Button button = (Button) findViewById(R.id.home);

        View.OnClickListener a= new View.OnClickListener() {
            public void onClick(View v) {
                Intent new_activity = new Intent(Stats.this, MyActivity.class);
                startActivity(new_activity);
            }
        };

        button.setOnClickListener(a);

        //get patient's body weight from shared preferences
        SharedPreferences sharedPreferences_weight=getSharedPreferences("My_Current_Patient", Context.MODE_WORLD_READABLE);
        variable_weight=sharedPreferences_weight.getString("weight",DEFAULT);
        weight=Float.valueOf(variable_weight);

        //get high and low threshold from shared preferences
        SharedPreferences sharedPreferences_my_value1=getSharedPreferences("My_Current_Patient", Context.MODE_WORLD_READABLE);
        variable1=sharedPreferences_my_value1.getString("high_threshold",DEFAULT);
        high_threshold=Float.valueOf(variable1);

        SharedPreferences sharedPreferences_my_value2=getSharedPreferences("My_Current_Patient", Context.MODE_WORLD_READABLE);
        variable2=sharedPreferences_my_value2.getString("low_threshold",DEFAULT);
        low_threshold=Float.valueOf(variable2);

        DatabaseHelper myDb = new DatabaseHelper(this);
        //force = myDb.getForce();

        //read from database
        Cursor result_1 = myDb.getAllData();
        if(result_1.getCount() == 0){
            Toast.makeText(getBaseContext(),"Error, Nothing Found",Toast.LENGTH_LONG).show();
            return;
        }

        else{
            Toast.makeText(getBaseContext(),"There is data",Toast.LENGTH_LONG).show();
        }

        while(result_1.moveToNext()){
            getDataString[x] = result_1.getString(1);
            x++;
        }

        for(int i=0;i<x;i++){
            data[i] = (((weight - (2*Float.parseFloat(getDataString[i])))/weight)*100);
        }

        //count number of times above and below the threshold
       while (w < data.length) {

            if (data[w] > high_threshold) {
                OverThreshold++;
            }

            if (data[w] < low_threshold){
                UnderThreshold++;
            }

            w++;
        };

       //write to file
        OutputStreamWriter outputWriter = null;
        try {
            FileOutputStream file = openFileOutput("over_threshold.txt",Context.MODE_APPEND);
            outputWriter = new OutputStreamWriter(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //save threshold to shared preferences
        /*SharedPreferences sharedPreferences_threshold = getSharedPreferences("Threshold", Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = sharedPreferences_threshold.edit();
        editor.putInt("above_threshold", OverThreshold);
        editor.apply();*/
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String time;
        time = sdf1.format(new Date());
        String temp ="Date: "+time+" Number of Times Over the Threshold: "+ String.valueOf(OverThreshold);

        try {
            outputWriter.append(temp);
            //outputWriter.append("\n\r");
            outputWriter.flush();
            outputWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //write to file
        OutputStreamWriter outputWriter2 = null;
        try {
            FileOutputStream file2 = openFileOutput("under_threshold.txt",Context.MODE_APPEND);
            outputWriter2 = new OutputStreamWriter(file2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //save threshold to shared preferences
        /*SharedPreferences sharedPreferences_threshold_2 = getSharedPreferences("Threshold_low", Context.MODE_WORLD_READABLE);
        SharedPreferences.Editor editor_low = sharedPreferences_threshold_2.edit();
        editor_low.putInt("low_threshold", UnderThreshold);
        editor_low.apply();*/
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String time2;
        time2 = sdf2.format(new Date());
        String temp2 ="Date: "+time2+" Number of Times Under the Threshold: "+ String.valueOf(UnderThreshold);

        try {
            outputWriter2.append(temp2);
            //outputWriter2.append("\n\r");
            outputWriter2.flush();
            outputWriter2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



        //plot weight against time
        GraphView weight_graph = (GraphView) findViewById(R.id.graph);
        for(int j=0;j<x;j++)
        {
            PointsGraphSeries<DataPoint> weight_series = new PointsGraphSeries<DataPoint>(new DataPoint[]{
                    new DataPoint(x_value, data[j]),
            });

            weight_series.setSize(3);
            weight_graph.addSeries(weight_series);

            // tap listener
            weight_series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(Stats.this, "Series: On Data Point clicked: " + dataPoint, Toast.LENGTH_SHORT).show();


                }
            });
            x_value = x_value + 0.05;
        }
        weight_graph.getViewport().setScalable(true);
        weight_graph.getViewport().setScrollable(true);
        weight_graph.getViewport().setYAxisBoundsManual(true);
        weight_graph.getViewport().setMinY(0.0);
        weight_graph.getViewport().setMaxY(100);
        weight_graph.getGridLabelRenderer().setVerticalAxisTitle("Percentage Body Weight");

        //plot high threshold
        GraphView high_threshold_graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series_high = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, high_threshold),
                new DataPoint(x_value, high_threshold)
        });
        series_high.setColor(Color.RED);
        series_high.setThickness(2);
        high_threshold_graph.addSeries(series_high);
        high_threshold_graph.getViewport().setScalable(true);
        high_threshold_graph.getViewport().setScrollable(true);

        //plot low threshold
        GraphView low_threshold_graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series_low = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, low_threshold),
                new DataPoint(x_value, low_threshold)
        });
        //series_low.setBackgroundColor(Color.rgb(255, 204, 204));
        //series_low.setDrawBackground(true);
        series_low.setColor(Color.RED);
        series_low.setThickness(2);
        low_threshold_graph.addSeries(series_low);
        low_threshold_graph.getViewport().setScalable(true);
        low_threshold_graph.getViewport().setScrollable(true);
    }
}

