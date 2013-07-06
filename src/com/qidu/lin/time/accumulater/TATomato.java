package com.qidu.lin.time.accumulater;

import java.util.Calendar;

public class TATomato
{
	private final int durationMs;
	public final long startMs;
	public final long endMs;

	public TATomato(long startMs, long endMs)
	{
		this.startMs = startMs;
		this.endMs = endMs;

		durationMs = (int) (this.endMs - this.startMs);
	}

	public String getDurationString()
	{
		int durationSec = durationMs / 1000;
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

}
