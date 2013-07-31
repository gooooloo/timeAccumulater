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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class TADataCenter
{
	private static final String TAG_IS_TIME_COMSUMING_FLAG = "com.qidu.lin.timeAccumulate.TAG_IS_TIME_COMSUMING_FLAG";
	private static final String TAG_TIME_ACCUMULATE = "com.qidu.lin.timeAccumulate.TAG_TIME_ACCUMULATE";
	private static final String TAG_LAST_TOGGLE_TIME = "com.qidu.lin.timeAccumulate.TAG_LAST_TOGGLE_TIME";
	private static final String TAG_TOMATO_COUNT = "com.qidu.lin.timeAccumulate.TAG_TOMATO_COUNT";
	private static final String TAG_TOMATO_INDEX_BEGIN_KEY = "com.qidu.lin.timeAccumulate.TAG_TOMATO_INDEX_BEGIN_KEY_";
	private static final String TAG_TOMATO_INDEX_END_KEY = "com.qidu.lin.timeAccumulate.TAG_TOMATO_INDEX_END_KEY_";

	static boolean getOnFlag(Context context, String projectName)
	{

		return TADataCenter.getProjectSP(context, projectName).getBoolean(TAG_IS_TIME_COMSUMING_FLAG, false);
	}

	public static void Toggle(Context context, String projectName)
	{
		boolean beforeIsOn = getOnFlag(context, projectName);

		long curTimeMs = Calendar.getInstance().getTimeInMillis();

		if (beforeIsOn)
		{
			long lastTimeMs = getLastTime(context, projectName);
			long pastTimeMs = curTimeMs - lastTimeMs;
			addPastTimeToAccumulate(context, projectName, pastTimeMs);
			saveATomato(context, projectName, lastTimeMs, curTimeMs);
		}
		setLastTime(context, projectName, curTimeMs);
		setOnFlag(context, projectName, !beforeIsOn);

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

	private static String getTomatoEndKey(int tomatoIndex)
	{
		return TAG_TOMATO_INDEX_END_KEY + tomatoIndex;
	}

	private static String getTomatoBeginKey(int tomatoIndex)
	{
		return TAG_TOMATO_INDEX_BEGIN_KEY + tomatoIndex;
	}

	public static List<TATomato> getReverseTomatoListForProject(Context context, String projectName)
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

	public static void addPastTimeToAccumulate(Context context, String projectName, long pastTimeMs)
	{
		setAccumulate(context, projectName, pastTimeMs + getAccumulateMs(context, projectName));
	}

	public static long getAccumulateMs(Context context, String projectName)
	{
		return getProjectSP(context, projectName).getLong(TAG_TIME_ACCUMULATE, 0);
	}

	public static TATime getAccumulateTime(Context context, String projectName)
	{
		int x = (int) TADataCenter.getAccumulateMs(context, projectName);
		int sec = x / 1000;
		int min = sec / 60;
		sec = sec % 60;
		int hour = min / 60;
		min = min % 60;
		return new TATime(hour, min, sec);
	}

	private static void setAccumulate(Context context, String projectName, long duration)
	{
		getProjectSP(context, projectName).edit().putLong(TAG_TIME_ACCUMULATE, duration).commit();
	}

	private static long getLastTime(Context context, String name)
	{
		return getProjectSP(context, name).getLong(TAG_LAST_TOGGLE_TIME, Calendar.getInstance().getTimeInMillis());
	}

	private static void setLastTime(Context context, String name, long y)
	{
		getProjectSP(context, name).edit().putLong(TAG_LAST_TOGGLE_TIME, y).commit();
	}

	private static void setOnFlag(Context context, String name, boolean on)
	{
		getProjectSP(context, name).edit().putBoolean(TAG_IS_TIME_COMSUMING_FLAG, on).commit();
	}

	private static SharedPreferences getProjectSP(Context context, String name)
	{
		return context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	public static class ProjectCenter
	{
		private static final String TAG_ALL_PROJECTS = "spnames";
		private static final String TAG_PROJECT_CNT = "cnt";
		private static final String TAG_LAST_PROJECT = "TAG_LAST_NAME";
		private static final int INVALID_ID = -1;

		private static int getLastPrjoectId(Context context)
		{
			SharedPreferences x = context.getSharedPreferences(ProjectCenter.TAG_ALL_PROJECTS, Context.MODE_PRIVATE);
			return x.getInt(ProjectCenter.TAG_LAST_PROJECT, INVALID_ID);
		}

		public static void setLastProjectId(Context context, int id)
		{
			SharedPreferences x = context.getSharedPreferences(ProjectCenter.TAG_ALL_PROJECTS, Context.MODE_PRIVATE);
			x.edit().putInt(TAG_LAST_PROJECT, id).commit();
		}

		public static String[] getProjectNames(Context context)
		{
			SharedPreferences x = context.getSharedPreferences(ProjectCenter.TAG_ALL_PROJECTS, Context.MODE_PRIVATE);
			int cnt = x.getInt(ProjectCenter.TAG_PROJECT_CNT, 0);
			if (cnt <= 0)
			{
				return new String[] {};
			}
			ArrayList<String> arr = new ArrayList<String>();
			for (int i = 0; i < cnt; i++)
			{
				String name = x.getString(ProjectCenter.makeKey(i), null);
				if (name != null)
				{
					arr.add(name);
				}
			}
			return arr.toArray(new String[] {});
		}

		public static String getLastProjectName(Context context)
		{
			return getProjectNameById(context, getLastPrjoectId(context));
		}

		public static void removeProjectName(Context context, String name)
		{
			if (name == null || name.length() == 0)
			{
				return;
			}

			int id = getProjectIdByName(context, name);
			if (id == INVALID_ID)
			{
				return;
			}

			SharedPreferences x = context.getSharedPreferences(ProjectCenter.TAG_ALL_PROJECTS, Context.MODE_PRIVATE);
			x.edit().putString(ProjectCenter.makeKey(id), null).commit();
		}

		public static int addProjectName(Context context, String name)
		{
			if (name == null || name.length() == 0)
			{
				return ProjectCenter.INVALID_ID;
			}

			int id = getProjectIdByName(context, name);
			if (id == INVALID_ID)
			{
				SharedPreferences x = context.getSharedPreferences(ProjectCenter.TAG_ALL_PROJECTS, Context.MODE_PRIVATE);
				int cnt = x.getInt(ProjectCenter.TAG_PROJECT_CNT, 0);
				id = cnt;
				x.edit().putString(ProjectCenter.makeKey(id), name).commit();
				x.edit().putInt(ProjectCenter.TAG_PROJECT_CNT, cnt + 1).commit();
			}
			return id;
		}

		public static String getProjectNameById(Context context, int id)
		{
			String retErr = "";
			if (id == INVALID_ID)
			{
				return retErr;
			}
			return context.getSharedPreferences(ProjectCenter.TAG_ALL_PROJECTS, Context.MODE_PRIVATE).getString(ProjectCenter.makeKey(id),
					null);
		}

		public static int getProjectIdByName(Context context, String name)
		{
			if (name == null)
			{
				return INVALID_ID;
			}

			SharedPreferences x = context.getSharedPreferences(ProjectCenter.TAG_ALL_PROJECTS, Context.MODE_PRIVATE);
			int cnt = x.getInt(ProjectCenter.TAG_PROJECT_CNT, 0);
			for (int i = 0; i < cnt; i++)
			{
				String aname = x.getString(ProjectCenter.makeKey(i), null);
				if (aname != null && aname.equalsIgnoreCase(name))
				{
					return i;
				}
			}
			return INVALID_ID;
		}

		private static String makeKey(int i)
		{
			return "" + i;
		}
	}
}