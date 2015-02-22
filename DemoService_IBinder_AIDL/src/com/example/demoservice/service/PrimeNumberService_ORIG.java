/**
 *  Author : Galo Paz
 *    Date : 10/22/2014
 *    Name : PrimeNumberService.java
 *     Tag : CS311D - Assignment 4
 * Purpose : A service providing a way for an activity to bind to it so it can
 *           access its function to generate prime numbers between two bounds
 *           (inclusively) not exceeding a certain number provided by the
 *           caller.  Using the ContextWrapper's method 'startService()' will
 *           keep the service alive even if all bounded activities were
 *           unbound.
 */
package com.example.demoservice.service;

import java.util.*;

import android.app.Service;
import android.content.Intent;
import android.os.*;

public class PrimeNumberService_ORIG extends Service
{    
    /**
     * Stores prime numbers already found. 
     */
    private SortedSet<Long> mLongCachedResults;
    
    /**
     * Stores the largest number checked as prime.
     */
    private long mGreatestValueChecked;
    
    /**
     * IBinder for activities to bind to.
     */
    private final IBinder mBinder = new PrimeBinder();

    /********************************** onBind ********************************/
    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }
    
    /********************************** onCreate ******************************/
    @Override
    public void onCreate()
    {
        super.onCreate();
        initialize();
    }
    
    /******************************** initialize ******************************/
    private synchronized void initialize()
    {
        mGreatestValueChecked = 1;
        mLongCachedResults = new TreeSet<Long>();
        mLongCachedResults.add(1L);
    }
    
    /**************************** onTrimMemory ********************************/
    @Override
    public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        switch(level)
        {
        case TRIM_MEMORY_RUNNING_CRITICAL:
            // reset the cached values
            initialize();
        case TRIM_MEMORY_BACKGROUND:
        case TRIM_MEMORY_COMPLETE:
        case TRIM_MEMORY_MODERATE:
        case TRIM_MEMORY_RUNNING_LOW:
        case TRIM_MEMORY_RUNNING_MODERATE:
        case TRIM_MEMORY_UI_HIDDEN:
        default:
            // do nothing
        }
    }

    /**************************** getPrimeNumbers *****************************/
    public long[] getPrimeNumbers(long from, long to, int count)
    throws IllegalArgumentException
    {
     // check if parameters are correctly bound or throw an Exception.
        if(from > to || from < 0 || to < 0 || count < 1)
        {
            throw new IllegalArgumentException(String.format("please check your"
                    + " parameters for correctness: from(%d), to(%d), count(%d)"
                    , from, to, count));
        }
        
        // copy member data into a local copy.
        final long greatestValueChecked;
        final SortedSet<Long> cachedValues;
        synchronized(this)
        {
            greatestValueChecked = mGreatestValueChecked;
            // shallow copy is all that is needed since the Long values are 
            // immutable even if anybody else tries to change their values.
            cachedValues = mLongCachedResults;
        }
        
        // set aside a return variable, to be set only once
        final long[] values;
        
        // check if we already have the prime numbers stored, and just copy
        // and return requested array.
        if(to <= greatestValueChecked)
        {
           SortedSet<Long> sub =  cachedValues.subSet(from, to+1);
           values = new long[Math.min(sub.size(), count)];
           int i = 0;
           for(Long lng : sub)
           {
               values[i++] = lng;
               if(i == values.length)
                   break;
           }
        }
        else
        {
            // to store the starting number to check from so that we are not
            // redundantly checking values we already have a result for.
            final long start;
            // gather the prime numbers that we already have, otherwise we will
            // start with an empty set to populate with the return values.
            if(from <= greatestValueChecked)
            {
                start = greatestValueChecked+1;
            }
            else
            {
                start = from;
            }
            // populate the prime number list (cachedValues) from 'start' to
            // 'to', limited by the 'count' of prime numbers found between those
            // two values.  It will return the last value checked before
            // returning.
            final long newLastValueChecked = populatePrimeList(cachedValues,
                    greatestValueChecked, start, to, count);
            
            // obtain the subset from the cached values set and create the array
            // with those values, limited by the count.
            final SortedSet<Long> finalSubSet = cachedValues.subSet(from, to+1);
            values = new long[Math.min(count, finalSubSet.size())];
            int i = 0;
            for(long lng : finalSubSet)
            {
                values[i++] = lng;
                if(i == values.length) break;
            }
            
            // synchronize the member variables with the new set if it is larger
            synchronized(this)
            {
                if(mGreatestValueChecked < newLastValueChecked)
                {
                    mGreatestValueChecked = newLastValueChecked;
                    mLongCachedResults = cachedValues;
                }
            }
        }
        
        
        return values;
    }
    
    /*************************** populatePrimeList ****************************/
    /**
     * Uses the 'cachedSet' of prime numbers to find other prime numbers and to
     * add newly found prime numbers.  Returns the last value checked.
     */
    private long populatePrimeList(SortedSet<Long> cachedSet,
            long lastValueChecked, long from, long to, int maxCount)
    {
        // keeps track of the prime numbers found within the specified 'from-to'
        // bounds to exit once we reach 'maxCount' numbers.
        int primeCounter = 0;
        
        // starting point
        long start;
        // prepare cached values and starting point
        if(from <= lastValueChecked)
        {
            start = lastValueChecked+1;
            primeCounter += cachedSet.subSet(from, start).size();
        }
        else 
        {
            for(long i = lastValueChecked+1; i < from; i++)
            {
                lastValueChecked = i;
                storeIfPrime(cachedSet, i);
            }
            start = from;
        }
        
        if(primeCounter < maxCount)
        {
            if(start <= to)
            {
                for(long i = start; i <= to; i++)
                {
                    lastValueChecked = i;
                    if(storeIfPrime(cachedSet, i))
                    {
                        if(++primeCounter >= maxCount)
                            break;
                    }
                }
            }
            else
            {
                // do nothing
            }
        }
        
        return lastValueChecked;
    }
    
    /******************************** storeIfPrime ****************************/
    private boolean storeIfPrime(SortedSet<Long> cached, long value)
    {
        if(checkPrimeUsingCache(cached, value))
        {
            cached.add(value);
            return true;
        }
        return false;
    }
    
    /*************************** checkPrimeUsingCache *************************/
    private boolean checkPrimeUsingCache(SortedSet<Long> cachedPrimes, long num)
    {
        
        for(long val : cachedPrimes.subSet(2L, (num/2)  + 1))
        {
            if(num % val == 0)
                return false;
        }
        
        return true;
    }
    
    /***************************** onStartCommand *****************************/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        switch(startId)
        {
        case 12345:
            final String strCode = intent.getStringExtra("secret_code");
            if("kill".equals(strCode))
                stopSelf();
            break;    
        default:
        }
        
        return START_STICKY;
    }
    
    /*************************** CLASS: PrimeBinder ***************************/
    public class PrimeBinder extends Binder
    implements ServiceProvider_ORIG<PrimeNumberService_ORIG>
    {
        /************************** getService ********************************/
        @Override
        public PrimeNumberService_ORIG getService()
        {
            return PrimeNumberService_ORIG.this;
        }
    }
    
}