/*****************************************************************************\
|                                                                             |
|    Author :  Galo Paz <galopaz@outlook.com>                                 | 
|      Date :  Jul 11, 2013                                                   |
|      Name :  Game.java                                                      |
|                                                                             |
| Objective :  A container to store and retrieve values of a game that has    |
|              or is being played.                                            |
|                                                                             |
|     Extra :  None                                                           |
|                                                                             |
\*****************************************************************************/
package com.cs211d.a5; ///HOMEWORK #5

public class Game 
{
   ///// PRIVATE MEMBERS //////
   private Integer _gameid;
   private User _user;
   private Long _time;
   private Integer _correct;
   private Integer _total;
   //// END PRIVATE MEMBERS ////
   
   /*------------------------------- Game() ------------------------------*/
   /**
    * Used when accessing a completed game from the database and initializing
    * the Game container for that record information.
    * 
    * @param game_id
    * @param user User that played the game
    * @param time Time in milliseconds that the player took to complete1 game.
    * @param correct The number of correct answers.
    * @param total Total number of questions asked.
    */
   public Game(int game_id, User user, long time, int correct, int total)
   {
      _gameid = game_id;
      _user = user;
      _time = time;
      _correct = correct;
      _total = total;
   }
   
   /*---------------------------- Game() ------------------------------*/
   /**
    * Constructor to initialize a game when the user is known.
    * 
    * @param user A User object who is playing the game.
    */
   public Game(User user)
   {
      _gameid = null;
      _user = user;
      _time = null;
      _correct = null;
      _total = null;
   }
   
   ////////////////////////////// SETTERS //////////////////////////////////
   /*--------------------------- setGameId() -----------------------------*/
   public void setGameId(int id) {_gameid = id;}
   
   /*---------------------------- setTime() ------------------------------*/
   public void setTime(long time) {_time = time;}
   
   /*---------------------------- setUser() ------------------------------*/
   public void setUser(User user) {_user = user;}
   
   /*---------------------------- setScore() -----------------------------*/
   public void setScore(int correct, int total)
   {
      _correct = correct;
      _total = total;
   }
   //////////////////////////// END SETTERS /////////////////////////////////
   
   ////////////////////////////// GETTERS ///////////////////////////////////
   /*--------------------------- getGameId() ------------------------------*/
   public int getGameId() {return _gameid;}
   
   /*---------------------------- getTime() -------------------------------*/
   public long getTime() {return _time;}
   
   /*-------------------------- getUserId() -------------------------------*/
   public int getUserId() {return _user.getUserId();}
   
   /*------------------------- getUserAlia() ------------------------------*/
   public String getUserAlias() {return _user.getAlias();}
   
   /*------------------------ getNumCorrect() -----------------------------*/
   public int getNumCorrect() {return _correct;}
   
   /*----------------------- getNumIncorrect() ----------------------------*/
   public int getNumIncorrect() {return (_total - _correct);}
   
   /*------------------------- getNumTotal() ------------------------------*/
   public int getNumTotal() {return _total;}
   ////////////////////////////// END GETTERS ///////////////////////////////
   
   /**
    * Calculates the percentage of correct answers and returns an integer 
    * representation between 0 and 100;
    * 
    * @return percentage of correct to total as a percentage between 0 an 100.
    * 
    */
   public float getPercentageCorrect()
   {
      if(hasCompleted())
         return (float)(((1.0* _correct)/_total)*100);
      else
         return -1;
   }
   
   /*--------------------------- hasCompleted() ----------------------------*/
   /**
    * Checks conditions to return a boolean value of whether this game has been
    * completed or not.
    * 
    * @return true if it is completed, false otherwise
    */
   public boolean hasCompleted()
   {
      if(_user != null && _time != null && _correct != null && _total != null )
         return true;
      else
         return false;
   }
}
