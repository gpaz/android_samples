/*****************************************************************************\
|                                                                             |
|    Author :  Galo Paz <galopaz@outlook.com>                                 |
|      Date :  Jul 18, 2013                                                   |
|      Name :  StatesDBHandler.java                                           |
|   Purpose :  Provide handling of the statecapital.db SQLite database.       |
|     Extra :  None                                                           |
|                                                                             |
\*****************************************************************************/

package com.cs211d.a6; //Homework #6

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class StatesDBHandler extends SQLiteOpenHelper {
   
   // Database Version
   private static final int DB_VERSION = 1;
 
   // Database name
   private static final String DB_NAME = "statecapital.db";
 
   // Table names
   public static final String STATES = "states";

   // States table columns names
   public static final String KEY_STATE_ID = "_id";
   public static final String COL_STATE = "state";
   public static final String COL_CAPITAL = "capital";
   
   // SQL STATEMENT : Create table for states and their capitals
   private static final String CREATE_STATES_TABLE = 
           "CREATE TABLE IF NOT EXISTS " + STATES + "(" 
           + KEY_STATE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_STATE 
           + " TEXT NOT NULL," + COL_CAPITAL + " TEXT NOT NULL)";
   
   // Keeps track of the input context.
   private Context mContext;

   //Log Tag
   private static final String TAG = "StatesDBHandler";
   
   /*-------------------- StateCapitalDBHandler(Context) --------------------*/
   public StatesDBHandler(Context context) {
      super(context, DB_NAME, null, DB_VERSION);
      this.mContext = context;
   }
   
   /*------------------------------- onCreate() ----------------------------*/
   /**
    * Creates tables and triggers in the SQLite Database.
    * 
    * @param db SQLiteDatabase object.
    */
   public void onCreate(SQLiteDatabase db) {
      db.execSQL(CREATE_STATES_TABLE);
      populateStatesTable(db);
      //db.close();
   }
   
   /*-------------------------- onUpgrade() ------------------------------*/
   /**
    * Upgrading database.  Only for states, the other information is kept.
    * 
    * @param db
    * @param oldVersion
    * @param newVersion 
    */
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS " + STATES);
      // Create tables again
      onCreate(db);
   }
   
   /*---------------------- populateStatesTable() -------------------------*/
   /**
    * Populates the states table with values from the file 'us_states' in the 
    * res/raw folder.
    * 
    * @param db 
    */
   private void populateStatesTable(SQLiteDatabase db) {
      
      try
      {
         InputStream is;
         is = mContext.getResources().openRawResource(R.raw.us_states);
         InputStreamReader isr = new InputStreamReader(is);
         BufferedReader reader = new BufferedReader(isr);
         String line;
         String[] state_capital;
         
         // Begin Transaction
         db.beginTransaction();
         try
         {
            while (( line = reader.readLine()) != null) {
               if(!line.toLowerCase().contains("state") && !line.contains("--")
                       && !line.trim().isEmpty())
               {
                  state_capital = parseLine(line);
                  if(state_capital != null && state_capital.length == 2)
                  {
                     ContentValues values = new ContentValues();
                     values.put(COL_STATE, state_capital[0]);
                     values.put(COL_CAPITAL, state_capital[1]);
                     db.insert(STATES, null, values);
                  }
               }
            }
            db.setTransactionSuccessful();
         }catch (Exception e)
         {
            Log.e(TAG, e.getMessage());
         }finally
         {
            // Commit the transaction
            db.endTransaction();
         }
      }
      catch (Exception e)
      {
         Log.w("DBHandler", "SQLite", e);
      }
   }
   
   /*----------------------------- parseLine() ----------------------------*/
   /**
    * Parses a string that contains a State and its Capital separated by
    * multiple spaces and returns a String array with only these two elements.
    * 
    * @param line String that needs parsing for State and Capital.
    * @return Token Strings strip of leading and trailing spaces: 
    *             String[0] = state, 
    *             String[1] = capital.
    */
   private synchronized String[] parseLine(String line)
   {
      if(line == null)
         return new String[0];
      String[] state_capital = new String[0];
      if(!line.isEmpty())
      {   
         // splits at place with two consequtive spaces.
         state_capital = line.split("[ ][ ]", 2);
         for(int i = 0; i < state_capital.length; i++)
            state_capital[i] = state_capital[i].trim();
      }
      return state_capital;
   }
   
   /*----------------------------- getCapitals() --------------------------*/
   /**
    * Retrieves all capitals from the states table.
    * 
    * @return String array of capitals.
    */
   public synchronized String[] getCapitals()
   {
      String[] capitals = {};
      SQLiteDatabase db = getReadableDatabase();
      Cursor cursor = db.query(STATES, new String[]{COL_CAPITAL}, null, null
              , null, null, null);
      if(cursor.moveToFirst())
      {
         capitals = new String[cursor.getCount()];
         int i = 0;
         do
         {
            capitals[i] = cursor.getString(0);
            i++;
         }while(cursor.moveToNext());
      }
      cursor.close();
      db.close();
      return capitals;
   }
   
   /*----------------------------- getStates() -----------------------------*/
   /**
    * Query database for all states and return them into an array.
    * 
    * @return String array of us state names found in the states table.
    */
   public synchronized String[] getStates()
   {
      String[] states = {};
      SQLiteDatabase db = getReadableDatabase();
      Cursor cursor = db.query(STATES, new String[]{COL_STATE}, null, null
              , null, null,COL_STATE + " DESC");
      if(cursor.moveToFirst())
      {
         states = new String[cursor.getCount()];
         int i = 0;
         do
         {
            states[i] = cursor.getString(0);
            i++;
         }while(cursor.moveToNext());
      }
      cursor.close();
      db.close();
      return states;
   }
   
   public synchronized Cursor getStatesCursor()
   {
      SQLiteDatabase db = getReadableDatabase();
      Cursor cursor = db.query (STATES, new String[]{KEY_STATE_ID,COL_STATE}
              , null, null, null, null, COL_STATE + " ASC");
      //cursor.moveToFirst();
      //db.close();
      return cursor;
   }
   /*------------------------------ getState() -----------------------------*/
   /**
    * Returns the state name associated with the input capital name.
    * 
    * @param capital String of the capital name.
    * @return String of the state name with the matching capital.
    */
   public String getState(String capital)
   {
      return getRecord(COL_STATE, COL_CAPITAL, capital);
   }
   
   /*---------------------------- getCapital() -----------------------------*/
   /**
    * Returns the capital name associated with the input state name.
    * 
    * @param state String of the state name.
    * @return String of the capital name with the matching state.
    */
   public String getCapital(String state)
   {
      return getRecord(COL_CAPITAL, COL_STATE, state);
   }
   
   public String getCapital(long id)
   {
      return getRecord(COL_CAPITAL, KEY_STATE_ID, String.valueOf(id));
   }
   
   /*---------------------------- getRecord() -----------------------------*/
   /**
    * Query's the states database for 'return_column' field that has matches
    * the record's 'match_str' input with its "match_column' field.
    * 
    * @param return_column The column from which to select a string from.
    * @param match_column The column from which to search for 'match_str' from.
    * @param match_str The string to search and match with that of a record in
    *                  the 'match_column' column.
    * @return String value from the return column that matches the constraints.
    *         NULL if no record is found.
    */
   private String getRecord(String return_column, String match_column
           , String match_str)
   {
      String return_val = null;
      SQLiteDatabase db = getReadableDatabase();
      Cursor cursor = db.query(STATES, new String[]{return_column}
              , match_column+"=?",new String[]{match_str}, null
              , null,null);
      if(cursor.moveToFirst())
         return_val = cursor.getString(0);
      cursor.close();
      db.close();
      return return_val;
   }

}
