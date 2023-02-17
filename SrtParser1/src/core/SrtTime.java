package core;

import exception.*;

public class SrtTime {
	/**
	 * 定义了字幕时间（SrtTime）的结构，以及可输入的时间范围，并提供 get、set 方法和时间转换方法
	 * */
	// SrtTime 的结构
	int hour;
	int minute;
	int second;
	int msecond;

	public static final int MAX_HOUR = 99;
	public static final int MAX_MINUTE = 59;
	public static final int MAX_SECOND = 59;
	public static final int MAX_MSECOND = 999;

	public static final int MIN_HOUR = -99;
	public static final int MIN_MINUTE = -59;
	public static final int MIN_SECOND = -59;
	public static final int MIN_MSECOND = -999;

	public static final int CONST_HOUR_CONVERT_MSECOND = 3600000;
	public static final int CONST_MINUTE_CONVERT_MSECOND = 60000;
	public static final int CONST_SECOND_CONVERT_MSECOND = 1000;
	public static final int CONST_MINUTE_CONVERT_SECOND = 60;
	public static final int CONST_HOUR_CONVERT_MINUTE = 60;


	public SrtTime() {

	}

	SrtTime(int hour,int minute,int second,int msecond) {
		this.hour=hour;
		this.minute=minute;
		this.second=second;
		this.msecond=msecond;
	}

	//将 SetTime 格式转换成 String
	public String toString(){
		String str = "";
		str+=String.format("%02d",this.hour);
		str+=":";
		str+=String.format("%02d",this.minute);
		str+=":";
		str+=String.format("%02d",this.second);
		str+=",";
		str+=String.format("%03d",this.msecond);
		return str;
	}

	public int asMsecondInt(){
		int ms = msecond;
		ms+=(hour*CONST_HOUR_CONVERT_MSECOND);
		ms+=(minute*CONST_MINUTE_CONVERT_MSECOND);
		ms+=(second*CONST_SECOND_CONVERT_MSECOND);
		return ms;
	}

	public void setMsecondInt(int msecond) throws SrtTimeOutOfBoundaryException {
		int totalSecond = (msecond - msecond%CONST_SECOND_CONVERT_MSECOND)/CONST_SECOND_CONVERT_MSECOND;
		int totalMinute = (totalSecond-totalSecond%CONST_MINUTE_CONVERT_SECOND)/CONST_MINUTE_CONVERT_SECOND;
		int totalHour = (totalMinute-totalMinute%CONST_HOUR_CONVERT_MINUTE)/CONST_HOUR_CONVERT_MINUTE;

		try {
			
			this.setMsecond(msecond%CONST_SECOND_CONVERT_MSECOND);
			this.setSecond((totalSecond%CONST_MINUTE_CONVERT_SECOND));
			this.setMinute((totalMinute%CONST_HOUR_CONVERT_MINUTE));
			this.setHour(totalHour);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) throws SrtTimeHourOutOfBoundaryException {
		if(hour> MAX_HOUR || hour < MIN_HOUR){
			throw new SrtTimeHourOutOfBoundaryException();
		}
		this.hour=hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) throws SrtTimeMinuteOutOfBoundaryException {
		if(minute> MAX_MINUTE || minute < MIN_MINUTE){
			throw new SrtTimeMinuteOutOfBoundaryException();
		}
		this.minute = minute;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) throws SrtTimeSecondOutOfBoundaryException {
		if(second> MAX_SECOND || second < MIN_SECOND){
			throw new SrtTimeSecondOutOfBoundaryException();
		}
		this.second = second;
	}

	public int getMsecond() {
		return msecond;
	}

	public void setMsecond(int msecond) throws SrtTimeMSecondOutOfBoundaryException {
		if(msecond > MAX_MSECOND || msecond < MIN_MSECOND){
			throw new SrtTimeMSecondOutOfBoundaryException();
		}
		this.msecond = msecond;
	}
}


