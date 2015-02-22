/**
 *  Author : Galo Paz
 *    Date : 11/12/2014
 *    Name : Range.java
 * Purpose : Provide a simple one-dimensional way to express a range of values.
 *           It also provides the ability to express not only equality and 
 *           inequality between two ranges, but also the ability to check
 *           whether a value is within the range or how a range compares to
 *           another range, ie: less-than, greater-than, partially-equal, etc.
 *           Additionally, it provides the ability to compare ranges based on
 *           an explicit error, whereby to check equality of a range can depend
 *           on the difference between two range end-points being within this
 *           provided in-spec-error value.
 */
package com.algee.cs311d.devicesensorinfo.util.math;

public strictfp final class Range
{
    public static final int IN_RANGE = 0 ,
                            OUTOF_RANGE_BELOW = 1 ,
                            OUTOF_RANGE_ABOVE = 2 ,
                            PARTIALLY_OUTOF_RANGE_BELOW = 3 ,
                            PARTIALLY_OUTOF_RANGE_ABOVE = 4 ,
                            FULLY_ENCAPSULATED_BY_RANGE = 5 ,
                            EQUAL_WITHIN_SPEC = 6;
    
    private float mMin, mMax;
    
    public Range(float min, float max)
    {
        if(min > max)
            throw new IllegalArgumentException(String.format(
                    "min(%f) must be less than max(%f), %1$f > %2$f -- "
                    , min, max));
        mMin = min;
        mMax = max;
    }
    
    public float getMin()
    {
        return mMin; 
    }
    
    public float getMax()
    {
        return mMax;
    }
    
    public boolean inRange(float val)
    {
        return mMin <= val && val <= mMax;
    }
    
    public boolean inRange(Range r)
    {
        if(r == null)
            throw new NullPointerException("Range cannot be null.");
        return mMin <= r.mMin && r.mMax <= mMax;
    }
    
    public boolean outOfRange(float val)
    {
        return !inRange(val);
    }
    
    public boolean outOfRange(Range r)
    {
        return !inRange(r);
    }
    
    public int compare(Range r)
    {
        final int nRetRange;
        if(mMin <= r.mMin)
        {
            if(mMax >= r.mMax)
                nRetRange = IN_RANGE;
            else if(mMax < r.mMax)
                nRetRange = PARTIALLY_OUTOF_RANGE_ABOVE;
            else
                nRetRange = OUTOF_RANGE_ABOVE;
        }
        else
        {
            if(mMin <= r.mMax)
                nRetRange = PARTIALLY_OUTOF_RANGE_BELOW;
            else if(mMin > r.mMax)
                nRetRange = OUTOF_RANGE_BELOW;
            else
                nRetRange = FULLY_ENCAPSULATED_BY_RANGE;
        }
        
        return nRetRange;
    }
    
    public int compare(Range r, float equalityDeltaSpec)
    {
        // get a base comparison
        int nRetRange = compare(r);
        // check bounds by approximation
        switch(nRetRange)
        {
        case FULLY_ENCAPSULATED_BY_RANGE:
        case IN_RANGE:
            if(isApproximate(mMin, r.mMin, equalityDeltaSpec)
                    && isApproximate(mMax, r.mMax, equalityDeltaSpec))
            {
                nRetRange = EQUAL_WITHIN_SPEC;
            } else {/*keep original return value*/}
            break;
        case OUTOF_RANGE_ABOVE:
            if(isApproximate(mMax, r.mMin, equalityDeltaSpec))
            {
                if(isApproximate(mMax, r.mMax, equalityDeltaSpec))
                {
                    if(isApproximate(mMin, r.mMin, equalityDeltaSpec))
                        nRetRange = EQUAL_WITHIN_SPEC;
                    else
                        nRetRange = IN_RANGE;
                }
                else
                    nRetRange = PARTIALLY_OUTOF_RANGE_ABOVE;
            } else {/*keep original return value*/}
        case OUTOF_RANGE_BELOW:
            if(isApproximate(mMin, r.mMax, equalityDeltaSpec))
            {
                if(isApproximate(mMin, r.mMin, equalityDeltaSpec))
                {
                    if(isApproximate(mMax, r.mMax, equalityDeltaSpec))
                        nRetRange = EQUAL_WITHIN_SPEC;
                    else
                        nRetRange = IN_RANGE;
                }
                else
                    nRetRange = PARTIALLY_OUTOF_RANGE_BELOW;
            } else {/*Keep Current Return Value*/}
        case PARTIALLY_OUTOF_RANGE_ABOVE:// TODO
            if(isApproximate(mMin, r.mMin, equalityDeltaSpec))
            {
                if(isApproximate(mMax, r.mMax, equalityDeltaSpec))
                {
                    nRetRange = EQUAL_WITHIN_SPEC;
                } else {/*keep original return value*/};
            }
            else
            {
                if(isApproximate(mMax, r.mMax, equalityDeltaSpec))
                {
                    nRetRange = IN_RANGE;
                } else {/*keep original return value*/};
            }
        case PARTIALLY_OUTOF_RANGE_BELOW:// TODO
            if(isApproximate(mMax, r.mMax, equalityDeltaSpec))
            {
                if(isApproximate(mMin, r.mMin, equalityDeltaSpec))
                {
                    nRetRange = EQUAL_WITHIN_SPEC;
                } else {/*keep original return value*/};
            }
            else
            {
                if(isApproximate(mMin, r.mMin, equalityDeltaSpec))
                {
                    nRetRange = IN_RANGE;
                } else {/*keep original return value*/};
            }
        }
        
        return nRetRange;
    }
    
    private boolean isApproximate(float val1, float val2, float error)
    {
        return Math.abs(val1 - val2) <= error;
    }
    
}
