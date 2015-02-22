/*****************************************************************************\
|                                                                             |
|    Author :  Galo Paz <galopaz@outlook.com>                                 |
|      Date :  Jul 14, 2013                                                   |
|      Name :  PlayGame.java                                                  |
| Objective :  Provide a layout and functions to allow user to play the game  |
|              and record scores to the database.                             |
|     Extra :  None                                                           |
|                                                                             |
\*****************************************************************************/
package com.cs211d.a5; ///HOMEWORK 5

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

/**
 * Author : Galo Paz <galopaz@outlook.com>
 * Date   : Jul 14, 2013
 * Purpose: The game.  Shows ten states, which you need to type in its
 *          associated capital.
 **/
public class PlayGame extends Activity implements OnClickListener
{

	private static final String TAG = "PlayGame";
	private Game mGame;
	private Map<String,String> mStateMap;
	private String[] mKeyStates;
	private Chronometer mTimer;
	private long mElapsedTime;
	//References to views in the game layout
	private ArrayList<TextView> mStateViews;
	private ArrayList<AutoCompleteTextView>mCapitalViews;
	
	/*------------------------onCreate()------------------------*/
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);
		StateCapitalDBHandler handler = new StateCapitalDBHandler(this);
		Bundle data;
		String[] capitals;        
		// Get user from intent
		data = getIntent().getExtras();
		if(data != null)
		{
			mGame = new Game((User)data.getParcelable("user"));
		}
		TextView username = (TextView)findViewById(R.id.playing_username);
		username.setText(mGame.getUserAlias());
		mTimer = (Chronometer)findViewById(R.id.time);
		mElapsedTime = 0;
		mStateMap = handler.getRandomStates(10);
		capitals = handler.getCapitals();
		mStateViews = new ArrayList<TextView>();
		mCapitalViews = new ArrayList<AutoCompleteTextView>();

		// REFLECTION
		Field[] field = R.id.class.getFields();
		try
		{
			// Sort fields by name
			for(Field f : field)
			{
				if(f.getName().startsWith("state"))
					mStateViews.add((TextView)findViewById(f.getInt(null)));
				else if (f.getName().startsWith("capital"))
					mCapitalViews.add((AutoCompleteTextView)
							findViewById(f.getInt(null)));
			}
		}catch(Exception e)
		{
			Log.e(TAG, e.getMessage());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this
				,android.R.layout.select_dialog_item, capitals);
		mKeyStates = mStateMap.keySet().toArray(new String[mStateMap.size()]);
		for(int i = 0; i < mCapitalViews.size(); i++)
		{
			mCapitalViews.get(i).setAdapter(adapter);
			mCapitalViews.get(i).setThreshold(4);
			mCapitalViews.get(i).setHint(Html.fromHtml(
					"<small>Capital of "+mKeyStates[i] + "</small>"));
			mStateViews.get(i).setText(mKeyStates[i]);
		}          
	}

	/*----------------------------- startTime() -----------------------------*/
	/**
	 * Starts the timer
	 */
	private void startTimer(){mTimer.start();}

	/*----------------------------- stopTimer() -----------------------------*/
	/**
	 * stops timer and returns elapsed time.
	 * 
	 * @return 
	 */
	private long stopTimer()
	{
		mTimer.stop();       
		return SystemClock.elapsedRealtime() - mTimer.getBase();
	}

	/*------------------------------ onPause() ------------------------------*/
	/**
	 * Pauses time and records elapsed time.
	 */
	@Override
	protected void onPause()
	{
		mElapsedTime = stopTimer();
		super.onPause();
	}

	/*------------------------------- onResume() ----------------------------*/
	/**
	 * Starts timer and set its base to include elapsed time.
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		startTimer();
		mTimer.setBase(SystemClock.elapsedRealtime()-mElapsedTime);
	}

	/*------------------------------ submit() -------------------------------*/
	/**
	 * Button handler for the submit button, stops time, records games to
	 * database, displays the score to the user, sets the button text to 'Return'
	 * and sets the button handler to the 'onClick' method of this class.
	 * 
	 * @param v The Button view that was clicked.
	 */
	public void submit(View v)
	{
		long time = stopTimer();
		v.setOnClickListener(this);
		((Button)v).setText(getResources().getString(R.string.btn_return));
		boolean[] scores = gradeQuiz();
		int t = 0;
		for(boolean b : scores)
		{
			if(b)
				t++;
		}
		displayScores(t,scores.length);
		recordScores(scores, time);
	}

	/*--------------------------- displayScores() ---------------------------*/
	/**
	 * Displays scores to user.
	 * 
	 * @param correct number of correct answers
	 * @param total number of total questions.
	 */
	private void displayScores(int correct, int total)
	{
		TextView score = (TextView)findViewById(R.id.score);
		String s = correct+"/"+total+"\n"+(correct*10)+"pts";
		score.setText(s);
		score.setVisibility(View.VISIBLE);
	}

	/*------------------------------ onClick() -----------------------------*/
	/**
	 * On click listener to return to the main screen.  But can always press the
	 * back button to cancel scores before the user submits answers for scoring.
	 * 
	 * @param v Button that was clicked on
	 */
	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
		case(R.id.btn_submit):
			this.finish();
		break;
		}
	}

	/*----------------------------- recordScores() --------------------------*/
	/**
	 * Records scores to Database.
	 * @param scores
	 * @param time 
	 */
	private void recordScores(boolean[] scores, long time)
	{
		int correct = 0;
		for(boolean b : scores)
		{
			if(b)
				correct++;
		}
		mGame.setScore(correct, scores.length);
		mGame.setTime(time);
		StateCapitalDBHandler handler = new StateCapitalDBHandler(this);
		handler.insertGame(mGame);
	}

	/*---------------------------- gradeQuiz -------------------------------*/
	/**
	 * Grades the answers given, highlights the fields blue if correct, red if 
	 * incorrect and disables focus to any of the text fields.
	 * 
	 * @return true or false for each answer in same order as _keystates
	 */
	private boolean[] gradeQuiz()
	{
		boolean[] b = new boolean[mKeyStates.length];
		for(int i = 0; i < mKeyStates.length; i++)
		{
			String input, capital, state;
			String[] parts;
			state = this.mStateViews.get(i).getText().toString();
			capital = ((String)this.mStateMap.get(state)).toLowerCase();
			parts = this.mCapitalViews.get(i).getText().toString().trim()
					.split("[ \t][ \t]");
			input = "";
			for(String s : parts)
			{
				if(!s.trim().isEmpty())
					input += s+" ";
			}
			input = input.trim();
			mCapitalViews.get(i).setText(input);

			if(capital.equalsIgnoreCase(input))
				b[i] = true;
			else
				b[i] = false;
			mCapitalViews.get(i).setClickable(false);
			mCapitalViews.get(i).setFocusable(false);
			markAnswer(mStateViews.get(i), mCapitalViews.get(i), b[i]);
		}
		return b;
	}

	/*------------------------------ markAnswers() --------------------------*/
	/**
	 * Handler method to mark correct answers blue and incorrect answers false.
	 * 
	 * @param stateview TextView with the state name.
	 * @param capitalview AutoCompleteTextView with the user input capital name
	 * @param is_correct Boolean, false to mark red, true to mark blue.
	 */
	private void markAnswer(TextView stateview, 
			AutoCompleteTextView capitalview, boolean is_correct)
	{
		if(is_correct)
			capitalview.setBackgroundColor(
					getResources().getColor(R.color.flag_blue));
		else
			capitalview.setBackgroundColor(
					getResources().getColor(R.color.flag_red));
	}
}
