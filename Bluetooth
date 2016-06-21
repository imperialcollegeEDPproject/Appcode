Java
package com.example.grace.supercrutch;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class Bluetooth extends AppCompatActivity {

    Button openButton;
    Button sendButton;
    Button closeButton;
    Button clearData;
    Button viewData;
    TextView myLabel;
    EditText myTextBox;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);


        // home button

        Button button = (Button) findViewById(R.id.home);

        View.OnClickListener a= new View.OnClickListener() {
            public void onClick(View v) {
                Intent new_activity = new Intent(Bluetooth.this, MyActivity.class);
                startActivity(new_activity);
            }
        };

        button.setOnClickListener(a);

        myDb = new DatabaseHelper(this);
        openButton = (Button)findViewById(R.id.open);
        sendButton = (Button)findViewById(R.id.send);
        closeButton = (Button)findViewById(R.id.close);
        clearData = (Button)findViewById(R.id.clear_data);
        viewData = (Button)findViewById(R.id.view_data);
        myLabel = (TextView)findViewById(R.id.label);
        myTextBox = (EditText)findViewById(R.id.entry);
        openButton();
        sendButton();
        closeButton();
        viewData();
        clearData();
    }


    public void openButton(){
        //Open Button
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    findBT();
                    openBT();
                } catch (IOException ex) {
                }
            }
        });
    }

    public void sendButton(){
        //Send Button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendData();
                } catch (IOException ex) {
                }
                Toast.makeText(getBaseContext(), "Send Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void closeButton(){
        //Close Button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    closeBT();
                } catch (IOException ex) {
                }
            }
        });
    }

    public void clearData(){
        clearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer isClear = myDb.clearData();
                if (isClear > 0) {
                    Toast.makeText(getBaseContext(), "Data Is Cleared", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), "Data Is Not Cleared", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void viewData(){
        viewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor result = myDb.getAllData();
                if (result.getCount() == 0) {
                    showMessage("Error", "Nothing Found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while (result.moveToNext()) {     //abstract boolean - moves the cursor to the next row
                    buffer.append("ID : " + result.getString(0) + "\n");
                    buffer.append("FORCE : " + result.getString(1) + "\n");
                    buffer.append("DISTANCE : " + result.getString(2) + "\n");
                    buffer.append("STEP : " + result.getString(3) + "\n\n");
                }

                showMessage("Data", buffer.toString());
            }
        });
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


    public void findBT() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getBaseContext(), "No Bluetooth Adapter Available", Toast.LENGTH_LONG).show();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals("blueblue")) {
                    mmDevice = device;
                    break;
                }
            }
        }
        Toast.makeText(getBaseContext(), "Bluetooth Device Found", Toast.LENGTH_LONG).show();
    }

    public void openBT() throws IOException{
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();
        beginListenForData();
        Toast.makeText(getBaseContext(), "Bluetooth Opened", Toast.LENGTH_LONG).show();
    }

    public void beginListenForData(){
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character
        final byte x = 120;

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable() { //Thread is an object where two processes can run at the same time
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        int bytesAvailable = mmInputStream.available(); //returns the number of bytes that can be read from this input stream
                        int y = 0;
                        String values[] = new String[10];
                        if (bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);    //puts the bytes into bytesAvailable
                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                if (b == delimiter) {
                                    byte[] encodeBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodeBytes, 0, encodeBytes.length);
                                    final String data = new String(encodeBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                    for(String temp : data.split(" ")){
                                        values[y] = temp;
                                        y++;
                                    }

                                    if(y == 3){
                                            myDb.insertString(values[0], values[1], values[2]);
                                    }
                                    y = 0;
                                    handler.post(new Runnable() { //handler is for making operation on UI
                                        @Override
                                        public void run() {
                                            myLabel.setText(data);
                                        }
                                    });
                                }

                                else if(b == x){
                                    readBufferPosition = 0;
                                }

                                else {
                                    readBuffer[readBufferPosition++] = b;
                                }



                            }
                        }
                    } catch (IOException e) {
                        stopWorker = true;
                    }
                }
            }
        });
        workerThread.start();
    }

    public void sendData() throws IOException{
        String msg = myTextBox.getText().toString();
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        Toast.makeText(getBaseContext(), "Data Sent", Toast.LENGTH_LONG).show();
    }

    public void closeBT() throws IOException{
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        Toast.makeText(getBaseContext(), "Bluetooth Closed", Toast.LENGTH_LONG).show();
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

//Database Helper Java
package com.example.grace.supercrutch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * Created by francesengland on 14/3/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "student.db"; //case for student.db doesn't matter. both cases are treated the same
    public static final String TABLE_NAME = "student_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "FORCE";
    public static final String COL_3 = "DISTANCE";
    public static final String COL_4 = "STEPS";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,FORCE TEXT,DISTANCE TEXT,STEPS TEXT)");       //executes whatever query you pass into it, PRIMARY KEY is the way to identify each row
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertForce(Float force) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, force);
        db.insert(TABLE_NAME, null, contentValues);
    }

    public void insertDistance(Float distance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3, distance);
        db.insert(TABLE_NAME, null, contentValues);
    }

    public void insertSteps(Integer steps) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_4, steps);
        db.insert(TABLE_NAME, null, contentValues);
    }


    public void insertData(Float force, Float distance, Integer steps) {
        SQLiteDatabase db = this.getWritableDatabase();  //this creates the database and table
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, force);
        contentValues.put(COL_3, distance);
        contentValues.put(COL_4, steps);
        db.insert(TABLE_NAME, null, contentValues); //if the data is not inserted, this method is going to return -1
    }

    public void insertString(String force, String distance, String steps) {
        SQLiteDatabase db = this.getWritableDatabase();  //this creates the database and table
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, force);
        contentValues.put(COL_3, distance);
        contentValues.put(COL_4, steps);
        db.insert(TABLE_NAME, null, contentValues); //if the data is not inserted, this method is going to return -1
    }

    public Integer clearData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, null, null);
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME, null);
        return result;
    }

    public boolean updateData(String id, String name, String surname, String marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, surname);
        contentValues.put(COL_4, marks);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }

    public String getForce(){
        String force;
        String[] projection = {
                DatabaseHelper.COL_2};
        Cursor cursor = null;
        cursor = this.getReadableDatabase().query(DatabaseHelper.TABLE_NAME, projection, null, null, null, null, null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                force = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_2));
                return force;
            }
        }
        cursor.close();
        return " - ";
    }

}
