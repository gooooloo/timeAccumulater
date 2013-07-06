package com.qidu.lin.time.accumulater;

public class TATomato
{
	public final int duration;
	public final long start;
	public final long end;
	public final int minutes;
	public final int seconds;

	public TATomato(String start, String end)
	{
		this.start = Long.valueOf(start.trim());
		this.end = Long.valueOf(end.trim());

		duration = (int) (this.end - this.start);
		seconds = duration % 60;
		minutes = duration / 60;
	}

	public String getDurationString()
	{
		return "" + minutes + ":" + seconds;
	}
	
	public String getStartTimeString()
	{
		return ""+start;
	}

}
