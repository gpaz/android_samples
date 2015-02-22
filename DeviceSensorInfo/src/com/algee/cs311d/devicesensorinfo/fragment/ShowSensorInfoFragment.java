/**
 *  Author : Galo Paz
 *    Date : 11/12/2014
 *    Name : ShowSensorInfoFragment.java
 *     Tag : CS311D - Extra Credit #1
 * Purpose : Provide a reusable entity with which to display the information of
 *           a list of incoming Sensors.
 */
package com.algee.cs311d.devicesensorinfo.fragment;

import java.util.List;

import com.algee.cs311d.devicesensorinfo.R;
import com.algee.cs311d.devicesensorinfo.util.sensor.SensorInfo;
import com.algee.cs311d.devicesensorinfo.util.sensor.SensorInfoSet;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/*********************** CLASS : ShowSensorInfoFragment ***********************/
public class ShowSensorInfoFragment extends Fragment
{
    private SensorInfoSet mSensorInfoSet;
    private CharSequence mOriginalActivityTitle = null;
    
    /******************************* CONSTRUCTOR ******************************/
    public ShowSensorInfoFragment(){}
    
    public ShowSensorInfoFragment(SensorInfoSet sis)
    {
        mSensorInfoSet = sis;
    }
    
    /******************************* CONSTRUCTOR ******************************/
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getActivity().getResources().getString(
                R.string.key_sensor_picked), mSensorInfoSet);
    }
    
    /********************************* onCreate *******************************/
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null)
        {
            final SensorInfoSet infoSet =
                    savedInstanceState.<SensorInfoSet>getParcelable(
                    getActivity().getString(R.string.key_sensor_picked));
            if(infoSet != null)
                mSensorInfoSet = infoSet;
        }
    }
    
    /******************************** onCreateView ****************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        final View v = inflater.inflate(R.layout.fragment_show_sensor_info
                , container, false);
        ViewGroup sensorInfoContainer = (ViewGroup)v.findViewById(
                R.id.sensor_list);
        if(mSensorInfoSet == null)
        {
            if(mOriginalActivityTitle != null)
                getActivity().setTitle(mOriginalActivityTitle);
            // just in case immediate does not work.
            if(!getFragmentManager().popBackStackImmediate())
                getFragmentManager().popBackStack(); 
            return null;
        }
        mOriginalActivityTitle = getActivity().getTitle(); // hold a reference
        getActivity().setTitle(mSensorInfoSet.getSensorTypeString());
        final List<SensorInfo> sensorList = mSensorInfoSet.getSensorInfoList();
        for(int i = 0; i < sensorList.size(); i++)
        {
            final SensorInfo si = sensorList.get(i);
            final View group = inflater.inflate(R.layout.sensor_info_layout, sensorInfoContainer, false);
            if(group.getParent() != sensorInfoContainer)
                sensorInfoContainer.addView(group);
            ((TextView)group.findViewById(R.id.sensor_number))
                .setText("("+1+")");
            ((TextView)group.findViewById(R.id.sensor_class_name))
                .setText("Class: " + si.getSensorClassName());
            ((TextView)group.findViewById(R.id.sensor_fifo_max_event_count))
                .setText("Max Event Count: "+si.getFifoMaxEventCount());
            ((TextView)group.findViewById(R.id.sensor_fifo_reserved_event_count))
                .setText("Reserved Event Count: "+si.getFifoReservedEventCount());
            ((TextView)group.findViewById(R.id.sensor_maximum_range))
                .setText("Maximum Range: "+si.getMaximumRange());
            ((TextView)group.findViewById(R.id.sensor_minimum_delay))
                .setText("Minimum Delay: "+si.getMinDelay() + " microseconds");
            ((TextView)group.findViewById(R.id.sensor_name))
                .setText("Name: " + si.getName());
            ((TextView)group.findViewById(R.id.sensor_power))
                .setText("Power: "+si.getPower() + "mA");
            ((TextView)group.findViewById(R.id.sensor_resolution))
                .setText("Resolution: "+si.getResolution());
            ((TextView)group.findViewById(R.id.sensor_int_type))
                .setText("Sensor Type (Integer): "+si.getIntType());
            ((TextView)group.findViewById(R.id.sensor_vendor))
                .setText("Vendor: " + si.getVendor());
            ((TextView)group.findViewById(R.id.sensor_version))
                .setText("Version: "+si.getVersion());
            if((i+1) < sensorList.size())
                inflater.inflate(R.layout.horizontal_splitter
                        , sensorInfoContainer, false);
        }

        return v;
    }
    
    /********************************* onDetach *******************************/
    @Override
    public void onDetach()
    {
        if(mOriginalActivityTitle != null)
            getActivity().setTitle(mOriginalActivityTitle);
        super.onDetach();
    } 
}
