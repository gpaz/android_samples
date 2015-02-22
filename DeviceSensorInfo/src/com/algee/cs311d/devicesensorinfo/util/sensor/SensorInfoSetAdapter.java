/**
 *  Author : Galo Paz
 *    Date : 11/12/2014
 *    Name : SensorInfoSetAdapter.java
 *     Tag : CS311D - Extra Credit #1
 * Purpose : Adapter for showing SensorInfoSet data in a listview.
 */
package com.algee.cs311d.devicesensorinfo.util.sensor;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/*********************** CLASS : SensorInfoSetAdapter *************************/
public class SensorInfoSetAdapter extends ArrayAdapter<SensorInfoSet>
{
    /**************************** CONSTRUCTOR ********************************/
    public SensorInfoSetAdapter(Context context, int resource,
            List<SensorInfoSet> objects)
    {
        super(context, resource, objects);
    }

    /****************************** getView  *********************************/
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = super.getView(position, convertView, parent);
        SensorInfoSet item = getItem(position);
        convertView.setEnabled(item.isAvailable());
        return convertView;
    }

}