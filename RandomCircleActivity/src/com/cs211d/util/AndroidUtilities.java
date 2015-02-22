/*****************************************************************************\
|                                                                             |
|  Author :  Galo Paz <galopaz@outlook.com>                                   |
|    Date :  Jun 20, 2013                                                     |
|    Name :  AndroidUtilities.java                                            |
| Purpose :  Provide utilities when programming for android devices           |
|                                                                             |
\*****************************************************************************/

package com.cs211d.util;

/**
 * Tools to help with android projects
 * 
 * @author Galo Paz <galopaz@outlook.com>
 */
public abstract class AndroidUtilities 
{
   
   /*--------------------------- rand() ---------------------------*/
   /**
    * Returns a random value between int a and int b.
    * 
    * @param a  the lowest integer value
    * @param b  the highest integer value
    * @return A pseudo-random integer between int a and int b
    */
   public static int rand(int a, int b)
   {
      return((int)((b-a+1) * Math.random() + a));
   }

}
