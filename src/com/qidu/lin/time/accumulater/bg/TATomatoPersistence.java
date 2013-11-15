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
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class TATomatoPersistence
{
	private static final String KEY_TOMATO_ID = "KEY_TOMATO_ID_";
	private static final String TAG_TOMATO_PERSISTANCE = "TAG_TOMATO_PERSISTANCE";
	private static final String TAG_TOMATO_NOTE = "TAG_TOMATO_NOTE";
	private static final String TAG_TOMATO_RATING = "TAG_TOMATO_RATING";
	private static final String TAG_TOMATO_COUNT = "com.qidu.lin.timeAccumulate.TAG_TOMATO_COUNT";
	private static final String TAG_TOMATO_INDEX_BEGIN_KEY = "com.qidu.lin.timeAccumulate.TAG_TOMATO_INDEX_BEGIN_KEY_";
	private static final String TAG_TOMATO_INDEX_END_KEY = "com.qidu.lin.timeAccumulate.TAG_TOMATO_INDEX_END_KEY_";
	private static final String TAG_LAST_TOGGLE_TIME = "com.qidu.lin.timeAccumulate.TAG_LAST_TOGGLE_TIME";
	private static final String TAG_IS_TIME_COMSUMING_FLAG = "com.qidu.lin.timeAccumulate.TAG_IS_TIME_COMSUMING_FLAG";
	private static final String TAG_TIME_ACCUMULATE = "com.qidu.lin.timeAccumulate.TAG_TIME_ACCUMULATE";

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

	public static void saveATomato(Context context, String projectName, long beginMs, long endMs)
	{
		SharedPreferences sp = getProjectSP(context, projectName);
		int tomatoCnt = sp.getInt(TAG_TOMATO_COUNT, 0) + 1;
		int tomatoIndex = tomatoCnt;
		Editor edit = sp.edit();
		edit.putInt(TAG_TOMATO_COUNT, tomatoCnt);
		edit.putLong(getTomatoBeginKey(tomatoIndex), beginMs);
		edit.putLong(getTomatoEndKey(tomatoIndex), endMs);
		edit.commit();
	}

	public static List<TATomato> loadReverseTomatoListForProject(Context context, String projectName)
	{
		SharedPreferences sp = getProjectSP(context, projectName);
		int tomatoCount = sp.getInt(TAG_TOMATO_COUNT, 0);
		if (tomatoCount <= 0)
		{
			return null;
		}

		ArrayList<TATomato> list = new ArrayList<TATomato>();
		for (int i = tomatoCount; i >= 1; i--)
		{
			long startMs = sp.getLong(getTomatoBeginKey(i), 0);
			long endMs = sp.getLong(getTomatoEndKey(i), 0);
			if (startMs == 0 || endMs == 0)
			{
				continue;
			}
			TATomato tomato = new TATomato(startMs, endMs);
			list.add(tomato);
		}
		return list;

	}

	public static void saveAccumulate(Context context, String projectName, long duration)
	{
		getProjectSP(context, projectName).edit().putLong(TAG_TIME_ACCUMULATE, duration).commit();
	}

	public static long loadAccumulateMs(Context context, String projectName)
	{
		return getProjectSP(context, projectName).getLong(TAG_TIME_ACCUMULATE, 0);
	}

	public static void deleteAllTomatoForProject(Context context, String projectName)
	{
		SharedPreferences sp = getProjectSP(context, projectName);
		sp.edit().clear().commit();
	}

	public static long loadLastTime(Context context, String name)
	{
		return getProjectSP(context, name).getLong(TAG_LAST_TOGGLE_TIME, Calendar.getInstance().getTimeInMillis());
	}

	public static void saveLastTime(Context context, String name, long y)
	{
		getProjectSP(context, name).edit().putLong(TAG_LAST_TOGGLE_TIME, y).commit();
	}

	public static void saveOnFlag(Context context, String name, boolean on)
	{
		getProjectSP(context, name).edit().putBoolean(TAG_IS_TIME_COMSUMING_FLAG, on).commit();
	}

	public static boolean loadOnFlag(Context context, String projectName)
	{
		return getProjectSP(context, projectName).getBoolean(TAG_IS_TIME_COMSUMING_FLAG, false);
	}

	public static void moveAllTomatoToAnotherProject(Context context, String projectNameSrc, String projectNameDesc)
	{
		SharedPreferences spSrc = getProjectSP(context, projectNameSrc);
		SharedPreferences spDesc = getProjectSP(context, projectNameDesc);
		Editor editDesc = spDesc.edit();

		for (Map.Entry<String, ?> each : ((Map<String, ?>) spSrc.getAll()).entrySet())
		{
			String key = each.getKey();
			Object value = each.getValue();

			if (value instanceof String)
			{
				editDesc.putString(key, (String) value);
			}
			else if (value instanceof Long)
			{
				editDesc.putLong(key, (Long) value);
			}
			else if (value instanceof Integer)
			{
				editDesc.putInt(key, (Integer) value);
			}
			else if (value instanceof Boolean)
			{
				editDesc.putBoolean(key, (Boolean) value);
			}
			else if (value instanceof Float)
			{
				editDesc.putFloat(key, (Float) value);
			}
		}

		editDesc.commit();

		spSrc.edit().clear().commit();
	}

	private static SharedPreferences getProjectSP(Context context, String name)
	{
		return context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	private static boolean invalidId(long tomatoId)
	{
		return tomatoId <= 0;
	}

	private static String getKeyByTomatoId(long tomatoId)
	{
		return KEY_TOMATO_ID + tomatoId;
	}

	private static String getTomatoEndKey(int tomatoIndex)
	{
		return TAG_TOMATO_INDEX_END_KEY + tomatoIndex;
	}

	private static String getTomatoBeginKey(int tomatoIndex)
	{
		return TAG_TOMATO_INDEX_BEGIN_KEY + tomatoIndex;
	}
}
