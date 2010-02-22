package com.brewzor.recipemanager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;


public class BrewzorTimerView extends View{
     // ===========================================================
     // Fields
     // ===========================================================
          
     // Set startup-values
     protected int mySecondsPassed = 0;
     protected int mySecondsTotal = 0;
     
     // Our Painting-Device (Pen/Pencil/Brush/Whatever...)
     protected final Paint myCountDownTextPaint = new Paint();
     protected final Paint myPizzaTimeTextPaint = new Paint();

     // ===========================================================
     // Constructors
     // ===========================================================

     public BrewzorTimerView(Context context) {
          super(context);
          //setBackgroundResource(R.drawable.pizza);
          
          // Black text for the countdown
          myCountDownTextPaint.setARGB(255, 255, 60, 10);
          myCountDownTextPaint.setTextSize(110);
          myCountDownTextPaint.setFakeBoldText(true);
          
          // Orange text for the IT PIZZA TIME
          myPizzaTimeTextPaint.setARGB(255, 255, 60, 10);
          myPizzaTimeTextPaint.setTextSize(110);
          myPizzaTimeTextPaint.setFakeBoldText(true);
          
     }
     
     // ===========================================================
     // onXYZ(...) - Methods
     // ===========================================================
     
     @Override
     protected void onDraw(Canvas canvas) {
          /* Calculate the time left,
           * until our pizza is finished. */
          int secondsLeft = mySecondsTotal - mySecondsPassed;
          
          // Check if pizza is already done
          if(secondsLeft <= 0){
               /* Draw the "! PIZZA !"-String
                *  to the middle of the screen */
               String itIsPizzaTime = "pizza time";
               canvas.drawText(itIsPizzaTime,
                                        10, (getHeight() / 2) + 30,
                                        myPizzaTimeTextPaint);             
          }else{

        	  String timeDisplayString;
//               if(secondsLeft > 60) // Show minutes
                    timeDisplayString = "" + (secondsLeft / 60) + ":" + String.format("%02d",(secondsLeft % 60));
//               else // Show seconds when less than a minute
//                    timeDisplayString = "" + secondsLeft;
               
               // Draw the remaining time.
               canvas.drawText(timeDisplayString,
                         getWidth() / 2 - (30 * timeDisplayString.length()),
                         getHeight()/ 2 + 30,
                         myCountDownTextPaint);
          }
     }
     // ===========================================================
     // Getter & Setter
     // ===========================================================
     
     public void updateSecondsPassed(int someSeconds){
          mySecondsPassed = someSeconds;
     }
     
     public void updateSecondsTotal(int totalSeconds){
          mySecondsTotal = totalSeconds;
     }
}
