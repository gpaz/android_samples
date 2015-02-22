/*****************************************************************************\
|                                                                             |
|   Author :   Galo Paz <galopaz@outlook.com>                                 |
|     Date :   Jun 20, 2013                                                   |
|     Name :   DrawCircle.java                                                |
|                                                                             |
|  Purpose :   Create an Object inherited from Android's View class that will |
|              be able to draw circles with random colors at random points    |
|              within itself without erasing itself automatically.            |
|              This object should created by the Android system by placing an |
|              instance of this class in a XML layout file.                   |
|                                                                             |
| Extra : Create a method to clear the screen.                                |
|                                                                             |
\*****************************************************************************/

package com.cs211d.a2;

import android.graphics.*;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import com.cs211d.util.AndroidUtilities; //my class of utilities


/**
 * Inheriting View class, this subclass has the ability to draw random circles
 * within it's bounds on the device's screen.
 * 
 * @author Galo Paz <galopaz@outlook.com>
 * @version 1.1
 * @since June 23, 2013
 */
public class DrawCircle extends View
{
   /***PROTECTED VALUE DECLARATIONS***/
   protected Paint p;
   protected int[] colors;
   protected int[] x_coor;
   protected int[] y_coor;
   protected int radius;
   protected int extra_circles;
   /***END VALUE DECLARATIONS***/
   
   /*-------------------- DrawCircle Constructor --------------------*/
   /**
    * Constructor calls the initialize method to initialize the object.
    * 
    * @see #init() 
    * @param con
    * @param attrs 
    */
   public DrawCircle(Context con, AttributeSet attrs)
   {
      super(con, attrs);
      initialize();
   }
   
   /*-------------------- DrawCircle Constructor --------------------*/
   /**
    * Constructor calls the initialize method to initialize the object.
    * 
    * @see #initialize() 
    * @param con
    * @param attrs 
    */
   public DrawCircle(Context con)
   {
      super(con);
      initialize();
   }
   
   /*--------------------------- initialize -------------------------*/
   /**
    * Initializes the protected member variables.
    */
   protected void initialize()
   {
      x_coor = new int[0];
      y_coor = new int[0];
      colors = new int[0];
      p = new Paint(Paint.ANTI_ALIAS_FLAG);
      p.setColor(Color.RED);
      p.setStyle(Paint.Style.FILL);
      radius = 0xf; //The constant radius of the circles
      extra_circles = 0;
   }

   /*--------------------------- onDraw() ---------------------------*/
   /**
    * {@inheritDoc }
    * 
    * Calls a method to randomize any circles that have not yet been created 
    * and draws all circles to the screen.
    * 
    * @param c 
    */
   @Override
   protected void onDraw(Canvas c)
   {
      super.onDraw(c);
    
      while(extra_circles > 0)
      {
         createRandomCircle();
         extra_circles--;
      }
      for (int i = 0; i < colors.length; i++){
         p.setColor(colors[i]); //set color
         c.drawCircle(x_coor[i], y_coor[i], radius, p); //draw circle
      }
   }
   
   /*----------------------- createRandomCircle ------------------------*/
   /**
    * Creates a circle of radius= this.radius, of a random color, and at a 
    * random location within the bounds of this view in the layout.  Uses 
    * rand(int, int) from my own package AndroidUtilities to randomize values.
    */
   protected void createRandomCircle()
   {
      int x = AndroidUtilities.rand(radius, this.getWidth()-radius-1);
      int y = AndroidUtilities.rand(radius, this.getHeight()-radius-1);
      int[] c = new int[3];
      for(int i = 0;i<c.length;i++)
      {
         c[i] = AndroidUtilities.rand(0, 255);
      }
      x_coor = addToArray(x, x_coor);
      y_coor = addToArray(y, y_coor);
      colors = addToArray(Color.rgb(c[0],c[1],c[2]), colors);
   }
   
   /*--------------------------- clearCanvas ---------------------------*/
   /**
    * Deletes all stored circles in this view.  
    */
   public void clearCanvas()
   {
      x_coor = new int[0];
      y_coor = new int[0];
      colors = new int[0];
   }
   
   /*---------------------------- addToArray ---------------------------*/
   /**
    * Adds an int type value to an int[] array and returns the new 
    * array.
    * 
    * @param val     value to be added to the array.
    * @param array   array with existing values that val must be added to.
    * @return int[] type with val added at the end.
    */
   private int[] addToArray(int val, int[] array)
   {
      int[] newarray = new int[array.length+1];
      for(int i = 0; i < array.length; i++)
      {
         newarray[i] = array[i];
      }
      newarray[array.length] = val;
      return newarray;
   }
   
   /*----------------------------- setExtra ---------------------------*/
   /**
    * Sets the number of extra random circles that need to drawn to the screen.
    * 
    * @param more 
    */
   protected void setExtra(int more)
   {
      if(more < 0)
      {
         extra_circles = 0;
      }else
      {
         extra_circles = more;
      }
   }
   
   /*---------------------------- setCircles --------------------------*/
   /**
    * Sets the x, y, and color values for the circles that must be drawn to the 
    * view.  Used for returning from a saved state.
    * 
    * @param xcor x
    * @param ycor
    * @param cols 
    */
   protected void setCircles(int[] xcor, int[] ycor, int[] cols)
   {
      if(xcor != null)
      {
         x_coor = xcor;
         y_coor = ycor;
         colors = cols;
      }else
      {
         this.clearCanvas();
      }
   }

}
