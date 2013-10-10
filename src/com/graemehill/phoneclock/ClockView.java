package com.graemehill.phoneclock;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
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
	Paint clockTickMarks = null;
	Paint digitColor = null;
	Paint outerRing = null;
	float centerX = 0;
	float centerY = 0;
	float radius = 0;
	float border = 0;
	float handLength = 0;
	float hourHandLength = 0;	
	float minHandLength = 0;	
	float secHandLength = 0;	
	boolean is24Display = false;
	boolean isPortrait= false;
	int moonSize = 150;
	Canvas canvas = null;
	SegmentDigits digits = null;
	Bitmap phaseMap = null;

	public ClockView(Context context) {
		super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        clockFace = new Paint();
    	clockFace.setColor(Color.BLACK);
        hourHand = new Paint();
    	hourHand.setColor(Color.RED);
    	hourHand.setStrokeWidth(5);
        minHand = new Paint();
    	minHand.setColor(Color.BLUE);
    	minHand.setStrokeWidth(5);
        secHand = new Paint();
    	secHand.setColor(Color.GREEN);
    	secHand.setStrokeWidth(2);
    	backGround = new Paint();
    	backGround.setColor(Color.BLACK);
    	digitColor = new Paint();
    	digitColor.setColor(Color.RED);
    	clockTickMarks = new Paint();
    	clockTickMarks.setColor(Color.WHITE);
    	outerRing = new Paint();
    	outerRing.setColor(Color.GRAY);
    	
    	digits = new SegmentDigits(getResources());
    	
		// Dump out Moon Phase
		//phaseMap = MoonPhaseCalc.drawMoon();

    	
	}
	
	
	public void set24Display(boolean value) {
		this.is24Display = value;
	}

	public void setPortrait(boolean value) {
		this.isPortrait = value;
	}

	
    @Override
    public void onDraw(Canvas canvas) {
    	this.canvas = canvas;
    	this.centerX = canvas.getWidth()/2;
    	this.centerY = canvas.getHeight()/2;
    	this.border  = 20;
    	// Colour the Backgound
    	this.canvas.drawPaint(this.backGround);
    	// Switch for Orientation Change
    	if (this.isPortrait) {
    		this.radius = centerX -border;
    	} else { 
    		this.radius = centerY - border;
    	}
    	this.hourHandLength = radius / 2;
    	this.minHandLength = this.hourHandLength + 40;
    	this.secHandLength = radius -30;
    	// Draw Main Face
    	this.canvas.drawCircle(centerX,  centerY,  radius, clockFace);
    	// Draw Bit in Middle
    	this.canvas.drawCircle(centerX, centerY, 10, this.clockTickMarks);
    	

    	Calendar cal = Calendar.getInstance(); 
        int date = cal.get(Calendar.DAY_OF_MONTH);
        // Note that we get hour of DAY which is in 24 hour clock
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int secs = cal.get(Calendar.SECOND);
        
        // SOme Corrections needed if in 12 hour mode since hour of day is in 24 hour
        // Need to also draw the digital clock

        drawClockFace(this.canvas, this.radius);
    	drawCircumPoints(this.canvas, this.radius);
        drawClockHands(this.is24Display, (float)hour,  (float)minute,  (float)secs);
        drawDigitalClockFace(this.is24Display, hour, minute, this.centerX, this.centerY);
        drawDate(date,this.centerX, this.centerY);
        //drawMoonPhase(phaseMap,this.centerX, this.centerY, this.moonSize);
    	
    }

    /**
     * Draw Clock Face
     */
    private void drawClockFace(Canvas canvas, float clockRadius) {
    	// Outer Ring
        canvas.drawCircle(this.centerX, this.centerY, clockRadius + 5, this.outerRing);
        canvas.drawCircle(this.centerX, this.centerY, clockRadius, this.backGround);
    }
    

    
    /**
     * Draw a digital clock
     * @param is24Hour true if 24 hour display ie 23 instead of 11
     * @param hour hour in 24 hour format
     * @param minute minute
     * @param centerX
     * @param centerY
     */
	private void drawDigitalClockFace(boolean is24Hour, 
			int hour, 
			int minute, 
			float centerX, 
			float centerY) {
		// Correct for display format
		if (!is24Hour && hour >12) {
			hour = hour - 12;
		}
		
		Bitmap[] hourDigits = digits.drawDigitBlocks((double)hour, 2);
        Bitmap[] minuteDigits = digits.drawDigitBlocks((double)minute, 2);
        float yCenterGap = 20;
        float xCenterGap = 20;
        // Work out new POsition
        float newY = centerY - yCenterGap - hourDigits[0].getHeight();
        float leftX = centerX - xCenterGap - (hourDigits[0].getWidth()* 2);
        float rightX = centerX + xCenterGap;
        
        drawDigitPair(hourDigits, leftX, newY, 1);
        drawDigitPair(minuteDigits, rightX, newY, 1);
        
	}

	// Draw a Pair of Digits
	private void drawDigitPair(Bitmap[] dispDigits, float xPos, float yPos,
			int borderSize) {
		// Border Rectangle Digit 1
        this.canvas.drawRect(xPos - borderSize, 
        					yPos - borderSize, 
        					xPos + dispDigits[0].getWidth()  + dispDigits[1].getWidth() + borderSize, 
        					yPos + dispDigits[0].getHeight() + borderSize, 
        					this.outerRing);
        this.canvas.drawBitmap(dispDigits[0], xPos, yPos, this.digitColor);
        this.canvas.drawBitmap(dispDigits[1], xPos + dispDigits[1].getWidth() , yPos, this.digitColor);
	}
	
	private void drawDate(int date, float centerX, float centerY) {
		Bitmap[] dateDigits = digits.drawDigitBlocks((double)date, 2);
        float yCenterGap = 20;
        float xCenterGap = 20;
        // Work out new POsition
        float rightX = centerX + xCenterGap;
        float newY = centerY + yCenterGap + dateDigits[0].getHeight();
        
        drawDigitPair(dateDigits, rightX, newY, 1);
	}

	/*
	private void drawMoonPhase(Bitmap phaseMap, float centerX, float centerY, int size) {
        float yCenterGap = 20;
        float xCenterGap = 20;
        // Work out new POsition
        float rightX = centerX - xCenterGap - phaseMap.getWidth();
        float newY = centerY + yCenterGap + phaseMap.getHeight();
        
        this.canvas.drawBitmap(phaseMap, rightX, newY, this.digitColor);
	}
	 */
	
    public void refreshTime() {
        this.invalidate();
    }
    
    
    /**
     * Draw Cardinal Points round the Edge
     */
    private void drawCircumPoints(Canvas canvas, float clockRadius) {
    	//int numHourPoints = 12;
    	int numHourPoints = 60;
    	int sizeTick = 0;
    	if (this.is24Display) {
    		numHourPoints = 24;
    	}
    	// Calculate how often to space them
    	int radiusDivisor = (360 / numHourPoints);
    	for (int i = 0; i < numHourPoints; i++) {
    		sizeTick = 2;
        	float stopX = (float)(Math.sin(degsToRads(i * radiusDivisor)) * (clockRadius - 5));
        	float stopY = (float)(Math.cos(degsToRads(i * radiusDivisor)) * (clockRadius - 5));
        	// Mark major cardinals in 5, minor in 2
        	// Stick to 2 if 24 hours clock
        	if ( (Math.floor(i / 5) == ((double)i / 5.0)) && (!this.is24Display)) {
        		sizeTick = 5;
        	}
        	canvas.drawCircle(this.centerX + stopX, this.centerY - stopY, sizeTick, this.clockTickMarks);
    	}
    }
    
    /**
     * Draw the Clock Hands 
     */
	private void drawClockHands(boolean is24Hour,
			float hours, 
			float mins, 
			float secs) {
    	// Note Conversion into Degrees
    	// Need to Adjust Hour/Min Hand to include fractions of hours/secs eg 2:45 is 2.75 hours to allow proper
    	// hour hand sweeping
    	double hourDivisor = 30;
    	double minDivisor = 6;

		
    	float disphours = (float) (hours + (mins / 60));
    	float dispmins = (float) (mins + (secs / 60));
    	float dispsecs = (float)secs;

    	// Correct for 24 hour display
    	if (is24Hour) {
    		hourDivisor = 15;
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
