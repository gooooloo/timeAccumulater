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


import android.content.Context;
import android.content.SharedPreferences;


public class TAProjectPersistence
{

	static final String TAG_ALL_PROJECTS = "spnames";
	static final String TAG_PROJECT_CNT = "cnt";
	static final String TAG_LAST_PROJECT = "TAG_LAST_NAME";
	static final int INVALID_ID = -1;
	public static void saveLastProjectId(Context context, int id)
	{
		SharedPreferences x = context.getSharedPreferences(TAG_ALL_PROJECTS, Context.MODE_PRIVATE);
		x.edit().putInt(TAG_LAST_PROJECT, id).commit();
	}
	public static int loadLastPrjoectId(Context context)
	{
		SharedPreferences x = context.getSharedPreferences(TAG_ALL_PROJECTS, Context.MODE_PRIVATE);
		return x.getInt(TAG_LAST_PROJECT, INVALID_ID);
	}
	public static String[] loadProjectNames(Context context)
	{
		SharedPreferences x = context.getSharedPreferences(TAG_ALL_PROJECTS, Context.MODE_PRIVATE);
		int cnt = x.getInt(TAG_PROJECT_CNT, 0);
		if (cnt <= 0)
		{
			return new String[] {};
		}
		ArrayList<String> arr = new ArrayList<String>();
		for (int i = 0; i < cnt; i++)
		{
			String name = x.getString(TAProjectPersistence.makeKey(i), null);
			if (name != null)
			{
				arr.add(name);
			}
		}
		return arr.toArray(new String[] {});
	}
	static String makeKey(int i)
	{
		return "" + i;
	}
	public static void removeProjectNameById(Context context, int id)
	{
		SharedPreferences x = context.getSharedPreferences(TAG_ALL_PROJECTS, Context.MODE_PRIVATE);
		x.edit().putString(makeKey(id), null).commit();
	}
	public static void updateProjectNameById(Context context, String nameDesc, int id)
	{
		SharedPreferences x = context.getSharedPreferences(TAG_ALL_PROJECTS, Context.MODE_PRIVATE);
		x.edit().putString(makeKey(id), nameDesc).commit();
	}
	public static int addProjectNameAndGetNewId(Context context, String name)
	{
		int id;
		SharedPreferences x = context.getSharedPreferences(TAG_ALL_PROJECTS, Context.MODE_PRIVATE);
		int cnt = x.getInt(TAG_PROJECT_CNT, 0);
		id = cnt;
		x.edit().putString(makeKey(id), name).commit();
		x.edit().putInt(TAG_PROJECT_CNT, cnt + 1).commit();
		return id;
	}
	public static String loadProjectNameById(Context context, int id)
	{
		String retErr = "";
		if (id == INVALID_ID)
		{
			return retErr;
		}
		return context.getSharedPreferences(TAG_ALL_PROJECTS, Context.MODE_PRIVATE).getString(makeKey(id),
				null);
	}
	public static int loadProjectIdByName(Context context, String name)
	{
		if (name == null)
		{
			return INVALID_ID;
		}
	
		SharedPreferences x = context.getSharedPreferences(TAG_ALL_PROJECTS, Context.MODE_PRIVATE);
		int cnt = x.getInt(TAG_PROJECT_CNT, 0);
		for (int i = 0; i < cnt; i++)
		{
			String aname = x.getString(makeKey(i), null);
			if (aname != null && aname.equalsIgnoreCase(name))
			{
				return i;
			}
		}
		return INVALID_ID;
	}
}
