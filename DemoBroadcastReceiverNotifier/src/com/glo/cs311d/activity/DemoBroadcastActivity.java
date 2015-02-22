/**
 *  Author : Galo Paz
 *    Date : 08/28/2014
 *    Name : DemoBroadcastActivity.java
 *     Tag : CS311D - Assignment 1
 * Purpose : Allows an Android user to broadcast a message using an intent so
 *           that a custom receiver (ie. MessageToLogReceiver) can catch it 
 *           and spit it out to the LogCat.
 */
package com.glo.cs311d.activity;

import com.glo.cs311d.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

/****************** CLASS-ACTIVITY : DemoBroadcastActivity ********************/
public class DemoBroadcastActivity extends Activity
{

    private Button mBroadcastBtn, mClearTextBtn;
    private EditText mMessageEdt;
    private Intent mBroadcastIntent;

    /***************************** onCreate ***********************************/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.demo_broadcast_activity);
        mBroadcastBtn = (Button) findViewById(R.id.broadcast_btn);
        mClearTextBtn = (Button) findViewById(R.id.clear_btn);
        mMessageEdt = (EditText) findViewById(R.id.message_edt);
        setupBroadcastIntent();

        super.onCreate(savedInstanceState);
    }

    /************************** setupBroadcastIntent **************************/
    private void setupBroadcastIntent()
    {
        mBroadcastIntent = new Intent(
                getString(R.string.action_broadcast));
    }

    /***************************** onClick ************************************/
    public void onClick(View view)
    {
        if (view == mBroadcastBtn)
        {
            final String message = ((EditText) mMessageEdt).getText()
                    .toString().trim();
            if (message.isEmpty())
            {
                Toast.makeText(this, "You must enter some text!",
                        Toast.LENGTH_SHORT).show();
            } else {
                mBroadcastIntent.putExtra(
                        getString(R.string.broadcast_message_key), message);
                sendBroadcast(mBroadcastIntent);
            }

        } else if (view == mClearTextBtn) {
            mMessageEdt.setText("");
        } else {
            Log.w(getClass().getSimpleName(), "Click event is not supported "
                    + "for view '" + view.toString() + "'");
        }
    }

}
