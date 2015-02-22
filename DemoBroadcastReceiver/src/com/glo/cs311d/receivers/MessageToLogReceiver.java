/**
 *  Author : Galo Paz
 *    Date : 08/28/2014
 *    Name : MessageToLogReceiver.java
 *     Tag : CS311D - Assignment 1
 * Purpose : Receives a broadcast and prints the incoming data message string to
 *           the LogCat with the tag "MessageReceived".
 */
package com.glo.cs311d.receivers;

import com.glo.cs311d.R;

import android.content.*;
import android.util.Log;
import android.widget.Toast;

/************************ CLASS : MessageToLogReceiver ***********************/
public class MessageToLogReceiver extends BroadcastReceiver
{
    private static final String TAG = "MessageReceived";

    /***************************** onReceive **********************************/
    @Override
    public void onReceive(Context context, Intent intent)
    {
        final String msg = intent.getStringExtra(context
                .getString(R.string.broadcast_message_key));
        Log.d(TAG, msg);
        Toast.makeText(context, "Message Received:\n" + (msg==null?"NULL":msg)
                , Toast.LENGTH_LONG).show();
    }

}
