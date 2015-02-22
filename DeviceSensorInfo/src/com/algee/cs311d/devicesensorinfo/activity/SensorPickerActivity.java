/**
 *  Author : Galo Paz
 *    Date : 11/12/2014
 *    Name : SensorPickerActivity.java
 *     Tag : CS311D - Extra Credit #1
 * Purpose : Provide a parcelable entity that will store information about a
 *           sensor with the intent of passing through Android Bundles, whether
 *           between Activities or between onSavedInstanceState and onCreate.
 */
package com.algee.cs311d.devicesensorinfo.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.algee.cs311d.devicesensorinfo.R;
import com.algee.cs311d.devicesensorinfo.fragment.AvailableSensorListFragment;

/************************* CLASS : SensorPickerActivity ***********************/
public class SensorPickerActivity extends Activity
{
    /******************************* onCreate *********************************/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_picker);
        FragmentManager manager = getFragmentManager();
        if(manager.findFragmentByTag("SensorList") == null)
        {
            FragmentTransaction transaction1 = manager.beginTransaction();
            transaction1.replace(R.id.frag_space_1
                    , new AvailableSensorListFragment(), "SensorList");
            transaction1.commit();
        }
    }
    
    /************************** onCreatesOptionsMenu **************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.sensor_picker, menu);
        return true;
    }
    
    /************************** onOptionsItemSelected *************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
}