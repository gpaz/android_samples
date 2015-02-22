/**
 *  Author : Galo Paz
 *    Date : 09/06/2014
 *    Name : DisplayMessageActivity.java
 *     Tag : CS311D - Assignment 1.2
 * Purpose : This Activity is started up whenever a user receives a notification
 *           in the status bar and clicks on it, which will then show the full
 *           message that was received issued from the DemoBroadcastActivity.
 */
package com.glo.cs311d.activity;

import com.glo.cs311d.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/********************** CLASS : DisplayMessageActivity ************************/
public class DisplayMessageActivity extends Activity
{
    TextView mMessageTv;

    /****************************** OnCreate **********************************/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.display_message_activity);
        mMessageTv = (TextView) findViewById(R.id.message_tv);

        final Intent i = getIntent();
        if (i != null)
        {
            mMessageTv.setText(i
                    .getStringExtra(getString(R.string.extra_message)));
        } else
        {
            mMessageTv.setText(R.string.no_message);
        }
    }
}
