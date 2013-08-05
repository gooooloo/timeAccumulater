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

package com.qidu.lin.time.accumulater.bg.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.net.Uri;

import com.qidu.lin.time.accumulater.bg.TATomato;

public class TAClockwiseTomatoCSVParser
{
	public static List<TATomato> parse(Uri uri) throws IOException
	{
		ArrayList<TATomato> list = new ArrayList<TATomato>();

		TACSVParser csvParser = new TACSVParser(uri);

		long lineCnt = 0;
		for (String[] y : csvParser.getMyEntries())
		{
			lineCnt++;
			if (lineCnt == 1)
			{
				// pass for header line.
				continue;
			}

			TATomato tomato = new TATomato(getStartOrEndTimeMs(y[5]), getStartOrEndTimeMs(y[6]));
			list.add(tomato);
		}

		return list;
	}

	public static List<TATomato> parseInReverse(Uri uri) throws IOException
	{
		List<TATomato> list = parse(uri);
		Collections.reverse(list);
		return list;
	}

	private static long getStartOrEndTimeMs(String timeStr)
	{
		// assumption : the time in clockwise tomato are in "Seconds" rather
		// than in "MillionSeconds".
		return Long.valueOf(timeStr.trim()) * 1000;
	}
}
