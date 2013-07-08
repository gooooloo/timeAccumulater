package com.qidu.lin.time.accumulater;

import java.util.Calendar;

public class TATomato
{
	public final long startMs;
	public final long endMs;

	public TATomato(long startMs, long endMs)
	{
		this.startMs = startMs;
		this.endMs = endMs;
	}
	
	public  int getDurationMs()
	{
		return (int) (endMs - startMs);
	}

	public String getDurationString()
	{
		int durationSec = getDurationMs() / 1000;
		int seconds = durationSec % 60;
		int minutes = durationSec / 60;
		return "" + minutes + ":" + seconds;
	}

	public String getStartTimeString()
	{
		Calendar x = Calendar.getInstance();
		x.setTimeInMillis(startMs);
		return x.getTime().toLocaleString();
	}
	
	public long getId()
	{
		return startMs;
	}

}
