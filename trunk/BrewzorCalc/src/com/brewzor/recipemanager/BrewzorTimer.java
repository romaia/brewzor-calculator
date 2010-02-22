package com.brewzor.recipemanager;

import com.brewzor.calculator.OptionsMenuHandler;
import com.brewzor.calculator.R;
import com.brewzor.recipemanager.DBAdapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BrewzorTimer extends Activity {

	private SharedPreferences prefs;
	
     protected static final int DEFAULTSECONDS = 60 * 60;  // 60 MInutes
     /* The value of these IDs is random!
      * they are just needed to be recognized */
     protected static final int SECONDPASSEDIDENTIFIER = 0x1337;
     protected static final int GUIUPDATEIDENTIFIER = 0x101;
     protected static final int PIZZA_NOTIFICATION_ID = 0x1991;
     
     /** is the countdown running at the moment ?*/
     protected boolean started = false;
     protected boolean running = false;
     
     protected int myStartTime = 0;
     
     /** Seconds passed so far */
     protected int mySecondsPassed = 0;
     
     /** Seconds to be passed totally */
     protected int mySecondsTotal = DEFAULTSECONDS;
     
     /* Thread that sends a message
      * to the handler every second */
     Thread myRefreshThread = null;
     
     private TextView timerView;
     private Button startPauseButton;
     private Button resetButton;
     
     DBAdapter db = new DBAdapter(this);
     
     // One View is all that we see.
     //BrewzorTimerView myPizzaView = null;
     
     /* The Handler that receives the messages
      * sent out by myRefreshThread every second */
     Handler myTimerViewUpdateHandler = new Handler(){
          /** Gets called on every message that is received */
          // @Override
          public void handleMessage(Message msg) {
               switch (msg.what) {
                    case BrewzorTimer.SECONDPASSEDIDENTIFIER:
                         // We identified the Message by its What-ID
                         if (running) {
                              // One second has passed
                              mySecondsPassed++;
                              if(mySecondsPassed == mySecondsTotal){
                            	  Toast.makeText(BrewzorTimer.this, R.string.timer_notification_text, Toast.LENGTH_SHORT).show(); 
                            	  running = false;
                              }
                         }
                         // No break here --> runs into the next case
                    case BrewzorTimer.GUIUPDATEIDENTIFIER:
                         // Redraw our Pizza !!
                         //myPizzaView.updateSecondsPassed(mySecondsPassed);
                         //myPizzaView.updateSecondsTotal(mySecondsTotal);
                         //myPizzaView.invalidate();
                    	int secondsLeft = mySecondsTotal - mySecondsPassed;
                    	//String timeDisplayString = "" + (secondsLeft / 60) + ":" + String.format("%02d",(secondsLeft % 60));
                    	String timeDisplayString = "" + (secondsLeft / 60);
                        timerView.setText(timeDisplayString); 
                    	break;
               }
               super.handleMessage(msg);
          }
     };
     
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.timer);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        
        timerView = (TextView) findViewById(R.id.timer);
                
        startPauseButton = (Button) findViewById(R.id.startPauseButton);
        startPauseButton.setOnClickListener(new OnClickListener() {
   			@Override
   			public void onClick(View view) {
   				//toggleButton();
   				running = !running; // START / PAUSE
   			}
        });

        resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new OnClickListener() {
   			@Override
   			public void onClick(View view) {
   				resetTimer();
   			}
        });

        if (icicle != null) {
        	// Restore a saved state, if one exists
        	running = icicle.getBoolean("running");
        	mySecondsPassed = icicle.getInt("mySecondsPast");
        	mySecondsTotal = icicle.getInt("mySecondsTotal");
        }
       
        
        myRefreshThread = new Thread(new secondCountDownRunner());
        myRefreshThread.start();
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
	  // Save UI state changes to the savedInstanceState.
	  // This bundle will be passed to onCreate if the process is
	  // killed and restarted.
	  savedInstanceState.putBoolean("running", running);
	  savedInstanceState.putInt("mySecondsPast", mySecondsPassed);
	  savedInstanceState.putInt("mySecondsTotal", mySecondsTotal);
	  super.onSaveInstanceState(savedInstanceState);
    }
   
    
    @Override
     public boolean onCreateOptionsMenu(Menu menu) {
		return OptionsMenuHandler.createTimerMenu(this, menu);
     }

    
	/* Handles item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return OptionsMenuHandler.showMenu(this, item);
	}
	

     @Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId()){
        case 0:
        	resetTimer();
        	return true;
	   }
	   return super.onMenuItemSelected(featureId, item);
	}

	@Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
          Message m = new Message();
          m.what = BrewzorTimer.GUIUPDATEIDENTIFIER;
          
          switch(keyCode){
               case KeyEvent.KEYCODE_DPAD_UP:
                    mySecondsTotal += 60; // One minute later
                    break;
               case KeyEvent.KEYCODE_DPAD_DOWN:
                    mySecondsTotal -= 60; // One minute earlier
                    break;
               case KeyEvent.KEYCODE_DPAD_CENTER:
                    running = !this.running; // START / PAUSE
                    break;
               case KeyEvent.KEYCODE_DPAD_LEFT:
                    mySecondsTotal += 60 * 15; // One second later
                    break;
               case KeyEvent.KEYCODE_DPAD_RIGHT:
                    mySecondsTotal -= 60 * 15; // One second earlier
                    break;
               default:
                    return super.onKeyDown(keyCode, event);
          }
          
          myTimerViewUpdateHandler.sendMessage(m);
          return true;
     }
	
	private void resetTimer() {
        // Reset the counter and stop it
        mySecondsTotal = BrewzorTimer.DEFAULTSECONDS;
        mySecondsPassed = 0;
        running = false;
	}
   
     class secondCountDownRunner implements Runnable{
          // @Override
          public void run() {
               while(!Thread.currentThread().isInterrupted()){
                    Message m = new Message();
                    m.what = BrewzorTimer.SECONDPASSEDIDENTIFIER;
                    BrewzorTimer.this.myTimerViewUpdateHandler.sendMessage(m);
                    try {
                         Thread.sleep(1000);
                    } catch (InterruptedException e) {
                         Thread.currentThread().interrupt();
                    }
               }
          }
    }
}
