package com.graemehill.phoneclock;

public class DateInfo {

		private int dayOfMonth;
    	private int hour;
    	private int minute;
    	private int second;
    	
    	public DateInfo(int day, int hour, int minute, int second) {
    		this.dayOfMonth = day;
    		this.hour = hour;
    		this.minute = minute;
    		this.second = second;
    	}
    	
    	public int getDayOfMonth() {
			return dayOfMonth;
		}

		public int getHour() {
			return hour;
		}

		public int getMinute() {
			return minute;
		}

		public int getSecond() {
			return second;
		}

    }
