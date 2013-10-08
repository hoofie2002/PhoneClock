package com.graemehill.phoneclock;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class SegmentDigits {

	private Resources res;
	private static int DIGIT_ZERO = 0;
	
	private int[] digits = new int[] {
			R.drawable.digit0,
			R.drawable.digit1,
			R.drawable.digit2,
			R.drawable.digit3,
			R.drawable.digit4,
			R.drawable.digit5,
			R.drawable.digit6,
			R.drawable.digit7,
			R.drawable.digit8,
			R.drawable.digit9
	};
	
	public SegmentDigits(Resources res) {
		this.res = res;
	}
	
    public Bitmap[] drawDigitBlocks(double value, int maxNumDigits) {
    	// First of All determine how may digits we will need
    	// Turn it into an int since we don't support decimals
    	int maxDigitLength = String.valueOf((int)value).length();
    	// This will give the number of digits required - if max length = 2 and numDigits = 5 we pad out first 3
    	// Set the Digit Array
    	Bitmap[] mappedDigits = new Bitmap[maxNumDigits];
    	// Increment through the digits left to right
    	for (int digitPos = 1; digitPos <= maxNumDigits; digitPos++) {
    		// Test whether we are up to t he value length yet
    		if (maxDigitLength >= (maxNumDigits - digitPos)) {
    			//process it since we are at the right position
    			double divisor = Math.pow(10 ,(maxNumDigits - digitPos));
    			// Divide Value by 10 to the power of the digit pos FROM THE RIGHT -1
    			double modulus = Math.floor(value/ divisor);
    			// This will give the modulus
    			// So for value 356 it will be 3 then 5 for the next digit then 6
    			// We subtract 300 [3 X 100] from the value and pass through again - this will knock of a digit
    			value = value - (modulus * divisor);
    			Log.d("drawDigitBlocks", "MAXDIGITLENGTH=" + maxDigitLength);
    			Log.d("drawDigitBlocks", "DIGITPOS=" + digitPos);
    			Log.d("drawDigitBlocks", "DIVISOR=" + divisor);
    			Log.d("drawDigitBlocks", "MODULUS=" + modulus);
    			Log.d("drawDigitBlocks", "NEWVALUE=" + value);
    			// We can use this to set the digit
    			mappedDigits[digitPos - 1] = BitmapFactory.decodeResource(this.res, this.digits[(int) modulus]);
    		} else {
    			// Put a zero in
    			mappedDigits[digitPos -1] = BitmapFactory.decodeResource(this.res, this.digits[DIGIT_ZERO]); 
    		}
    		
    	}
    	return mappedDigits;
    }
	
}

