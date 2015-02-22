/*****************************************************************************\
|                                                                             |
|    Author :  Galo Paz <galopaz@outlook.com>                                 | 
|      Date :  Jul 11, 2013                                                   |
|      Name :  User.java                                                      |
|                                                                             |
| Objective :  Container for User information, very basic and implements      |
|              parcelable to pass through intents and bundles.                |
|                                                                             |
|     Extra :  None                                                           |
|                                                                             |
\*****************************************************************************/
package com.cs211d.a5; ///HOMEWORK #5

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Author: Galo Paz <galopaz@outlook.com>
 * Date  : Jul 12, 2013
 * Program Name: User.java
 * Objective: To hold information about a User and provide methods to access
 *            and manipulate that data.
 *
**/
public class User implements Parcelable
{
   
   private Integer mUserId;
   private String mUserName;
   
   /*------------------------ Parcelable.Creator ---------------------------*/
   public static final Parcelable.Creator<User> CREATOR
          = new Parcelable.Creator<User>() {
             
      /*------------- createFromParcel() -------------*/
      public User createFromParcel(Parcel in) {
          return new User(in);
      }

      /*----------------- newArray() -----------------*/
      public User[] newArray(int size) {
          return new User[size];
      }
   };
   
   /*-------------------------- describeContents() -------------------------*/
   public int describeContents() {
      return 0;
   }
   
   /*-------------------------- writeToParcel() ----------------------------*/
   public void writeToParcel(Parcel out, int flags) {
      out.writeInt(mUserId);
      out.writeString(mUserName);
   }
   
   /*--------------------------- User(Parcel) -----------------------------*/
   /**
    * Copy Constructor for input of a Parcel.
    * @param in Parcel object
    */
   private User(Parcel in) {
      mUserId = in.readInt();
      mUserName = in.readString();
   }

   /*-------------------------- User(int, String) -------------------------*/
   /**
    * Constructor to initialize the members of this User class object.
    * @param user_id id of user given by the autoincrementing id field in the
    *                database's user table.
    * @param alias the user's name.
    */
   public User(int user_id, String alias)
   {
      mUserId = user_id;
      mUserName = alias != null ? alias : "guest"+mUserId;
      //game_ids = game_ids != null ? game_ids : new ArrayList<Integer>();
   }
   
   //Getters, no setters: user should be only initialized with original values,
   //which are not changeable.
   public Integer getUserId() {return mUserId;}
   public String getAlias() {return mUserName;}
}
