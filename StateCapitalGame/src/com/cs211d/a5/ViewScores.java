/*****************************************************************************\
|                                                                             |
|    Author :  Galo Paz <galopaz@outlook.com>                                 |
|      Date :  Jul 14, 2013                                                   |
|      Name :  ViewScores.java                                                |
| Objective :  Show top players and their scores and times.  Provide a button |
|              to return to the StartScreen activity.                         |
|     Extra :  None                                                           |
|                                                                             |
\*****************************************************************************/

package com.cs211d.a5; ///HOMEWORK #5

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class ViewScores extends Activity 
{

   /*---------------------------onCreate()------------------------------*/
   @Override
   public void onCreate(Bundle b) 
   {
      super.onCreate(b);
      setContentView(R.layout.scores_layout);
      
      // Retrieve the top game scores
      StateCapitalDBHandler handler = new StateCapitalDBHandler(this);
      Game[] top_games = handler.getTopGames(getResources()
              .getInteger(R.integer.num_top_players));
      //add games to the screen
      _displayGames(top_games);
   }
   
   /*------------------------- _displayGames() ---------------------------*/
   /**
    * Method called to display the scores for an array of games.  Games will
    * be displayed in the order of their positions inside the array.
    * 
    * @param games Array of Game objects
    */
   private void _displayGames(Game[] games)
   {
      if(games==null)
         return;
      TableLayout table = (TableLayout)findViewById(R.id.tableLayout1);
      for(int i = 0; i < games.length; i++)
      {
         addGameToTable(table, games[i], i+1);
      }
      
   }
   
   /*-------------------- _formatTimeFromMilliseconds() --------------------*/
   /**
    * Formats and returns a string of the input time converted from milliseconds
    * to the format "##m ##.###s" when the time is a value of at least one 
    * minute, otherwise returns a string of the format "##.###s".
    * @param time
    * @return a formatted time between the values "00.000s" and "99m 99.999s"
    */
   private String formatTimeFromMilliseconds(long time)
   {
         long mins, secs, msec;
         msec = time;
         
         if(msec != 0)
            secs = msec/1000;
         else
            secs = 0;
         
         if(secs != 0)
            mins = secs / 60;
         else
            mins = 0;
         
         if(mins > 99)
         {
            mins = 99;
            secs = 99;
            msec = 999;
         }else
         {
            msec = msec - (secs * 1000);
            secs = secs - (mins * 60);
         }
         
         String sM, sS, sMS;
         // set milliseconds field to "sss" format
         if(msec < 10)
            sMS = "00"+msec;
         else if (msec < 100)
            sMS = "0"+msec;
         else
            sMS = ""+msec;
         
         // set seconds field to "SS" format
         if(secs < 10)
            sS = "0"+secs;
         else
            sS = ""+secs;
         
         // set minutes field to "mm" format
         if(mins < 10)
            sM = "0"+mins;
         else
            sM = ""+mins;
         String out = new String();
         if(mins > 0)
            out = sM+"m ";
         out += sS+"."+sMS+"s";
         // return time as similar format to "00m 00.000s"
         return out;
   }
   
   /*----------------------- _addGameToTable() --------------------------*/
   /**
    * Adds game scores to the bottom of a table view.
    * 
    * @param table  The TableView to append to
    * @param game  Game object to add
    * @param number  position of the game scores compared to all scores.
    */
   private void addGameToTable(TableLayout table, Game game, int number)
   {
      
      String pos, player, score, correct, total, ratio, time;
      
      correct = String.valueOf(game.getNumCorrect());
      total = String.valueOf(game.getNumTotal());
      
      // Set container strings for the game scores and other values
      pos = String.valueOf(number);
      player = game.getUserAlias();
      score = String.valueOf(game.getNumCorrect() * 10);
      ratio = correct + "/" + total;
      time = formatTimeFromMilliseconds(game.getTime());
      String[] fields = {pos, player, score, ratio, time};
      
      // Create new row and set fields
      TableRow row = new TableRow(this);
      row.setLayoutParams(new LayoutParams( LayoutParams.WRAP_CONTENT
              ,LayoutParams.WRAP_CONTENT));
      
      TextView[] tviews = new TextView[5];
      for(int i = 0; i < 5; i++)
      {
         tviews[i] = new TextView(this);
         tviews[i].setText(fields[i]);
         // Alternate 2 colors with each consecutive row.
         if(number%2 == 0)
            tviews[i].setTextColor(Color.YELLOW);
         else
            tviews[i].setTextColor(Color.MAGENTA);
         tviews[i].setGravity(Gravity.CENTER);
         
         // add text views to the row
         row.addView(tviews[i]);
      }
      
      // append row to the bottom of the table
      table.addView(row, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT
              , LayoutParams.WRAP_CONTENT));
   }
   
   /*--------------------------- returnToMain() ---------------------------*/
   /**
    * onClick handler for the 'Return' button, to return to the start screen.
    * 
    * @param V Button that was clicked.
    */
   public void returnToMain(View V)
   {
      finish();
   }

}
