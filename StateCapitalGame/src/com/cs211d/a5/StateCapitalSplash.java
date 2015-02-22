/**
 * ***************************************************************************\
 * | | | Author : Galo Paz <galopaz@outlook.com> | | Date : Jul 11, 2013 | |
 * Name : StateCapitalSplash.java | | | | Objective : Provide a splash screen
 * while loading resources. In fact, | | because loading takes such a short
 * time, I extended the time | | this activity is displayed so that the user has
 * more time to | | see the Title; so it's not just flashed to the user. | | | |
 * Extra : None | | |
 * \****************************************************************************
 */
package com.cs211d.a5; ///HOMEWORK #5

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Author  : Galo Paz <galopaz@outlook.com>
 * Date    : Jul 11, 2013
 * Purpose : displays a splash screen to the user for a bit of time before
 *           automatically switching over to the start screen.
 */
public class StateCapitalSplash extends Activity {

   private static String TAG = "StateCapitalSplash"; //tag for logging
   private static long SLEEP_TIME = 1500;    // Sleep time in milliseconds
   /*------------------------onCreate()------------------------*/

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.splash_layout);

      // Start timer and launch main activity
      LoadDataThread data_loader = new LoadDataThread();
      data_loader.start();
   }

   /**
    * LoadDataThread class to handle loading of resources on a different thread.
    */
   private class LoadDataThread extends Thread {

      /*-------------------------------- run() ------------------------------*/
      /**
       * Loads the state capitals & sleeps for some time, then starts the main
       * activity and closes this one.
       */
      @Override
      public void run() {
         StateCapitalDBHandler handler;
         handler = new StateCapitalDBHandler(StateCapitalSplash.this);
         //just to initialize the DB if needed
         //////////////////////////////////
         User user = handler.getLastUser();
         //////////////////////////////////
         try {
            Thread.sleep(SLEEP_TIME);
         } catch (InterruptedException ex) {
            Log.i(TAG, ex.getMessage());
         }

         Intent intent = new Intent(StateCapitalSplash.this, StartScreen.class);
         // add name of the last user to have played the game.
         intent.putExtra("last_user", user);
         // send user to the start screen
         StateCapitalSplash.this.startActivity(intent);
         // finish up the splash screen activity.
         StateCapitalSplash.this.finish();
      }
   }
}
