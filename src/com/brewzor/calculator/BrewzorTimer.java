package com.brewzor.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BrewzorTimer extends Activity {

     protected static final int DEFAULTSECONDS = 60 * 12;  // 12 MInutes
     /* The value of these IDs is random!
      * they are just needed to be recognized */
     protected static final int SECONDPASSEDIDENTIFIER = 0x1337;
     protected static final int GUIUPDATEIDENTIFIER = 0x101;
     protected static final int PIZZA_NOTIFICATION_ID = 0x1991;
     
     /** is the countdown running at the moment ?*/
     protected boolean running = false;
     /** Seconds passed so far */
     protected int mySecondsPassed = 0;
     /** Seconds to be passed totally */
     protected int mySecondsTotal = DEFAULTSECONDS;
     
     /* Thread that sends a message
      * to the handler every second */
     Thread myRefreshThread = null;
     // One View is all that we see.
     BrewzorTimerView myPizzaView = null;
     
     /* The Handler that receives the messages
      * sent out by myRefreshThread every second */
     Handler myPizzaViewUpdateHandler = new Handler(){
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
                              }
                         }
                         // No break here --> runs into the next case
                    case BrewzorTimer.GUIUPDATEIDENTIFIER:
                         // Redraw our Pizza !!
                         myPizzaView.updateSecondsPassed(mySecondsPassed);
                         myPizzaView.updateSecondsTotal(mySecondsTotal);
                         myPizzaView.invalidate();
                         break;
               }
               super.handleMessage(msg);
          }
     };
     
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
               
        myPizzaView = new BrewzorTimerView(this);
        myPizzaView.updateSecondsTotal(BrewzorTimer.DEFAULTSECONDS);
        setContentView(myPizzaView);
        
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
//        menu.add(0, 0, 0, getResources().getString(R.string.menu_reset));
        menu.add(0, 0, 0, "reset");
    	return super.onCreateOptionsMenu(menu);          
     }

     @Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId()){
        case 0:
             // Reset the counter and stop it
             mySecondsTotal = BrewzorTimer.DEFAULTSECONDS;
             mySecondsPassed = 0;
             running = false;
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
          
          myPizzaViewUpdateHandler.sendMessage(m);
          return true;
     }
   
     class secondCountDownRunner implements Runnable{
          // @Override
          public void run() {
               while(!Thread.currentThread().isInterrupted()){
                    Message m = new Message();
                    m.what = BrewzorTimer.SECONDPASSEDIDENTIFIER;
                    BrewzorTimer.this.myPizzaViewUpdateHandler.sendMessage(m);
                    try {
                         Thread.sleep(1000);
                    } catch (InterruptedException e) {
                         Thread.currentThread().interrupt();
                    }
               }
          }
    }
}
