package com.graemehill.phoneclock;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class ClockView extends View {
	
	Paint clockFace = null;
	Paint hourHand = null;
	Paint minHand = null;
	Paint secHand = null;
	Paint backGround = null;
	Paint digitColor = null;
	float centerX = 0;
	float centerY = 0;
	float radius = 0;
	float border = 0;
	float handLength = 0;
	float hourHandLength = 0;	
	float minHandLength = 0;	
	float secHandLength = 0;	
	boolean is24Display = false;
	Canvas canvas = null;
	SegmentDigits digits = null;

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
    	secHand.setStrokeWidth(2);
    	backGround = new Paint();
    	backGround.setColor(Color.BLACK);
    	digitColor = new Paint();
    	digitColor.setColor(Color.RED);
    	
    	digits = new SegmentDigits(getResources());
	}
	
	
	public void is24Display(boolean value) {
		this.is24Display = value;
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
        int date = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        int secs = cal.get(Calendar.SECOND);
        drawClockHands((float)hour,  (float)minute,  (float)secs);
        
        // Need to also draw the digital clock
    	if (this.is24Display) {
    		hour = hour + 12;
    	}

        drawDigitalClockFace(hour, minute, this.centerX, this.centerY);
        drawDate(date,this.centerX, this.centerY);
    	
    }


	private void drawDigitalClockFace(int hour, int minute, float centerX, float centerY) {
		Bitmap[] hourDigits = digits.drawDigitBlocks((double)hour, 2);
        Bitmap[] minuteDigits = digits.drawDigitBlocks((double)minute, 2);
        if (hourDigits == null) {
        	Log.d("onDraw", "CLOCK DIGITS ARE NULL");
        } else {
        	Log.d("onDraw", "CLOCK DIGITS ARE SET - " + hourDigits.length);
        }
        float yCenterGap = 20;
        float xCenterGap = 20;
        float xDigitGap = 1;
        // Work out new POsition
        float newY = centerY - yCenterGap - hourDigits[0].getHeight();
        float leftX = centerX - xCenterGap - xDigitGap - (hourDigits[0].getWidth()* 2);
        float rightX = centerX + xCenterGap;
        
        this.canvas.drawBitmap(hourDigits[0], leftX, newY, this.digitColor);
        this.canvas.drawBitmap(hourDigits[1], leftX + hourDigits[1].getWidth() + xDigitGap, newY, this.digitColor);
        
        this.canvas.drawBitmap(minuteDigits[0], rightX, newY, this.digitColor);
        this.canvas.drawBitmap(minuteDigits[1], rightX + minuteDigits[1].getWidth() + xDigitGap, newY, this.digitColor);
	}
	
	private void drawDate(int date, float centerX, float centerY) {
		Bitmap[] dateDigits = digits.drawDigitBlocks((double)date, 2);
        float yCenterGap = 20;
        float xCenterGap = 20;
        float xDigitGap = 1;
        // Work out new POsition
        float rightX = centerX + xCenterGap;
        float newY = centerY + yCenterGap + dateDigits[0].getHeight();
        
        this.canvas.drawBitmap(dateDigits[0], rightX, newY, this.digitColor);
        this.canvas.drawBitmap(dateDigits[1], rightX + dateDigits[1].getWidth() + xDigitGap, newY, this.digitColor);
	}
    
    public void refreshTime() {
        this.invalidate();
    }
    
    /**
     * Draw Cardinal Points round the Edge
     */
    private void drawCircumPoints(Canvas canvas, float clockRadius) {
    	int radiusDivisor = 30;
    	int numHourPoints = 12;
    	if (this.is24Display) {
    		radiusDivisor = 15;
    		numHourPoints = 24;
    	}
    	for (int i = 0; i < numHourPoints; i++) {
        	float stopX = (float)(Math.sin(degsToRads(i * radiusDivisor)) * (clockRadius - 5));
        	float stopY = (float)(Math.cos(degsToRads(i * radiusDivisor)) * (clockRadius - 5));
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
    	double hourDivisor = 30;
    	double minDivisor = 6;

		
    	float disphours = (float) (hours + (mins / 60));
    	float dispmins = (float) (mins + (secs / 60));
    	float dispsecs = (float)secs;

    	if (this.is24Display) {
    		hourDivisor = 15;
    		// Note we read Hours 0-11
    		disphours = disphours + 12;
    	}

    	// Note that for 24 the hour hand moves 1/2 as quickly but mins/secs are unchanged
    	drawClockHand(disphours * hourDivisor, this.hourHandLength, this.hourHand);
    	drawClockHand(dispmins * minDivisor, this.minHandLength, this.minHand);
    	drawClockHand(dispsecs * minDivisor, this.secHandLength, this.secHand);
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
