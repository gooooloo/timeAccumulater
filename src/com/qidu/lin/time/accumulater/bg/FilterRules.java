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

import java.util.List;

import android.content.Context;

public class FilterRules
{
	public boolean unaccumalatedOnly = false;
	public boolean within2DaysOnly = false;

	private boolean filterOff(Context context, TATomato tomato, FilterRules filterRules)
	{
		if (filterRules.unaccumalatedOnly)
		{
			if (TATomatoPersistence.getProjectName(context, tomato.getId()) != null)
			{
				return true;
			}
		}

		if (filterRules.within2DaysOnly)
		{
			long msUntilNow = System.currentTimeMillis() - tomato.startMs;
			int msPerDay = 1000 * 60 * 60 * 24;
			if (msUntilNow > msPerDay * 2)
			{
				return true;
			}
		}

		return false;
	}

	public void filterTomatoListByUISettings(Context context, List<TATomato> tomatoListReverse2, FilterRules filterRules)
	{
		for (int i = tomatoListReverse2.size() - 1; i >= 0; i--)
		{
			if (filterOff(context, tomatoListReverse2.get(i), filterRules))
			{
				tomatoListReverse2.remove(i);
			}
		}
	}
}