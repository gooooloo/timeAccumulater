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

public class TATime
{
	public final int hour;
	public final int minute;
	public final int second;

	public TATime(int hour, int minute, int second)
	{
		if (second >= 60)
		{
			minute += second / 60;
			second %= 60;
		}

		if (minute >= 60)
		{
			hour += minute / 60;
			minute %= 60;
		}

		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}

	public String toString()
	{
		return String.format("%d:%d%d:%d%d", hour, minute / 10, minute % 10, second / 10, second % 10);
	}
}
