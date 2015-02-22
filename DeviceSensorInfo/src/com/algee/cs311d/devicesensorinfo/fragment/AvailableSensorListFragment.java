/**
 *  Author : Galo Paz
 *    Date : 11/12/2014
 *    Name : AvailableSensorListFragment.java
 *     Tag : CS311D - Extra Credit #1
 * Purpose : 
 */
package com.algee.cs311d.devicesensorinfo.fragment;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.algee.cs311d.devicesensorinfo.R;
import com.algee.cs311d.devicesensorinfo.util.StringUtil;
import com.algee.cs311d.devicesensorinfo.util.sensor.SensorInfo;
import com.algee.cs311d.devicesensorinfo.util.sensor.SensorInfoSet;
import com.algee.cs311d.devicesensorinfo.util.sensor.SensorInfoSetAdapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/********************** CLASS : AvailableSensorListFragment *******************/
public class AvailableSensorListFragment extends ListFragment
{
    List<SensorInfoSet> mSensorList;

    /********************************* onCreate *******************************/
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final Activity activity = getActivity();
        mSensorList = createSensorSupportList(activity);
        setListAdapter(new SensorInfoSetAdapter(activity
                , android.R.layout.simple_list_item_1, mSensorList));
    }
    
    /***************************** onListItemClick ****************************/
    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        final SensorInfoSet sis = (SensorInfoSet)l.getItemAtPosition(position);
        if(sis.isAvailable())
        {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.addToBackStack("sensor-selector");
            ft.replace(R.id.frag_space_1, new ShowSensorInfoFragment(sis));
            ft.commit();
        }
    }
    
    /************************* createSensorSupportList ************************/
    private List<SensorInfoSet> createSensorSupportList(Activity activity)
    {
        final SensorManager sensorManager = (SensorManager)activity
                .getSystemService(Context.SENSOR_SERVICE);
        List<SensorInfoSet> retSensorInfos = null;
        try
        {
            retSensorInfos = createSensorListThroughReflection(sensorManager);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return retSensorInfos;
    }

    /******************* createSensorListThroughReflection ********************/
    private List<SensorInfoSet> createSensorListThroughReflection(SensorManager sm)
    throws IllegalAccessException, IllegalArgumentException
    { 
        Map<String, SensorInfoSet> mappedSet = new TreeMap<String, SensorInfoSet>();
        final List<SensorInfoSet> retSensorInfoSetList = new LinkedList<SensorInfoSet>();
        Field[] fields = Sensor.class.getFields();
        Field[] dfields = Sensor.class.getDeclaredFields();
        for(Field f : (fields.length > dfields.length ? fields : dfields))
        {
            if((f.getModifiers() & Modifier.STATIC) > 0)
            {
                final String fieldName = f.getName();
                if(fieldName.startsWith("TYPE_") && !f.getName().equals("TYPE_ALL"))
                {
                    final int type = f.getInt(null);
                    final List<Sensor> sl = sm.getSensorList(type);
                    final List<SensorInfo> sil = SensorInfo.createFromSensorList(sl);
                    
                    final String key = convertToPrettyString(
                            fieldName.substring(fieldName.indexOf('_')+1));
                    final SensorInfoSet mappedSIS = mappedSet.get(key);
                    if(mappedSIS != null)
                    {
                        mappedSIS.setSensorInfoList(sil);
                        mappedSIS.setSensorType(type);
                        mappedSIS.setSensorTypeString(key);
                    }
                    else
                    {
                        final SensorInfoSet newSIS =
                                new SensorInfoSet(sil, type, key);
                        mappedSet.put(key, newSIS);
                    }
                }
                else if(fieldName.startsWith("STRING_TYPE_"))
                {
                    // set package info string for api level 20 and above.
                    final String packageInforStr = (String)f.get(null);
                    
                    final String key = convertToPrettyString(
                            fieldName.substring(fieldName.indexOf('_', 7)+1));
                    final SensorInfoSet mappedSIS = mappedSet.get(key);
                    if(mappedSIS != null)
                    {
                        mappedSIS.setPackageInfoString(packageInforStr);
                    }
                    else
                    {
                        final SensorInfoSet newSIS = new SensorInfoSet();
                        newSIS.setPackageInfoString(packageInforStr);
                        mappedSet.put(key, newSIS);
                    }
                }
            }
        }
        
        for(Entry<String, SensorInfoSet> entry: mappedSet.entrySet())
        {
            retSensorInfoSetList.add(entry.getValue());
        }
        
        return retSensorInfoSetList;
    }
    
    /*************************** convertToPrettyString ************************/
    private static String convertToPrettyString(String typeStr)
    {
        typeStr = typeStr.replace('_', ' ');
        typeStr = StringUtil.capitalizeEachWord(typeStr.toLowerCase());
        return typeStr;
    }

    /***************************** onCreateView *******************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}