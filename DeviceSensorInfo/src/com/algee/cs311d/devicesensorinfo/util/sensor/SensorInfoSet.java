/**
 *  Author : Galo Paz
 *    Date : 11/12/2014
 *    Name : SensorInfoSet.java
 *     Tag : CS311D - Extra Credit #1
 * Purpose : Holds parcelable sensor information.
 */
package com.algee.cs311d.devicesensorinfo.util.sensor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/************************** CLASS : SensorInfoSet *****************************/
public class SensorInfoSet implements Parcelable
{
    private List<SensorInfo> mSensorInfoList;
    private int mSensorType;
    private String mSensorTypeString = "";
    private String mPackageInfoString = "";
    
    /****************************** CONSTRUCTOR *******************************/
    public SensorInfoSet(List<SensorInfo> sensorList, int sensorType
            , String sensorTypeString)
    {
        mSensorInfoList = sensorList;
        mSensorType = sensorType;
        mSensorTypeString = sensorTypeString;
    }
    
    /***************************** SensorInfoSet ******************************/
    public SensorInfoSet()
    {
        mSensorInfoList = Collections.<SensorInfo>emptyList();
    }
    
    /***************************** getSensorInfoList **************************/
    public List<SensorInfo> getSensorInfoList()
    {
        return mSensorInfoList;
    }
    
    /*************************** setSensorInfoList ****************************/
    public void setSensorInfoList(List<SensorInfo> sil)
    {
        mSensorInfoList = sil;
    }
    
    /****************************** getSensorType *****************************/
    public int getSensorType()
    {
        return mSensorType;
    }
    
    /****************************** setSensorType *****************************/
    public void setSensorType(int type)
    {
        mSensorType = type;
    }
    
    /*************************** getSensorTypeString **************************/
    public String getSensorTypeString()
    {
        return mSensorTypeString;
    }
    
    /*************************** setSensorTypeString **************************/
    public void setSensorTypeString(String typeStr)
    {
        mSensorTypeString = typeStr;
    }
    
    /*************************** getPackageInfoString *************************/
    public String getPackageInfoString()
    {
        return mPackageInfoString;
    }
    
    /************************** setPackageInfoString **************************/
    public void setPackageInfoString(String packageInfo)
    {
        mPackageInfoString = packageInfo;
    }
    
    /******************************** toString ********************************/
    @Override
    public String toString()
    {
        return getSensorTypeString();
    }

    /**************************** describeContents ****************************/
    @Override
    public int describeContents()
    {
        return 0;
    }

    /****************************** writeToParcel *****************************/
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeList(mSensorInfoList);
        dest.writeInt(mSensorType);
        dest.writeString(mSensorTypeString);
        dest.writeString(mPackageInfoString);
    }
    
    /************************ CONSTRUCTOR : Parcelable ************************/
    private SensorInfoSet(Parcel in)
    {
        final List<SensorInfo> list = new LinkedList<SensorInfo>();
        in.readList(list, ClassLoader.getSystemClassLoader());
        mSensorInfoList = list;
        mSensorType = in.readInt();
        mSensorTypeString = in.readString();
        mPackageInfoString = in.readString();
    }

    /************************** Parcelable.CREATOR ****************************/
    public static final Parcelable.Creator<SensorInfoSet> CREATOR =
            new Parcelable.Creator<SensorInfoSet>()
    {

        /*********************** createFromParcel *************************/
        @Override
        public SensorInfoSet createFromParcel(Parcel source)
        {
            return new SensorInfoSet(source);
        }

        /*************************** newArray *****************************/
        @Override
        public SensorInfoSet[] newArray(int size)
        {
            return new SensorInfoSet[size];
        }
        
    };

    /***************************** isAvailable ********************************/
    public boolean isAvailable()
    {
        return mSensorInfoList.size() > 0;
    }
    
}
