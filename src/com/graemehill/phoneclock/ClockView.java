package com.graemehill.phoneclock;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class ClockView extends View {
	
	Paint clockFace = null;
	Paint hourHand = null;
	Paint minHand = null;
	Paint secHand = null;
	Paint backGround = null;
	float centerX = 0;
	float centerY = 0;
	float radius = 0;
	float border = 0;
	float handLength = 0;
	float hourHandLength = 0;	
	float minHandLength = 0;	
	float secHandLength = 0;	
	Canvas canvas = null;

	public ClockView(Context context) {
		super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        clockFace = new Paint();
    	clockFace.setColor(Color.WHITE);
        hourHand = new Paint();
    	hourHand.setColor(Color.RED);
    	hourHand.setStrokeWidth(10);
        minHand = new Paint();
    	minHand.setColor(Color.BLUE);
    	minHand.setStrokeWidth(10);
        secHand = new Paint();
    	secHand.setColor(Color.GREEN);
    	secHand.setStrokeWidth(5);
    	backGround = new Paint();
    	backGround.setColor(Color.BLACK);
	}
	

	
    @Override
    public void onDraw(Canvas canvas) {
    	this.canvas = canvas;
    	this.centerX = canvas.getWidth()/2;
    	this.centerY = canvas.getHeight()/2;
    	this.border  = 20;
    	// Colour the Backgound
    	this.canvas.drawPaint(this.backGround);
    	this.radius = centerX -border;
    	this.hourHandLength = radius / 2;
    	this.minHandLength = this.hourHandLength + 40;
    	this.secHandLength = radius -10;
    	// Draw Main Face
    	this.canvas.drawCircle(centerX,  centerY,  radius, clockFace);
    	// Draw Bit in Middle
    	this.canvas.drawCircle(centerX, centerY, 10, this.backGround);
    	
    	drawCircumPoints(this.canvas, this.radius);

    	Calendar cal = Calendar.getInstance(); 
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        int secs = cal.get(Calendar.SECOND);
        drawClockHands((float)hour,  (float)minute,  (float)secs);
    	
    }
    
    public void refreshTime() {
        this.invalidate();
    }
    
    /**
     * Draw Cardinal Points round the Edge
     */
    private void drawCircumPoints(Canvas canvas, float clockRadius) {
    	for (int i = 0; i < 12; i++) {
        	float stopX = (float)(Math.sin(degsToRads(i * 30)) * (clockRadius - 5));
        	float stopY = (float)(Math.cos(degsToRads(i * 30)) * (clockRadius - 5));
        	canvas.drawCircle(this.centerX + stopX, this.centerY - stopY, 5, this.backGround);
    	}
    }
    
    /**
     * Draw the Clock Hands 
     */
	private void drawClockHands(float hours, float mins, float secs) {
    	// Note Conversion into Degrees
    	// Need to Adjust Hour/Min Hand to include fractions of hours/secs eg 2:45 is 2.75 hours to allow proper
    	// hour hand sweeping
    	float disphours = (float) (hours + (mins / 60));
    	float dispmins = (float) (mins + (secs / 60));
    	float dispsecs = (float)secs;
    	drawClockHand(disphours * 30.0, this.hourHandLength, this.hourHand);
    	drawClockHand(dispmins * 6.0, this.minHandLength, this.minHand);
    	drawClockHand(dispsecs * 6.0, this.secHandLength, this.secHand);
	}
        
    private void drawClockHand(double num, float handLength, Paint paint) {
    	// Calculate Positions
    	// Note positions will need mapped to Android Screen Coordinate System
    	float startX = centerX; // Start Position is always center
    	float startY = centerY;
    	float stopX = (float)(Math.sin(degsToRads(num)) * handLength);
    	float stopY = (float)(Math.cos(degsToRads(num)) * handLength);
    	this.canvas.drawLine(startX, startY, startX + stopX, startY - stopY, paint);
    }

    private double degsToRads(double degs) {
    	return ((degs / 180 )* Math.PI);
    }

}
