/*****************************************************************************\
|                                                                             |
|    Author :  Galo Paz <galopaz@outlook.com>                                 |
|      Date :  Jul 18, 2013                                                   |
|      Name :  StateListActivity.java                                         |
| Objective :  Provide an activity, utilizing a List View, a database, and a  |
|              simple cursor adapter, that lists states such that when the    |
|              the user clicks on the state name, its capital will be         |
|              displayed to the user at the bottom of the screen.             |
|     Extra :  None                                                           |
|                                                                             |
\*****************************************************************************/

package com.cs211d.a6; //Homework #6

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class StateListActivity extends ListActivity 
                              implements OnItemClickListener
{
   ///// PRIVATE MEMBER VARIABLES /////
   private StatesDBHandler mDBhandler;
   private TextView mCapitalTv;
   private ListView mStatesTv;
   private Long mSelectedItem;
   //////// END DECLARATIONS //////////
   
   /*-------------------------------- onCreate() ----------------------------*/
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.state_list_layout);
      initialize();
   }
   
   /*----------------------------- _initialize() ---------------------------*/
   /**
    * Called by onCreate(), initializes the ListView with states from the 
    * database.
    */
   private void initialize()
   {
      mCapitalTv = (TextView)findViewById(R.id.capital_field);
      mStatesTv = getListView();
      mDBhandler = new StatesDBHandler(this);
      mSelectedItem = null;
      setlistview();
   }
   
   /*----------------------------- _setListView() -------------------------*/
   /**
    * Sets up a simple cursor adapter pointing to a table populated with state 
    * id and states.
    */
   private void setlistview()
   {
      Cursor cursor = mDBhandler.getStatesCursor();
      SimpleCursorAdapter db_adapter = new SimpleCursorAdapter(
              this,
              android.R.layout.simple_list_item_1,
              cursor,
              new String[] {StatesDBHandler.COL_STATE},
              new int[] {android.R.id.text1}
      );
      
      mStatesTv.setAdapter(db_adapter);
      mStatesTv.setOnItemClickListener(this);
   }
   
   /*----------------------------- onItemClick() ---------------------------*/
   /**
    * Handler for when an item in the list is clicked.  Satisfies the
    * implementation of OnClickListener abstract class.
    * 
    * @param av Adapter View associated with the list.
    * @param view the view that was clicked, notably the Text View with the 
    *             state name.
    * @param position the position of the clicked item on the list.
    * @param id the id of state associated with the item clicked.
    */
   public void onItemClick(AdapterView<?> av, View view, int position, long id)
   {
      // Don't query the database if item is already selected.
      if((mSelectedItem != null && !mSelectedItem.equals(id)) ||
              mSelectedItem == null)
      {
         // Search database by state id
         mCapitalTv.setText(mDBhandler.getCapital(id));
         mSelectedItem = id;
      }
   }
}
