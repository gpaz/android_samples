/*****************************************************************************\
|                                                                             |
|    Author :  Galo Paz <galopaz@outlook.com>                                 |
|      Date :  Jun 20, 2013                                                   |
|      Name :  RandomCircleActivity.java                                      |
|                                                                             |
| Objective :  Display a button on the android device's screen labeled        |
|              "Push Me".  Each time this button in pressed, a small solid    |
|              circle of random color and of random placement on the screen   |
|              is drawn, not covering the "Push Me" button.  The layouts      |
|              should be set as XML files in the res/layout folder.           |
|                                                                             |
| Extra :   When switching from portrait to landscape, and vice versa, add    |
|           enough random circles on to the screen in that configuration as   |
|           are on the screen in the previous configuration.  That way both   |
|           configurations hold the same number of random circles.  Layouts   |
|           are placed in the res/layout-land for landscape and               |
|           res/layout-port for portrait.                                     |
|                                                                             |
\*****************************************************************************/

package com.cs211d.a2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Surface;

/**
 * Activity that draws random circles on the screen whenever a user clicks 
 * the 'Push Me' button.  Implementation also features a button to clear the 
 * screen and handles orientation.  There are two XML layouts in the 
 * res/layout-land and res/layout-port folders respectively:
 *    
 *    1. landscape configuration
 *    2. portrait configuration
 * 
 * The Android system takes care of the layout changes.
 * 
 * @author Galo Paz <galopaz@outlook.com>
 * @version 1.1
 * @since June 23, 2013
 */
public class RandomCircleActivity extends Activity
{
   /***PRIVATE VALUE DECLARATIONS***/
   //Private members not available outside this class.
   private int[] previous_x;
   private int[] previous_y;
   private int[] previous_col;
   
   private boolean current_config;  //false = portrait; true = landscape
   /***END VALUE DECLARATIONS***/
   
