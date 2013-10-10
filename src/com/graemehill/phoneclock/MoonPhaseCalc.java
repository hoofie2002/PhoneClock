package com.graemehill.phoneclock;

import java.util.Calendar;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

public class MoonPhaseCalc {

	private static double SYNODIC_PERIOD = 29.530588853;

	public static double calcPhaseForToday() {
		Calendar baseCalendar = Calendar.getInstance();
		baseCalendar.set(1999, Calendar.AUGUST, 11, 0, 0);
		Calendar now = Calendar.getInstance();
		// Calculate Difference in Days
		double timeDiff = (double) (now.getTimeInMillis() - baseCalendar
				.getTimeInMillis());
		double diffInDays = (timeDiff / (1000.0 * 60.0 * 60.0 * 24.0));
		double modulus = diffInDays % SYNODIC_PERIOD;
		return (modulus / SYNODIC_PERIOD);
	}

	public static Bitmap drawMoon() {
		int Ypos = 0;
		int Xpos = 0;
		int Rpos = 0;
		int Xpos1 = 0;
		int Xpos2 = 0;
		int size = 90;
		int halfSize = size /2;
		Paint pBlack = new Paint();
		pBlack.setColor(Color.WHITE);
		Paint pWhite = new Paint();
		pWhite.setColor(Color.GRAY);

		//double phase = calcPhaseForToday();
		double phase = 0.05;
		Bitmap map = Bitmap
				.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
		Log.d("drawMoon", "BITMAP MUTEABLE=" + map.isMutable());
		Canvas c = new Canvas(map);

		// Assuming a circle SIZE
		// Rotate through 0 to 90 degrees ie drop to half size
		for (Ypos = 0; Ypos <= halfSize; Ypos++) {
			// work out the xpos - will drop as rotates
			Xpos = (int) (Math.sqrt(halfSize * halfSize - Ypos * Ypos));
			// Draw darkness part of the moon
			Point pB1 = new Point(size - Xpos, Ypos + size); // Draw
			Point pB2 = new Point(Xpos + size, Ypos + size);
			Point pB3 = new Point(size - Xpos, size - Ypos);
			Point pB4 = new Point(Xpos + size, size - Ypos);

			c.drawLine(pB1.x, pB1.y, pB2.x, pB2.y, pBlack);
			c.drawLine(pB3.x, pB3.y, pB4.x, pB4.y, pBlack);
			// Determine the edges of the lighted part of the moon
			Rpos = 2 * Xpos;
			if (phase < 0.5) {
				Xpos1 = -Xpos;
				Xpos2 = (int) (Rpos - 2 * phase * Rpos - Xpos);
			} else {
				Xpos1 = Xpos;
				Xpos2 = (int) (Xpos - 2 * phase * Rpos + Rpos);
			}
			// Draw the lighted part of the moon
			Point pW1 = new Point(Xpos1 + size, size - Ypos);
			Point pW2 = new Point(Xpos2 + size, size - Ypos);
			Point pW3 = new Point(Xpos1 + size, Ypos + size);
			Point pW4 = new Point(Xpos2 + size, Ypos + size);
			c.drawLine(pW1.x, pW1.y, pW2.x, pW2.y, pWhite);
			c.drawLine(pW3.x, pW3.y, pW4.x, pW4.y, pWhite);
		}
		return map;
	}

}
