package com.graemehill.phoneclock;


import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends Activity {

	SegmentDigits viewDigits;
	ClockView clockView;
	ActionBar bar;
	// Default to 12 hour clock
	public boolean is24HourClock = false;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		clockView = new ClockView(this);
        setContentView(clockView);
        clockView.requestFocus();
        
		bar = this.getActionBar();
		bar.show();
        
        Timer t = new Timer();
        t.schedule(new MyClockTimer(), 3000, 500);
	}
	
	/** Generates Help Menu */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	/** Menu Clicks */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_about:
	            showHelpScreen();
	            return true;
	        case R.id.menu_set24:
	            this.is24HourClock = true;
	            return true;
	        case R.id.menu_set12:
            this.is24HourClock = false;
            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void showHelpScreen() {
		AlertDialog dialog =  new AlertDialog.Builder(
                this).create();
		dialog.setTitle("About");
		dialog.setMessage("Designed by Graeme Hill (c) 2013");
		dialog.setButton("OK", new DialogInterface.OnClickListener() {
        	 
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog
                // closed
            }
        });		
		dialog.show();
	}
	

    class MyClockTimer extends TimerTask {

		@Override
		public void run() {
            clockView.post(new Runnable() {
                public void run() {
                	clockView.is24Display(is24HourClock);
        			clockView.refreshTime();
                }
            });
		}
    	
    }
}
