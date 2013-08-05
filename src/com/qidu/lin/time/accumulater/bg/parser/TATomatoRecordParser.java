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
import java.util.List;

import android.content.Intent;
import android.net.Uri;

import com.qidu.lin.time.accumulater.bg.DataSource;
import com.qidu.lin.time.accumulater.bg.TATomato;

public class TATomatoRecordParser
{
	public static class TomatoListReverseWithSource
	{
		final public DataSource source;
		public final List<TATomato> tomatoListReverse;

		public TomatoListReverseWithSource(DataSource source, List<TATomato> tomatoListReverse)
		{
			this.source = source;
			this.tomatoListReverse = tomatoListReverse;
		}
	}

	public static TomatoListReverseWithSource parseTomatoListReverseAndSource(Intent intent)
	{
		assert (intent.getType().equalsIgnoreCase("text/plain"));
		Uri stream = intent.getParcelableExtra(Intent.EXTRA_STREAM);

		List<TATomato> tomatoListReverse = null;
		DataSource source = null;
		try
		{
			tomatoListReverse = TAClockwiseTomatoCSVParser.parseInReverse(stream);
			source = DataSource.ClockwiseTomato;
		}
		catch (IOException e)
		{
			String content = intent.getStringExtra(Intent.EXTRA_TEXT);
			tomatoListReverse = TASystemAlarmRecordParser.parse(content);
			source = DataSource.SystemAlarm;
		}

		return new TomatoListReverseWithSource(source, tomatoListReverse);
	}
}
