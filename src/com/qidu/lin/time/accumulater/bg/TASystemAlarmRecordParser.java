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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TASystemAlarmRecordParser
{
	public static List<TATomato> parse(String content)
	{
		List<TATomato> tomatoListReverse = null;
		String regularExpression = "((\\d+):(\\d+)\\.\\d+)";
		Matcher matcher = Pattern.compile(regularExpression).matcher(content);
		if (matcher.find())
		{
			MatchResult results = matcher.toMatchResult();
			String min = results.group(2);
			String sec = results.group(3);
			int durationMs = (Integer.valueOf(min) * 60 + Integer.valueOf(sec)) * 1000;
			long curMs = System.currentTimeMillis();
			tomatoListReverse = new ArrayList<TATomato>();
			tomatoListReverse.add(new TATomato(curMs - durationMs, curMs));
		}
		return tomatoListReverse;
	}

}
