/*
 * Copyright 2013 Qidu Lin
 * 
 * This file is part of TimeAccumulater.
 * 
 * TimeAccumulater is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * TimeAccumulater is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * TimeAccumulater. If not, see <http://www.gnu.org/licenses/>.
 */
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
