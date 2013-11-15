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

import android.content.Context;

public class TATomatoPersistence
{
	private static final String KEY_TOMATO_ID = "KEY_TOMATO_ID_";
	private static final String TAG_TOMATO_PERSISTANCE = "TAG_TOMATO_PERSISTANCE";
	private static final String TAG_TOMATO_NOTE = "TAG_TOMATO_NOTE";
	private static final String TAG_TOMATO_RATING = "TAG_TOMATO_RATING";

	public static void saveProjectName(Context context, long tomatoId, String projectName)
	{
		if (invalidId(tomatoId))
		{
			return;
		}

		context.getSharedPreferences(TAG_TOMATO_PERSISTANCE, Context.MODE_PRIVATE).edit()
				.putString(getKeyByTomatoId(tomatoId), projectName).commit();
	}

	public static String getProjectName(Context context, long tomatoId)
	{
		if (invalidId(tomatoId))
		{
			return null;
		}
		return context.getSharedPreferences(TAG_TOMATO_PERSISTANCE, Context.MODE_PRIVATE).getString(getKeyByTomatoId(tomatoId), null);
	}

	public static void saveTomatoNote(Context context, long tomatoId, String note)
	{

		if (invalidId(tomatoId))
		{
			return;
		}

		context.getSharedPreferences(TAG_TOMATO_NOTE, Context.MODE_PRIVATE).edit().putString(getKeyByTomatoId(tomatoId), note).commit();
	}

	public static String loadTomatoNote(Context context, long tomatoId)
	{
		if (invalidId(tomatoId))
		{
			return null;
		}
		return context.getSharedPreferences(TAG_TOMATO_NOTE, Context.MODE_PRIVATE).getString(getKeyByTomatoId(tomatoId), null);
	}

	public static void saveTomatoRating(Context context, long tomatoId, float numStars)
	{
		if (invalidId(tomatoId))
		{
			return;
		}

		context.getSharedPreferences(TAG_TOMATO_RATING, Context.MODE_PRIVATE).edit().putFloat(getKeyByTomatoId(tomatoId), numStars)
				.commit();

	}

	public static float loadTomatoRating(Context context, long tomatoId)
	{
		if (invalidId(tomatoId))
		{
			return 0;
		}
		return context.getSharedPreferences(TAG_TOMATO_RATING, Context.MODE_PRIVATE).getFloat(getKeyByTomatoId(tomatoId), 0);
	}

	private static boolean invalidId(long tomatoId)
	{
		return tomatoId <= 0;
	}

	private static String getKeyByTomatoId(long tomatoId)
	{
		return KEY_TOMATO_ID + tomatoId;
	}
}
