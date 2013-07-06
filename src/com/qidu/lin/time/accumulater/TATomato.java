package com.qidu.lin.time.accumulater;

public class TATomato
{
	public final String duration;
	private final long start;
	private final long end;
	public final int minutes;
	public final int seconds;

	public TATomato(String start, String end)
	{
		this.start = Long.valueOf(start.trim());
		this.end = Long.valueOf(end.trim());

		int time = (int) (this.end - this.start);
		seconds = time % 60;
		minutes = time / 60;
		duration = "" + minutes + ":" + seconds;
	}

}
