/**
 *  Author : Galo Paz
 *    Date : 08/28/2014
 *    Name : MessageToNotificationReceiver.java
 *     Tag : CS311D - Assignment 1.2
 * Purpose : Receives a broadcast and notifies the user of the new message
 *           through a notification in the Status bar.  When the Notification is
 *           pressed, it will send the user to an activity to display the 
 *           Message that was received.
 */
package com.glo.cs311d.receivers;

import com.glo.cs311d.R;

import android.annotation.TargetApi;
import android.app.*;
import android.content.*;
import android.os.Build;
import android.util.Log;

/************************ CLASS : MessageToLogReceiver ***********************/
public class MessageToNotificationReceiver extends BroadcastReceiver
{
    private static final String TAG = "MessageReceived";
    private static final long[] VIBRATE_PATTERN;
    static{
        VIBRATE_PATTERN = new long[]
                {
                100,150,
                100,150,
                100,150,
                100,2500,
                800,200,
                100,200,
                100,250,
                250,3500,
                };
    }
    
    /***************************** onReceive **********************************/
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent)
    {
        final String msg = intent.getStringExtra(context.getString(
                R.string.broadcast_message_key));
        Log.d(TAG, msg);
        NotificationManager nMgr = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        
        final Intent ViewMessageIntent = new Intent(Intent.ACTION_VIEW);
        ViewMessageIntent.addCategory(Intent.CATEGORY_DEFAULT);
        ViewMessageIntent.setType(context.getString(R.string.custom_mime_type));
        ViewMessageIntent.putExtra(context.getString(R.string.extra_message)
                , msg);
        
        final PendingIntent pi = PendingIntent.getActivity(context, 0,
                ViewMessageIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Message Received")
        .setContentText("View Message")
        .setSmallIcon(R.drawable.alert_icon)
        .setAutoCancel(true).setContentInfo("Demo Notification")
        .setTicker("You received a message!")
        .setPriority(Notification.PRIORITY_MAX)
        .setShowWhen(true)
        .setWhen(System.currentTimeMillis()) // five secs from now
        .setVibrate(VIBRATE_PATTERN);
        
        // checks for availability of method through android versions
        if(android.os.Build.VERSION.SDK_INT >= 
                android.os.Build.VERSION_CODES.JELLY_BEAN)
        {
            builder.setUsesChronometer(true);
        }
        
        builder.setContentIntent(pi);
        nMgr.notify(R.id.notification_id, builder.build());
    }

}
