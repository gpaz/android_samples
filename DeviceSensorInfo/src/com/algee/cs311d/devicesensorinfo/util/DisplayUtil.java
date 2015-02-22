/**
 *  Author : Galo Paz
 *    Date : 11/12/2014
 *    Name : DisplayUtil.java
 *     Tag : CS311D - Extra Credit #1
 * Purpose : 
 */
package com.algee.cs311d.devicesensorinfo.util;

import android.util.DisplayMetrics;

/*************************** CLASS : DisplayUtil ******************************/
public strictfp final class DisplayUtil
{
    public static final float FACTOR_INCH_TO_MM = 24.400f;
    public static final float FACTOR_INCH_TO_CM = FACTOR_INCH_TO_MM * 10;
    
    /**************************** CONSTRUCTOR *********************************/
    private DisplayUtil(){}
    
    /***************************** getWidthInches *****************************/
    public static final float getWidthInches(DisplayMetrics metrics)
    {
        return getSizeInches(metrics.widthPixels, metrics.xdpi);
    }
    
    /***************************** getHeightInches ****************************/
    public static final float getHeightInches(DisplayMetrics metrics)
    {
        return getSizeInches(metrics.heightPixels, metrics.ydpi);
    }
    
    /***************************** getSizeInches ******************************/
    public static final float getSizeInches(int sizePixels, float sizeDpi)
    {
        return sizePixels / sizeDpi;
    }

    /************************** getWidthMillimeters ***************************/
    public static final float getWidthMillimeters(DisplayMetrics metrics)
    {
        return getWidthInches(metrics) * FACTOR_INCH_TO_MM;
    }
    
    /************************** getHeightMillimeters **************************/
    public static final float getHeightMillimeters(DisplayMetrics metrics)
    {
        return getHeightInches(metrics) * FACTOR_INCH_TO_MM;
    }
    
    /************************** getWidthCentimeters ***************************/
    public static final float getWidthCentimeters(DisplayMetrics metrics)
    {
        return getWidthInches(metrics) * FACTOR_INCH_TO_CM;
    }
    
    /************************** getHeightCentimeters **************************/
    public static final float getHeightCentimeters(DisplayMetrics metrics)
    {
        return getHeightInches(metrics) * FACTOR_INCH_TO_CM;
    }

    /******************* CLASS : DisplayMetricsInterpreter ********************/
    public strictfp static final class DisplayMetricsInterpreter
    {
        private DisplayMetrics mMetrics;
        /************************** CONSTRUCTOR ***************************/
        public DisplayMetricsInterpreter(DisplayMetrics metrics)
        {
            if(metrics == null)
                throw new NullPointerException("Instance cannot be "
                        + "instantiated with a null metrics object.");
            mMetrics = metrics;
        }
        
        /*************************** getWidthIN ***************************/
        public float getWidthInches()
        {
            return DisplayUtil.getWidthInches(mMetrics);
        }
        
        /************************ getHeightInches *************************/
        public float getHeightInches()
        {
            return DisplayUtil.getHeightInches(mMetrics);
        }
        
        /********************** getWidthMillimeters ***********************/
        public float getWidthMillimeters()
        {
            return DisplayUtil.getWidthMillimeters(mMetrics);
        }
        
        /********************* getHeightMillimeters ***********************/
        public float getHeightMillimeters()
        {
            return DisplayUtil.getHeightMillimeters(mMetrics);
        }
        
        /********************** getWidthCentimeters ***********************/
        public float getWidthCentimeters()
        {
            return DisplayUtil.getWidthCentimeters(mMetrics);
        }
        
        /********************** getHeightCentimeters***********************/
        public float getHeightCentimeters()
        {
            return DisplayUtil.getHeightCentimeters(mMetrics);
        }
        
    }
    
    /************************** ENUMERATION : Size ****************************/
    public enum Size
    {
        SMALL,
        NORMAL,
        LARGE,
        XLARGE,
        XXLARGE
    }
    
}
