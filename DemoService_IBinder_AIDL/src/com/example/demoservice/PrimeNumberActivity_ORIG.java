/**
 *  Author : Galo Paz
 *    Date : 10/22/2014
 *    Name : PrimeNumberActivity.java
 *     Tag : CS311D - Assignment 4
 * Purpose : Provide a User Interface that allows the generation and display of
 *           all prime numbers between two bounds, whose count does not exceed
 *           a provided amount.  If the provided maximum amount field is left
 *           not populated, it will attempt to retrieve all values between the
 *           specified bounds not exceeding the Integer.MAX_VALUE value.
 */
package com.example.demoservice;

import java.lang.ref.WeakReference;
import java.util.*;

import com.example.demoservice.service.*;
import com.example.demoservice.service.PrimeNumberService.PrimeBinder;

import android.app.Activity;
import android.content.*;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

/************************** CLASS: PrimeNumberActivity ************************/
public class PrimeNumberActivity_ORIG extends Activity implements OnClickListener
{

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        
    }
//    private static final String TAG = PrimeNumberActivity_ORIG.class.getSimpleName();
//    private static final String KEY_PRIME_NUMBERS = "prime-numbers";
//    
//    private PrimeNumberService mBoundService;
//    private TextView mFrom, mTo, mCount, mPrimeNumbers;
//    private Button mActionButton;
//    
//    /**
//     * Member SeviceConnection object and inner anonymous class.
//     */
//    private ServiceConnection mServiceConnection = new ServiceConnection()
//    {
//        /************************* onServiceConnected *************************/
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service)
//        {
//            mBoundService = ((PrimeBinder)service).getService();
//            Log.i(TAG, "Service bound");
//            Toast.makeText(getApplicationContext(), "Bound to Service", 
//                    Toast.LENGTH_SHORT).show();
//        }
//
//        /************************* onServiceDisconnected **********************/
//        @Override
//        public void onServiceDisconnected(ComponentName name)
//        {
//            mBoundService = null;
//            Log.i(TAG, "Service unbound");
//            Toast.makeText(getApplicationContext(), "Disconnected from Service", 
//                    Toast.LENGTH_SHORT).show();
//        }
//        
//    }; // end ServiceConnection anonymouse inner class
//
//    /********************************** onCreate ******************************/
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_prime_number);
//        mFrom = (TextView)findViewById(R.id.from);
//        mTo = (TextView)findViewById(R.id.to);
//        mCount = (TextView)findViewById(R.id.count);
//        mPrimeNumbers = (TextView)findViewById(R.id.text);
//        mActionButton = (Button)findViewById(R.id.button);
//    }
//
//    /*************************** onCreateOptionsMenu **************************/
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.prime_number, menu);
//        return true;
//    }
//
//    /************************** onOptionsItemSelected *************************/
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings)
//        {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//    
//    /********************************** onStart *******************************/
//    @Override
//    protected void onStart()
//    {
//        startService(new Intent(getBaseContext(), PrimeNumberService.class));
//        bindService(new Intent(getApplicationContext(),
//               PrimeNumberService.class), mServiceConnection, BIND_AUTO_CREATE);
//        super.onStart();
//    }
//    
//    /********************************** onStop ********************************/
//    @Override
//    protected void onStop()
//    {
//        if(mBoundService != null)
//            unbindService(mServiceConnection);
//        super.onStop();
//    }
//    
//    /*************************** onSaveInstanceState **************************/
//    @Override
//    protected void onSaveInstanceState(Bundle outState)
//    {
//        super.onSaveInstanceState(outState);
//        outState.putString(KEY_PRIME_NUMBERS, mPrimeNumbers.getText().toString());
//    }
//    
//    /*************************** onRestoreInstanceState ***********************/
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState)
//    {
//        super.onRestoreInstanceState(savedInstanceState);
//        if(savedInstanceState != null)
//        {
//            final String primeNumbersText = savedInstanceState.
//                    getString(KEY_PRIME_NUMBERS);
//            if(primeNumbersText != null)
//                mPrimeNumbers.setText(primeNumbersText);
//        }
//    }
//
//    /********************************** onClick *******************************/
//    @Override
//    public void onClick(View v)
//    {
//        switch(v.getId())
//        {
//        case R.id.button:
//            if(mBoundService != null)
//            {
//                final String sFrom = mFrom.getText().toString().trim();
//                final String sTo = mTo.getText().toString().trim();
//                final String sCount = mCount.getText().toString().trim();
//                
//                LinkedList<String> emptyFields = new LinkedList<String>();
//                if(sFrom.isEmpty()) emptyFields.add("from");
//                if(sTo.isEmpty()) emptyFields.add("to");
//                
//                if(emptyFields.size() > 0)
//                {
//                    final String message = getResources().getQuantityString(
//                            R.plurals.empty_field, emptyFields.size(),
//                            emptyFields.toString());
//                    Toast.makeText(getApplicationContext(), message,
//                            Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                
//                try
//                {
//                    final long lngFrom = Long.parseLong(sFrom);
//                    final long lngTo = Long.parseLong(sTo);
//                    final int nCount = sCount.isEmpty() ? Integer.MAX_VALUE :
//                        Integer.parseInt(sCount); // default to ALL
//                    if(lngFrom < 0 || lngTo < 0)
//                        throw new NumberFormatException("Values must be "
//                                + "positive.");
//                    if(lngFrom > lngTo)
//                        throw new NumberFormatException("The 'from' value must "
//                                + "be less-than or equal-to the 'to' value.");
//                    GetPrimeNumbersTask task = new GetPrimeNumbersTask(
//                            mActionButton, mPrimeNumbers,
//                            new PrimeNumberServiceProvider());
//                    task.execute(lngFrom, lngTo, nCount);
//                }
//                catch (NumberFormatException e)
//                {
//                    Toast.makeText(getApplicationContext(), e.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                }
//            }
//            break;
//        default:
//            // do nothing, not handled
//        }
//    }
//    
//    /******************* CLASS: PrimeNumberServiceProvider ********************/
//    private class PrimeNumberServiceProvider implements ServiceProvider<PrimeNumberService>
//    {
//        /***************************** getService *****************************/
//        @Override
//        public PrimeNumberService getService()
//        {
//            return mBoundService;
//        }
//        
//    } // end PrimeNumberServiceProvider
//    
//    /************************ CLASS: GetPrimeNumbersTask **********************/
//    private static class GetPrimeNumbersTask extends AsyncTask<Number, Void, long[]>
//    {
//        private static final long[] EMPTY_LONGARRAY = new long[0];
//        
//        private WeakReference<View> mWeakInteractionView;
//        private WeakReference<TextView> mWeakTextView;
//        private ServiceProvider<PrimeNumberService> mServiceProvider;
//        
//        /************************** CONSTRUCTOR *******************************/
//        public GetPrimeNumbersTask(View interactionView, TextView displayView,
//                ServiceProvider<PrimeNumberService> serviceProvider)
//        {
//            if(serviceProvider == null)
//                throw new NullPointerException("You must provide a Service "
//                        + "Provider.");
//            mWeakInteractionView = new WeakReference<View>(interactionView);
//            mWeakTextView = new WeakReference<TextView>(displayView);
//            mServiceProvider = serviceProvider;
//            
//        }
//        
//        /****************************** onPreExecute **************************/
//        @Override
//        protected void onPreExecute()
//        {
//            super.onPreExecute();
//            setInteractionViewEnabled(false);
//        }
//        
//        /***************************** doInBackground *************************/
//        @Override
//        protected long[] doInBackground(Number... params)
//        {
//            final long[] results;
//            if(params.length == 3)
//            {
//                final long from = (Long)params[0];
//                final long to = (Long)params[1];
//                final int count = (Integer)params[2];
//                PrimeNumberService service = mServiceProvider.getService();
//                if(service != null)
//                    results = service.getPrimeNumbers(from, to, count);
//                else
//                    results = EMPTY_LONGARRAY; 
//            }
//            else
//            {
//                results = EMPTY_LONGARRAY;
//            }
//                
//            return results;
//        }
//        
//        /***************************** onCacelled *****************************/
//        @Override
//        protected void onCancelled()
//        {
//            super.onCancelled();
//            setInteractionViewEnabled(true);
//        }
//        
//        /**************************** onPostExecute ***************************/
//        @Override
//        protected void onPostExecute(long[] results)
//        {
//            super.onPostExecute(results);
//            setInteractionViewEnabled(true);
//            displayResults(results);
//        }
//        
//        /************************ setInteractionViewEnabled *******************/
//        private boolean setInteractionViewEnabled(boolean enabled)
//        {
//            final boolean bRet;
//            final View v = mWeakInteractionView.get();
//            if(v != null)
//            {
//                v.setEnabled(enabled);
//                bRet = true;
//            }
//            else
//            {
//                bRet = false;
//            }
//            
//            return bRet;
//        }
//        
//        /*************************** displayResults ***************************/
//        private boolean displayResults(long[] values)
//        {
//            final boolean bRet;
//            final TextView tv = mWeakTextView.get();
//            if(tv != null)
//            {
//                StringBuilder sb = new StringBuilder();
//                sb.append(Arrays.toString(values));
//                final String results = sb.substring(1, sb.length()-1);
//                tv.setText(results.replace(',', '\n'));
//                bRet = true;
//            }
//            else
//            {
//                bRet = false;
//            }
//            
//            return bRet;
//        }
//        
//    } // end GetPrimeNumbersTask
//    
} // end PrimeNumberActivity
