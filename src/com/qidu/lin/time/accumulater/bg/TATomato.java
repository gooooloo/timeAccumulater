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
package com.qidu.lin.time.accumulater.bg;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

import com.qidu.lin.time.accumulater.R;

public class TATomato
{
	public enum StringFilter
	{
		StartDate, EndDate, StartTime, StartTimeWithDate, EndTime, EndTimeWithDate, Duration,
	}

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

	public String getString(Context context, StringFilter filter)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Calendar x = Calendar.getInstance();

		SimpleDateFormat format = null;
		Date d = null;

		switch (filter)
		{
		case StartDate:
		case EndDate:
			format = dateFormat;
			break;
		case StartTime:
		case EndTime:
			format = timeFormat;
			break;
		case StartTimeWithDate:
		case EndTimeWithDate:
			format = dateTimeFormat;
			break;
		case Duration:
			return getDurationString();
		default:
			return "ERROR";
		}

		switch (filter)
		{
		case StartDate:
		case StartTime:
		case StartTimeWithDate:
			x.setTimeInMillis(startMs);
			d = x.getTime();
			break;
		case EndDate:
		case EndTime:
		case EndTimeWithDate:
			x.setTimeInMillis(endMs);
			d = x.getTime();
			break;
		case Duration:
			d = new Date();
			d.setTime(endMs - startMs);
			break;
		}

		return format.format(d);
	}

	public long getId()
	{
		return startMs;
	}

}
