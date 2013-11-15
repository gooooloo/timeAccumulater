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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

public class TADataCenter
{
	public static boolean getOnFlag(Context context, String projectName)
	{
		return TATomatoPersistence.loadOnFlag(context, projectName);
	}

	public static void Toggle(Context context, String projectName)
	{
		boolean beforeIsOn = getOnFlag(context, projectName);

		long endTimeMs = Calendar.getInstance().getTimeInMillis();

		if (beforeIsOn)
		{
			long startTimeMs = getLastTime(context, projectName);
			long durationTimeMs = endTimeMs - startTimeMs;
			addPastTimeToAccumulate(context, projectName, durationTimeMs);
			setATomato(context, projectName, startTimeMs, endTimeMs);
			TATomatoPersistence.saveProjectName(context, new TATomato(startTimeMs, endTimeMs).getId(), projectName);
		}
		setLastTime(context, projectName, endTimeMs);
		setOnFlag(context, projectName, !beforeIsOn);

	}

	public static void setATomato(Context context, String projectName, long beginMs, long endMs)
	{
		TATomatoPersistence.saveATomato(context, projectName, beginMs, endMs);
	}

	public static void moveAllTomatoToAnotherProject(Context context, String projectNameSrc, String projectNameDesc)
	{
		TATomatoPersistence.moveAllTomatoToAnotherProject(context, projectNameSrc, projectNameDesc);
	}

	public static void deleteAllTomatoForProject(Context context, String projectName)
	{
		TATomatoPersistence.deleteAllTomatoForProject(context, projectName);
	}

	public static List<TATomato> getAllReverseTomatosWithinHours(Context context, int withinHoursNum)
	{
		final int msWithin = 1000 * 60 * 60 * withinHoursNum;
		final long nt = System.currentTimeMillis();
		String[] pns = ProjectCenter.getProjectNames(context);
		List<TATomato> ret = new ArrayList<TATomato>();
		for (String pn : pns)
		{
			long lt = getLastTime(context, pn);
			if (nt - lt > msWithin)
			{
				continue;
			}

			List<TATomato> list = getReverseTomatoListForProject(context, pn);
			for (TATomato tomato : list)
			{
				if (nt - tomato.startMs > msWithin)
				{
					continue;
				}
				ret.add(tomato);
			}
		}

		Comparator<TATomato> comparator = new Comparator<TATomato>()
		{

			@Override
			public int compare(TATomato lhs, TATomato rhs)
			{
				return (int) (rhs.startMs - lhs.startMs);
			}
		};
		Collections.sort(ret, comparator);
		return ret;
	}

	public static List<TATomato> getReverseTomatoListForProject(Context context, String projectName)
	{
		return TATomatoPersistence.loadReverseTomatoListForProject(context, projectName);
	}

	public static void addPastTimeToAccumulate(Context context, String projectName, long pastTimeMs)
	{
		setAccumulate(context, projectName, pastTimeMs + getAccumulateMs(context, projectName));
	}

	public static long getAccumulateMs(Context context, String projectName)
	{
		return TATomatoPersistence.loadAccumulateMs(context, projectName);
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
		TATomatoPersistence.saveAccumulate(context, projectName, duration);
	}

	private static long getLastTime(Context context, String name)
	{
		return TATomatoPersistence.loadLastTime(context, name);
	}

	private static void setLastTime(Context context, String name, long y)
	{
		TATomatoPersistence.saveLastTime(context, name, y);
	}

	private static void setOnFlag(Context context, String name, boolean on)
	{
		TATomatoPersistence.saveOnFlag(context, name, on);
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

		public static void changeProjectName(Context context, String nameSrc, String nameDesc)
		{
			if (nameSrc == null || nameSrc.length() == 0)
			{
				return;
			}

			if (getProjectIdByName(context, nameDesc) != INVALID_ID)
			{
				// don:t support change to existing
				return;
			}

			int id = getProjectIdByName(context, nameSrc);
			if (id == INVALID_ID)
			{
				return;
			}

			SharedPreferences x = context.getSharedPreferences(ProjectCenter.TAG_ALL_PROJECTS, Context.MODE_PRIVATE);
			x.edit().putString(ProjectCenter.makeKey(id), nameDesc).commit();
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