package com.graemehill.phoneclock;

import android.util.Log;
import android.widget.ImageView;

public class SegmentDigits {

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
	
	public SegmentDigits() {
	}
	
    public void drawDigitBlock(double value, ImageView[] imgDigits) {
        Log.d("drawDigitBlock", String.valueOf(value));
    	for (int count = imgDigits.length - 1; count > 0; count --) {
            Log.d("drawDigitBlock", "counter = "  + String.valueOf(count));
            Log.d("drawDigitBlock", "Divisor is " + String.valueOf(Math.pow(10.0, count)));
    		value  = processDigitTotal(value , Math.pow(10.0, count), (ImageView) imgDigits[count]);
    	}
    }
    
    private double processDigitTotal(double value, double divisor, ImageView digit) {
    	Log.d("processDigitTotal", "Value: " + String.valueOf(value));
    	Log.d("processDigitTotal", "Divisor: " + String.valueOf(divisor));
    	double remain = Math.floor(value / divisor);
    	Log.d("processDigitTotal", "Remain : " + String.valueOf(remain));
    	int pos = (int)remain;
    	Log.d("processDigitTotal", "Position : " + String.valueOf(pos));
    	digit.setImageResource(this.digits[pos]);
    	
    	double retValue = value - (remain * divisor);
    	if (retValue > 0.0) { 
    		return retValue;
    	} else {
    		return 0.0;
    	}
    }
	
}
