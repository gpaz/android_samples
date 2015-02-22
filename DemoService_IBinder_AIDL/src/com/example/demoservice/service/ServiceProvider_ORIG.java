/**
 *  Author : Galo Paz
 *    Date : 10/22/2014
 *    Name : ServiceProvider.java
 *     Tag : CS311D - Assignment 4
 * Purpose : Provides the method to get a reference to a service.
 */
package com.example.demoservice.service;

import android.app.Service;

/*********************** INTERFACE: ServiceProvider ***************************/
public interface ServiceProvider_ORIG<T extends Service>
{
    /******************************* getService *******************************/
    /**
     * Returns a bound service or null if not bound.
     * @return
     */
    public T getService();
    
} // end ServiceProvider
