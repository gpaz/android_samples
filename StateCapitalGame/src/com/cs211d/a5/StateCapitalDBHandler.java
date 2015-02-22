/**
 * Author  : Galo Paz <galopaz@outlook.com>
 * Date    : Jul 14, 2013
 * Name    : StateCapitalDBHandler.java
 * Purpose : Provide handling of the statecapital.db sql database.
 * Extra   : None
 **/
package com.cs211d.a5; ///HOMEWORK #5

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.*;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class StateCapitalDBHandler extends SQLiteOpenHelper {

   // Database Version
   private static final int DB_VERSION = 1;
   // Database name
   private static final String DB_NAME = "statecapitals.db";
   // Table names
   private static final String STATES = "states_and_capitals";
   private static final String USERS = "users";
   private static final String GAMES = "games";
   // States table columns names
   private static final String KEY_STATE_ID = "state_id";
   private static final String COL_STATE = "state";
   private static final String COL_CAPITAL = "capital";
   // Users table column names
   private static final String KEY_USER_ID = "user_id"; // also in scores table
   private static final String COL_USERNAME = "username";
   private static final String COL_LAST_USER = "lastuser";
   private static final String USER_UPDATE_TRIGGER_NAME = "before_users_update";
   private static final String USER_INSERT_TRIGGER_NAME = "before_users_insert";
   // Games table
   private static final String KEY_GAME_ID = "game_id";
   private static final String COL_TIME_SPENT = "time";
   private static final String COL_CORRECT = "correct";
   private static final String COL_TOTAL = "total";
   // SQL STATEMENT : Create table for states and their capitals
   private static final String CREATE_STATES_TABLE =
           "CREATE TABLE IF NOT EXISTS " + STATES + "("
           + KEY_STATE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_STATE
           + " TEXT NOT NULL," + COL_CAPITAL + " TEXT NOT NULL)";
   // SQL STATEMENT : Create table for user information
   private static final String CREATE_USERS_TABLE =
           "CREATE TABLE IF NOT EXISTS " + USERS + " ("
           + KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_USERNAME
           + " TEXT NOT NULL," + COL_LAST_USER
           + " BOOLEAN NOT NULL DEFAULT 0)";
   // SQL STATEMENT : Creates table for game information and stats
   private static final String CREATE_GAMES_TABLE =
           "CREATE TABLE IF NOT EXISTS " + GAMES + " ("
           + KEY_GAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
           + KEY_USER_ID + " INTEGER NOT NULL," + COL_TIME_SPENT
           + " TEXT NOT NULL," + COL_TOTAL + " INTEGER NOT NULL,"
           + COL_CORRECT + " INTEGER NOT NULL, FOREIGN KEY(" + KEY_USER_ID
           + ") REFERENCES " + USERS + "(" + KEY_USER_ID + "))";
   // SQL STATEMENT : Creates a TRIGGER for the database to update the last user
   //                 field of all records to false before the table is updated
   //                 ensuring that only one record says true.  This is assuming
   //                 that the initial update is setting a value to true and
   //                 not false for the value of last user.  More constraints
   //                 should be implemented.
   private static final String CREATE_USER_UPDATE_TRIGGER =
           "CREATE TRIGGER " + USER_UPDATE_TRIGGER_NAME + " BEFORE UPDATE OF "
           + COL_LAST_USER + " ON " + USERS + " FOR EACH ROW BEGIN"
           + " UPDATE " + USERS + " SET " + COL_LAST_USER
           + " = 0 WHERE " + COL_LAST_USER + " = 1; END";
   // SQL STATEMENT : Creates a TRIGGER, same as previous the previous trigger
   //                 but for insert statements.
   private static final String CREATE_USER_INSERT_TRIGGER =
           "CREATE TRIGGER " + USER_INSERT_TRIGGER_NAME + " BEFORE INSERT ON "
           + USERS + " FOR EACH ROW BEGIN" + " UPDATE " + USERS + " SET "
           + COL_LAST_USER + " = 0 WHERE " + COL_LAST_USER + " = 1; END";
   // Keeps track of the input context.
   private Context _context;
   //Log Tag
   private static final String TAG = "StateCapitalDBHandler";

   /*-------------------- StateCapitalDBHandler(Context) --------------------*/
   public StateCapitalDBHandler(Context context) {
      super(context, DB_NAME, null, DB_VERSION);
      this._context = context;
      getWritableDatabase();
   }

   /*------------------------------- onCreate() ----------------------------*/
   /**
    * Creates tables and triggers in the SQLite Database.
    *
    * @param db SQLiteDatabase object.
    */
   public void onCreate(SQLiteDatabase db) {
      db.execSQL(CREATE_STATES_TABLE);
      db.execSQL(CREATE_USERS_TABLE);
      db.execSQL(CREATE_USER_UPDATE_TRIGGER);
      db.execSQL(CREATE_USER_INSERT_TRIGGER);
      db.execSQL(CREATE_GAMES_TABLE);
      _populateStatesTable(db);
      //db.close(); /// I don't know why I closed the database.  crash solved
   }

   /*-------------------------- onUpgrade() ------------------------------*/
   /**
    * Upgrading database. Only for states, the other information is kept.
    *
    * @param db
    * @param oldVersion
    * @param newVersion
    */
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // Drop older table if existed
      db.execSQL("DROP TABLE IF EXISTS " + STATES);
      //db.execSQL("DROP TABLE IF EXISTS " + USERS);
      //db.execSQL("DROP TABLE IF EXISTS " + GAMES);
      // Create tables again
      onCreate(db);
   }

   /*---------------------- _populateStatesTable() -------------------------*/
   /**
    * Populates the states table with values from the file 'us_states' in the
    * res/raw folder.
    *
    * @param db
    */
   private void _populateStatesTable(SQLiteDatabase db) {

      try {
         InputStream is;
         is = _context.getResources().openRawResource(R.raw.us_states);
         InputStreamReader isr = new InputStreamReader(is);
         BufferedReader reader = new BufferedReader(isr);
         String line;
         String[] state_capital;

         // Begin Transaction
         db.beginTransaction();
         try {
            while ((line = reader.readLine()) != null) {
               if (!line.toLowerCase().contains("state") && !line.contains("--")
                       && !line.trim().isEmpty()) {
                  state_capital = _parseLine(line);
                  if (state_capital != null && state_capital.length == 2) {
                     ContentValues values = new ContentValues();
                     values.put(COL_STATE, state_capital[0]);
                     values.put(COL_CAPITAL, state_capital[1]);
                     db.insert(STATES, null, values);
                  }
               }
            }
            db.setTransactionSuccessful();
         } catch (Exception e) {
            Log.e(TAG, e.getMessage());
         } finally {
            // Commit the transaction
            db.endTransaction();
         }
      } catch (Exception e) {
         Log.w("DBHandler", "SQLite", e);
      }
   }

   /*------------------------- getUsernames() -----------------------------*/
   /**
    * Returns string array of all usernames in the database.users table.
    *
    * @return String array of usernames.
    */
   public synchronized String[] getUsernames() {
      String[] names = {};
      SQLiteDatabase db = getReadableDatabase();
      Cursor cursor = db.query(USERS, new String[]{COL_USERNAME}, null, null, null, null, null);
      if (cursor.moveToFirst()) {
         names = new String[cursor.getCount()];
         int i = 0;
         do {
            names[i] = cursor.getString(0);
            i++;
         } while (cursor.moveToNext());
      }
      cursor.close();
      db.close();
      return names;
   }

   /*----------------------------- insertGame() --------------------------*/
   /**
    * Inserts only a completed game.
    *
    * @param game Game object
    * @return true if successful, false otherwise.
    */
   public synchronized boolean insertGame(Game game) {
      SQLiteDatabase db = getWritableDatabase();
      if (game.hasCompleted()) {
         db.beginTransaction();
         try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_ID, game.getUserId());
            values.put(COL_TIME_SPENT, game.getTime());
            values.put(COL_CORRECT, game.getNumCorrect());
            values.put(COL_TOTAL, game.getNumTotal());
            db.insert(GAMES, null, values);
            db.setTransactionSuccessful();
         } catch (Exception e) {
            return false;
         } finally {
            db.endTransaction();
         }
         db.close();
         return true;
      }
      db.close();
      return false;
   }

   /*------------------------- setAsCurrentUser() --------------------------*/
   /**
    * Sets the user from the User object as the current user in the database.
    *
    * @param user User object that is the current user.
    */
   public synchronized void setAsCurrentUser(User user) {
      SQLiteDatabase db = getWritableDatabase();
      ContentValues values = new ContentValues();
      values.put(COL_LAST_USER, true);
      db.update(USERS, values, KEY_USER_ID + "=?", new String[]{user.getUserId().toString()});
      db.close();
   }

   /*----------------------------- getLastUser() ----------------------------*/
   /**
    * Retrieves the user record that has a value of 1 (or true) in the lastuser
    * column of the users table.
    *
    * @return the last user that played as a User object, or null if no players
    * have played.
    */
   public synchronized User getLastUser() {
      return _getUser(COL_LAST_USER, "1");
   }

   /*--------------------------- getTopGames() -----------------------------*/
   /**
    * Returns a sorted array of Game objects representing the highest scoring
    * games in the local database (sorted by highest score first, and then by
    * the lowest time.)
    *
    * @param qty
    * @return
    */
   public synchronized Game[] getTopGames(int qty) {
      Game[] games;
      SQLiteDatabase db = getReadableDatabase();
      String[] args = {GAMES + "." + KEY_GAME_ID, GAMES + "." + COL_TIME_SPENT, GAMES + "." + COL_CORRECT, GAMES + "." + COL_TOTAL, USERS + "." + KEY_USER_ID, USERS + "." + COL_USERNAME, GAMES, USERS, GAMES + "." + KEY_USER_ID, USERS + "." + KEY_USER_ID, GAMES + "." + COL_CORRECT, GAMES + "." + COL_TIME_SPENT};
      String sql_format = "SELECT %s, %s, %s, %s, %s, %s "
              + "FROM %s "
              + "INNER JOIN %s "
              + "ON %s = %s "
              + "ORDER BY %s DESC, CAST(%s AS INTEGER) ASC " //to sort fiield
              + "LIMIT %d";                        // as an int not string
      String sql = String.format(sql_format, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], qty);

      Cursor cursor = db.rawQuery(sql, null);
      int count = cursor.getCount();
      games = new Game[count];
      if (cursor.moveToFirst() && cursor.getColumnCount() == 6) {
         int i = 0;
         do {
            User user = new User(cursor.getInt(4), cursor.getString(5));
            games[i++] = new Game(cursor.getInt(0), user, cursor.getLong(1),
                    cursor.getInt(2), cursor.getInt(3));

         } while (cursor.moveToNext());
      }
      cursor.close();
      db.close();
      return games;
   }

   /*---------------------------- getUser() --------------------------------*/
   /**
    * Returns the user from the database.users table that matches the input id.
    *
    * @param userid Intger id value of the user.
    * @return User object with the user information.
    */
   public synchronized User getUser(int userid) {
      return _getUser(KEY_USER_ID, String.valueOf(userid));
   }

   /*----------------------------- getUser() -----------------------------*/
   /**
    * Search database for the user that matches the input username, otherwise
    * will return a newly created user from the database for this username.
    *
    * @param username String of the username.
    * @return User object of the user matching the username, never null.
    */
   public synchronized User getUser(String username) {
      User user = _getUser(COL_USERNAME, username);
      if (user == null) {
         user = _createUser(username);
      }

      return user;
   }

   /*---------------------------- _createUser() ---------------------------*/
   /**
    * Creates user in the database.users table.
    *
    * @param username The username of the new user.
    * @return User object of the new user created in the database.
    */
   private synchronized User _createUser(String username) {
      SQLiteDatabase db = getWritableDatabase();
      ContentValues values = new ContentValues();
      values.put(COL_USERNAME, username);
      values.put(COL_LAST_USER, true);
      db.beginTransaction();
      try {
         db.insertOrThrow(USERS, null, values);
         db.setTransactionSuccessful();
      } catch (SQLException e) {
         Log.e(TAG, e.getMessage());
      } finally {
         db.endTransaction();
      }
      db.close();
      return this.getUser(username);
   }

   /*-------------------------- getUsers() -------------------------------*/
   /**
    * Gets multiple users from the database corresponding to the input integer
    * array of user ids and returns them as User objects in an array.
    *
    * @param userids Integer array of user ids
    * @return Array of User objects
    */
   public synchronized User[] getUsers(int[] userids) {
      User[] users = new User[userids.length];
      for (int i = 0; i < userids.length; i++) {
         users[i] = getUser(userids[i]);
      }
      return users;
   }

   /*---------------------------- _getUser() ------------------------------*/
   /**
    * Gets a Single user by the input selection parameters. Equiv to SQL: WHERE
    * column_name = argv[0].
    *
    * @param column_name Select Column name
    * @param argv array of just one value.
    * @return User object of the matching user in the database.
    */
   private synchronized User _getUser(String column_name, String argv) {
      String[] columns = USER_COLUMNS();
      String selection = column_name + "=?";
      String[] selectionArgs = {argv};
      String limit = "1";
      User user = null;
      SQLiteDatabase db = getReadableDatabase();
      Cursor cursor = db.query(USERS, columns, selection, selectionArgs, null, null, null, limit);
      if (cursor.moveToFirst()) {
         user = new User(cursor.getInt(0), cursor.getString(1));
      }
      cursor.close();
      db.close();
      return user;
   }

   /*----------------------------- _parseLine() ----------------------------*/
   /**
    * Parses a string that contains a State and its Capital separated by
    * multiple spaces and returns a String array with only these two elements.
    *
    * @param line String that needs parsing for State and Capital.
    * @return Token Strings strip of leading and trailing spaces: String[0] =
    * state, String[1] = capital.
    */
   private synchronized String[] _parseLine(String line) {
      if (line == null) {
         return new String[0];
      }
      String[] state_capital = new String[0];
      if (!line.isEmpty()) {
         // splits at place with two consequtive spaces.
         state_capital = line.split("[ ][ ]", 2);
         for (int i = 0; i < state_capital.length; i++) {
            state_capital[i] = state_capital[i].trim();
         }
      }
      return state_capital;
   }

   /*----------------------------- getCapitals() --------------------------*/
   /**
    * Retrieves all capitals from the states table.
    *
    * @return String array of capitals.
    */
   public synchronized String[] getCapitals() {
      String[] capitals = {};
      SQLiteDatabase db = getReadableDatabase();
      Cursor cursor = db.query(STATES, new String[]{COL_CAPITAL}, null, null, null, null, null);
      if (cursor.moveToFirst()) {
         capitals = new String[cursor.getCount()];
         int i = 0;
         do {
            capitals[i] = cursor.getString(0);
            i++;
         } while (cursor.moveToNext());
      }
      cursor.close();
      db.close();
      return capitals;
   }

   /*--------------------------- getRandomeStates() -------------------------*/
   /**
    * Retrieves random states and places them in a map<state, capital>
    * data structure
    *
    * @param count Number of max number of records to retrieve.
    * @return Map with State as the key, and capital as the value.
    */
   public synchronized Map<String, String> getRandomStates(int count) {
      Map<String, String> states_map = new HashMap<String, String>(count);
      SQLiteDatabase db = getReadableDatabase();
      Cursor cursor = db.query(STATES, STATES_COLUMNS(), null, null, null, null, "random()", String.valueOf(count));
      if (cursor.moveToFirst()) {
         do {
            states_map.put(cursor.getString(0), cursor.getString(1));
         } while (cursor.moveToNext());
      }
      cursor.close();
      db.close();

      return states_map;
   }

   /*----------------------------- STATE_COLUMNS() -------------------------*/
   /**
    * PRIVATE STATIC method to return a string array of the State and capital
    * Strings.
    *
    * @return String array of {COL_STATE, COL_CAPITAL}
    */
   private static final String[] STATES_COLUMNS() {
      return (new String[]{COL_STATE, COL_CAPITAL});
   }

   /*-------------------------- USER_COLUMNS() -----------------------------*/
   /**
    * PRIVATE STATIC method to return a string array of the required user
    * information to retrieve from the database in order to make a User object.
    *
    * @return String array of {KEY_STRING_ID, COL_USERNAME}
    */
   private static final String[] USER_COLUMNS() {
      return (new String[]{KEY_USER_ID, COL_USERNAME});
   }
}