   /*--------------------------- onCreate() -----------------------------*/
   /**
    * {@inheritDoc }
    * 
    * @param savedInstanceState 
    */
   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      intialize(savedInstanceState);
   }
   
   /*------------------------- buttonHandler() --------------------------*/
   /**
    * The 'onClick' handler for the 'Push Me' and 'Clear' buttons.
    * 
    * @param view  The Button that was clicked.  
    */
   public void buttonHandler(View view)
   {
      DrawCircle dc = (DrawCircle)this.findViewById(R.id.Draw_Space);
      switch (view.getId())
      {
         case R.id.Button_PushMe:
            addRandomCircle(dc); //Add random circle to screen
            break;
         case R.id.Button_Clear:
            clearScreen(dc);  //clear screen of all circles
            break;
      }
   }

   /*---------------------- onSaveInstanceState() -----------------------*/
   /**
    * {@inheritDoc }
    * 
    * Helps to persist data across configurations.
    * 
    * @param outState 
    */
   @Override
   protected void onSaveInstanceState(Bundle outState)
   {
      DrawCircle dc = (DrawCircle)this.findViewById(R.id.Draw_Space);
      if(!current_config) //portrait
         this.saveToState(outState, dc.x_coor, dc.y_coor, dc.colors,
                 previous_x, previous_y, previous_col);
      else //landscape
         this.saveToState(outState, previous_x, previous_y, previous_col,
                 dc.x_coor, dc.y_coor, dc.colors);
   }
   
   /*--------------------------- initialize() ---------------------------*/
   /**
    * Initializes the RandomCircleActivity class.
    * 
    * Private method is only called from the onCreate(Bundle) callback method.  
    * 
    * @param savedInstanceState  Reference to a bundle sent from the 
    *                            onCreate(Bundle) callback.
    */
   private void intialize(Bundle savedInstanceState)
   {
      int[] colors_port, xcor_port, ycor_port;
      int[] colors_land, xcor_land, ycor_land;
      
      //Retrieve persistent portrait values, or set null if none exist.
      try
      {
         xcor_port = savedInstanceState.getIntArray(
              getResources().getString(R.string.port_x));
         ycor_port = savedInstanceState.getIntArray(
              getResources().getString(R.string.port_y));
         colors_port = savedInstanceState.getIntArray(
              getResources().getString(R.string.port_color));
      }catch(Exception e)
      {
         colors_port = xcor_port = ycor_port = null;
         Log.i(getResources().getString(R.string.log_tag), 
                 getResources().getString(R.string.log_string_port));
      }
      
      //Retrieve persistent landscape values, or set null if none exist.
      try
      {
         xcor_land = savedInstanceState.getIntArray(
              getResources().getString(R.string.land_x));
         ycor_land = savedInstanceState.getIntArray(
              getResources().getString(R.string.land_y));
         colors_land = savedInstanceState.getIntArray(
              getResources().getString(R.string.land_color));
      }catch (Exception e)
      {
         colors_land = xcor_land = ycor_land = null;
         Log.i(getResources().getString(R.string.log_tag), 
                 getResources().getString(R.string.log_string_land));
      }
      
      //if currently in portrait mode
      if(!inLandscapeConfig())
      {
         initPrivateValues(xcor_land, ycor_land, colors_land,
                 xcor_port, ycor_port, colors_port);
      }else //if currently in landscape mode
      {
         initPrivateValues(xcor_port, ycor_port, colors_port,
                 xcor_land, ycor_land, colors_land);
      }
   }
   
   /*----------------------- initPrivateValues() -------------------------*/
   /**
    * Initializes the private members of the RandomCircleActivity class.
    * Called from the initialize method.  Null will not throw an exception.
    * 
    * @param pre_x   x-values from the previous configuration of the device.
    * @param pre_y   y-values from the previous configuration of the device.
    * @param pre_col color values from the previous configuration of the device.
    * @param cur_x   x-values from the current configuration of the device.
    * @param cur_y   y-values from the current configuration of the device.
    * @param cur_col color values from the current configuration of the device.
    */
   private void initPrivateValues(int[] pre_x, int[] pre_y, int[] pre_col,
           int[] cur_x, int[] cur_y, int[] cur_col)
   {
      DrawCircle dc;
      int extra_count;
      if(pre_x != null && cur_x != null)
      {
         extra_count = pre_x.length-cur_x.length;
      }else if (pre_x != null)
      {
         extra_count = pre_x.length;
      }else
      {
         extra_count = 0;
      }
      current_config = inLandscapeConfig();
      previous_x = pre_x;
      previous_y = pre_y;
      previous_col = pre_col;
      dc = (DrawCircle)this.findViewById(R.id.Draw_Space);
      dc.setCircles(cur_x, cur_y, cur_col);
      //add extra circles
      dc.setExtra(extra_count);
   }
   
   /*------------------------- addRandomCircle() -------------------------*/
   /**
    * Adds a random circle to the DrawCircle view.
    * 
    * Invokes DrawCircle.drawRandomCircle method to take care of the addition 
    * to the view.
    * 
    * @param dc The DrawCircle view of the activity.  
    */
   private void addRandomCircle(DrawCircle dc)
   {
      dc.createRandomCircle();
      dc.invalidate();
   }
   
   /*------------------------ inLandscapeConfig() ------------------------*/
   /**
    * Returns the current configuration of the device.
    * 
    * @return  true if currently in landscape, false if in portrait.
    */
   private boolean inLandscapeConfig()
   {
      //get current configuration
      int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
      //if in ladscape configuration
      if(rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270)
         return true;
      else
         return false;
   }
   
   /*--------------------------- clearScreen() ---------------------------*/
   /**
    * Clears all circles from the screens of both configurations.
    * Invokes the DrawCircle.clearCanvas method to clear the current screen.
    * 
    * @param dc DrawCircle view.
    */
   private void clearScreen(DrawCircle dc)
   {
      previous_x = null;
      previous_y = null;
      previous_col = null;
      dc.clearCanvas();
      dc.invalidate();
   }
   
   /*--------------------------- saveToState() ---------------------------*/
   /**
    * Saves the parameters to a Bundle object.
    * 
    * @param outState      A Bundle object to save the parameters to.
    * @param toPortX       x-values for portrait configuration.
    * @param toPortY       y-values for portrait configuration.
    * @param toPortColor   color values for portrait configuration.
    * @param toLandX       x-values for landscape configuration.
    * @param toLandY       y-values for landscape configuration.
    * @param toLandColor   color values for landscape configuration.
    */
   private void saveToState(Bundle outState, int[] toPortX, int[] toPortY,
           int[] toPortColor, int[] toLandX, int[] toLandY, int[] toLandColor)
   {
      //set portrait parameters
      outState.putIntArray(this.getResources().
                 getString(R.string.port_x), toPortX);
      outState.putIntArray(this.getResources().
                 getString(R.string.port_y), toPortY);
      outState.putIntArray(this.getResources().
                 getString(R.string.port_color), toPortColor);
      
      //set landscape parameters
      outState.putIntArray(this.getResources().
                 getString(R.string.land_x), toLandX);
      outState.putIntArray(this.getResources().
                 getString(R.string.land_y), toLandY);
      outState.putIntArray(this.getResources().
                 getString(R.string.land_color), toLandColor);
   }
   
}
