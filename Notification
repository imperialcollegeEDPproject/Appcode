package com.example.grace.supercrutch;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;

import java.util.Calendar;

/**
 * Created by francesengland on 22/3/2016.
 */
public class PushNotification extends Service {

    @Override
    public IBinder onBind (Intent arg0){
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){//starts when start command is called, within main java function.
        // run until stopped

        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int min = now.get(Calendar.MINUTE);

        //System.currentTimeMillis() use maybe?
        //if ( hour ==9 && min == 51 )//75,600,000 is 9pm in milliseconds, the function gets the current time in milliseconds
        {
            Intent intent2 = new Intent();
            PendingIntent pIntent = PendingIntent.getActivity(PushNotification.this, 0, intent2, 0);
            Notification noti = new Notification.Builder(PushNotification.this)
                    .setTicker("TickerTitle")
                    .setContentTitle("SmartCrutch") // title to appear in push notification
                    .setContentText("Don't forget to rate your pain for today!") // text to display in push notification
                            // .addAction(R.mipmap.ic_launcher1,"Tap here to open the App", pIntent) //button to open the app (not working yet)
                    .setSmallIcon(R.drawable.logo2) // imports image for use in notification
                    .setColor(Color.BLUE)//set background colour of notification to blue
                    .setVibrate( new long[] {100, 250}) //make phone vibrate maybe?
                    .setContentIntent(pIntent)
                    .getNotification();


            noti.flags = Notification.FLAG_AUTO_CANCEL; //dismisses notification

            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(0, noti);

        }

        return 0;

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}

