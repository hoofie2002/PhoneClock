package com.graemehill.phoneclock;


import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	SegmentDigits viewDigits;
	ClockView clockView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//setContentView(R.layout.main);
        //viewDigits = new SegmentDigits();
		
		clockView = new ClockView(this);
        setContentView(clockView);
        clockView.requestFocus();
        
        Timer t = new Timer();
        t.schedule(new MyTimer(), 3000, 500);
	}

	/*
    private void drawTime() {
        Calendar cal = Calendar.getInstance(); 

        int minute = cal.get(Calendar.MINUTE);
              //12 hour format
        int hour = cal.get(Calendar.HOUR);
    	
    	viewDigits.drawDigitBlock(hour, new ImageView[] {	
    			(ImageView)findViewById(R.id.hour1), 
    			(ImageView)findViewById(R.id.hour2), 
    			});

    	viewDigits.drawDigitBlock(minute, new ImageView[] {	
    			(ImageView)findViewById(R.id.mins1), 
				(ImageView)findViewById(R.id.mins2), 
				});
    }
    */
    
    class MyTimer extends TimerTask {

		@Override
		public void run() {
            clockView.post(new Runnable() {
                public void run() {
        			clockView.refreshTime();
                }
            });
		}
    	
    }


}
