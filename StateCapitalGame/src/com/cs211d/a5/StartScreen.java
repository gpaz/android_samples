/*****************************************************************************\
|                                                                             |
|    Author :  Galo Paz <galopaz@outlook.com>                                 | 
|      Date :  Jul 11, 2013                                                   |
|      Name :  StartScreen.java                                               |
|                                                                             |
| Objective :  The Main activity after the splash screen that allows for the  |
|              user to click a button to view the top scores or to type a     |
|              user name and click a button to play the game.  Uses intents   |
|              between activities and utilizes an SQLite database.            |
|                                                                             |
|     Extra :  None                                                           |
|                                                                             |
\*****************************************************************************/
package com.cs211d.a5; ///HOMEWORK #5

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class StartScreen extends Activity implements OnKeyListener
{
   ////////// MEMBER VARIABLES /////////////
   private User _user;
   private AutoCompleteTextView _userfield;
   ///////////// END MEMBERS ///////////////
   
   /*---------------------------- onCreate() ------------------------------*/
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.startscreen);
      _user = null;
      
      Bundle iData = getIntent().getExtras();
      
      //Set private member variables to point to view widgets
      _userfield = (AutoCompleteTextView)findViewById(R.id.edit_username);
      //end
      _userfield.setOnKeyListener(this);
      StateCapitalDBHandler handler = new StateCapitalDBHandler(this);
      String[] possible_users = handler.getUsernames();
      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this
              ,android.R.layout.select_dialog_item, possible_users);
      _userfield.setAdapter(adapter);
      if(iData!= null && savedInstanceState == null)
      {
         _user = (User)iData.getParcelable("last_user");
         if(_user != null)
            _userfield.setText(_user.getAlias());
      }else
      {
         _user = (User)savedInstanceState.getParcelable("user");
         _userfield.setText(savedInstanceState.getString("textfield"));
      }
   }
   
   /*---------------------- onSaveInstanceState() --------------------------*/
   /**
    * Save state when changing configuration layouts.
    * @param outState 
    */
   @Override
   public void onSaveInstanceState(Bundle outState)
   {
      outState.putString("textfield", _userfield.getText().toString());
      outState.putParcelable("user", _user);
      super.onSaveInstanceState(outState);
   }
   
   /*------------------------------ goPlay() ------------------------------*/
   /**
    * Button handler for the 'Play' button.  Sends user to the PlayGame
    * activity.  Retrieves a user from the database whose name matches the 
    * the text in the username text view or creates a new user in the database
    * with that name if none exists and sends over that information in a User
    * to the PlayGame activity through an intent.  Does not finish, is on pause
    * in the background waiting for user to come back to it. 
    * 
    * @param v the Button View that was clicked.
    */
   public void goPlay(View v)
   {
      Intent intent = null;
      
      String username = _userfield.getText().toString().trim();
      //Set User
      _setUser(username);
      if(_user != null)
      {
         intent = new Intent(StartScreen.this, PlayGame.class);
         intent.putExtra("user", _user);
         this.startActivity(intent);
      }
   }
   
   /*----------------------------- viewScores() ---------------------------*/
   /**
    * Sends user to the activity that shows the user the top games, players, 
    * and their scores through an intent.  Puts this activity into sleep waiting
    * for user to come back.
    * 
    * @param V Button view that was clicked.
    */
   public void viewScores(View V)
   {
      Intent intent = new Intent(this, ViewScores.class);
      startActivity(intent);
   }
   
   /*------------------------------ _setUser() ------------------------------*/
   /**
    * PRIVATE
    * Gets user that matches input and sets that user it as the current user in
    * the database.  
    * This will then be used by this activity the next time it is called in 
    * order to populate the user name TextView with the last user that played 
    * the game.
    * @param username String of the input username.
    */
   private void _setUser(String username)
   {
      StateCapitalDBHandler handler = new StateCapitalDBHandler(this);
      if(!username.isEmpty())
      {
         if(_user != null)
         {
            if(!_user.getAlias().equals(username))
            {
               _user = _queryForUser(username);
            }
         }else 
         {
            _user = _queryForUser(username);
         }
         handler.setAsCurrentUser(_user);
      }
      //else dont play if no username is supplied
   }
   
   /*--------------------------- _queryForUser() --------------------------*/
   /**
    * Creates if not exists and gets the user matching the input username string
    * and returns it wrapped in a User object.
    * 
    * @param username String of the input username to query the database.
    * @return User object with the user information found in the database.
    */
   private User _queryForUser(String username)
   {
      StateCapitalDBHandler db = new StateCapitalDBHandler(this);
      User user = db.getUser(username);
      if(user == null)
         user = db.getUser(username);
      return user;
   }
   
   /****************************** onKey() ********************************/
   /**
    * Key event handler for the textview, if user presses enter, the user will
    * be sent to the PlayGame activity if text is present in the textview.
    * 
    * @param v The view the keyevent originates from.
    * @param keyCode the key pressed.
    * @param ke   the key event.
    * @return true if key did anything significant.
    */
   public boolean onKey(View v, int keyCode, KeyEvent ke)
   {
      boolean worked = false;
      String username = _userfield.getText().toString().trim();
      if(ke.getAction() == KeyEvent.ACTION_DOWN 
              && keyCode == KeyEvent.KEYCODE_ENTER 
              && !username.isEmpty())
      {
         worked = true;
         goPlay(v);
      }
      return worked;
   }
}
