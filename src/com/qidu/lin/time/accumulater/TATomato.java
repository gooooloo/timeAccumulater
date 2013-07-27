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

import android.content.Context;

public class TATomato
{
	public final long startMs;
	public final long endMs;

	public TATomato(long startMs, long endMs)
	{
		this.startMs = startMs;
		this.endMs = endMs;
	}

	public TATomato(Calendar startCalendar, Calendar endCalendar)
	{
		this.startMs = startCalendar.getTimeInMillis();
		this.endMs = endCalendar.getTimeInMillis();
	}

	public int getDurationMs()
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

	public String getStartEndTimeString(Context context)
	{
		Calendar x = Calendar.getInstance();
		x.setTimeInMillis(startMs);
		String xString = x.getTime().toLocaleString();

		Calendar y = Calendar.getInstance();
		y.setTimeInMillis(endMs);
		String yString = y.getTime().toLocaleString();

		return context.getString(R.string.tomato_start_end_string, xString, yString, getDurationString());

	}

	public long getId()
	{
		return startMs;
	}

}
