/**
 *  Author : Galo Paz
 *    Date : 11/12/2014
 *    Name : SensorInfo.java
 *     Tag : CS311D - Extra Credit #1
 * Purpose : Provide a parcelable entity with which to store information about a
 *           sensor with the intent of passing through Android Bundles, whether
 *           between Activities or between onSavedInstanceState and onCreate.
 */
package com.algee.cs311d.devicesensorinfo.util.sensor;

import java.util.ArrayList;
import java.util.List;

import android.hardware.Sensor;
import android.os.Parcel;
import android.os.Parcelable;

/*************************** CLASS : SensorInfo *******************************/
public class SensorInfo implements Parcelable
{
    private int mFifoMaxEventCount,
                mFifoReservedEventCount,
                mMinDelay,
                mIntType,
                mVersion;

    private float mMaximumRange,
                  mPower,
                  mResolution;
    
    private String mSensorClassName,
                   mName,
                   mVendor;

    /****************************** CONSTRUCTOR *******************************/
    public SensorInfo(Sensor s)
    {
        if(s == null)
            throw new NullPointerException("The sensor parameter cannot be "
                    + "instantiated with a null object.");
        mSensorClassName = s.getClass().getName();
        mFifoMaxEventCount = s.getFifoMaxEventCount();
        mFifoReservedEventCount = s.getFifoReservedEventCount();
        mMaximumRange = s.getMaximumRange();
        mMinDelay = s.getMinDelay();
        mName = s.getName();
        mPower = s.getPower();
        mResolution = s.getResolution();
        // DO NOT USE THIS METHOD, THROWS NoSuchMethodException AND CANNOT
        // CATCH JNI-CALL ERROR, THUS APP CRASH.(API 19)
        //mStringType = s.getStringType();
        mIntType = s.getType();
        mVendor = s.getVendor();
        mVersion = s.getVersion();
    }

    /************************** getSensorClassName ****************************/
    public String getSensorClassName()
    {
        return mSensorClassName;
    }

    /************************** getFifoMaxEventCount **************************/
    public int getFifoMaxEventCount()
    {
        return mFifoMaxEventCount;
    }

    /*********************** getFifoReservedEventCount ************************/
    public int getFifoReservedEventCount()
    {
        return mFifoReservedEventCount;
    }

    /****************************** getIntType ********************************/
    public int getIntType()
    {
        return mIntType;
    }

    /**************************** getMaximumRange *****************************/
    public float getMaximumRange()
    {
        return mMaximumRange;
    }

    /****************************** getMinDelay *******************************/
    public int getMinDelay()
    {
        return mMinDelay;
    }

    /******************************* getName **********************************/
    public String getName()
    {
        return mName;
    }

    /******************************* getPower *********************************/
    public float getPower()
    {
        return mPower;
    }

    /***************************** getResolution ******************************/
    public float getResolution()
    {
        return mResolution;
    }
    
    /****************************** getVendor *********************************/
    public String getVendor()
    {
        return mVendor;
    }

    /******************************* getVersion *******************************/
    public int getVersion()
    {
        return mVersion;
    }

    /**************************** describeContents  ***************************/
    @Override public int describeContents() { return 0; }

    /***************************** writeToParcel ******************************/
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(mFifoMaxEventCount);
        dest.writeInt(mFifoReservedEventCount);
        dest.writeInt(mIntType);
        dest.writeFloat(mMaximumRange);
        dest.writeInt(mMinDelay);
        dest.writeString(mName);
        dest.writeFloat(mPower);
        dest.writeFloat(mResolution);
        dest.writeString(mSensorClassName);
        //        dest.writeString(mStringType);
        dest.writeString(mVendor);
        dest.writeInt(mVersion);
    }

    /*********************** CONSTRUCTOR : PARCELABLE *************************/
    private SensorInfo(Parcel in)
    {
        mFifoMaxEventCount = in.readInt();
        mFifoReservedEventCount = in.readInt();
        mIntType = in.readInt();
        mMaximumRange = in.readFloat();
        mMinDelay = in.readInt();
        mName = in.readString();
        mPower = in.readFloat();
        mResolution = in.readFloat();
        mSensorClassName = in.readString();
        //        mStringType = in.readString();
        mVendor = in.readString();
        mVersion = in.readInt();
    }

    /*********************** STATIC PARCELABLE CREATOR ************************/
    public static final Parcelable.Creator<SensorInfo> CREATOR
    = new Parcelable.Creator<SensorInfo>()
    {
        /************************ createFromParcel ************************/
        public SensorInfo createFromParcel(Parcel source) 
        {
            return new SensorInfo(source);
        };

        /*************************** new Array ****************************/
        public SensorInfo[] newArray(int size)
        {
            return new SensorInfo[size];
        }
    };

    /***************************** FACTORY METHOD *****************************/
    /**
     * Creates a List of SensorInfo objects from a List of Android Sensor
     * objects.
     * @param list
     * @return
     */
    public static List<SensorInfo> createFromSensorList(List<Sensor> list)
    {
        final int size = list.size();
        final List<SensorInfo> retList = new ArrayList<SensorInfo>(size);
        for(Sensor s : list)
        {
            retList.add(new SensorInfo(s));
        }

        return retList;
    }

}
