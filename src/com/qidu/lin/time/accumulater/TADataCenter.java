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

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;

public class TADataCenter
{
	private static final String TAG_IS_TIME_COMSUMING_FLAG = "com.qidu.lin.timeAccumulate.TAG_IS_TIME_COMSUMING_FLAG";
	private static final String TAG_TIME_ACCUMULATE = "com.qidu.lin.timeAccumulate.TAG_TIME_ACCUMULATE";
	private static final String TAG_LAST_TOGGLE_TIME = "com.qidu.lin.timeAccumulate.TAG_LAST_TOGGLE_TIME";

	static boolean getOnFlag(Context context, String name)
	{

		return TADataCenter.getSP(context, name).getBoolean(TAG_IS_TIME_COMSUMING_FLAG, false);
	}

	public static void Toggle(Context context, String name)
	{
		boolean beforeIsOn = getOnFlag(context, name);

		Calendar x = Calendar.getInstance();
		long y = x.getTimeInMillis();

		if (beforeIsOn)
		{
			long yy = getLastTime(context, name);
			long yyy = y - yy;
			setAccumulate(context, name, yyy + getAccumulate(context, name));
		}
		setLastTime(context, name, y);
		setOnFlag(context, name, !beforeIsOn);

	}

	public static long getAccumulate(Context context, String name)
	{
		// TODO Auto-generated method stub

		long x = getSP(context, name).getLong(TAG_TIME_ACCUMULATE, 0);
		return x;
	}

	private static void setAccumulate(Context context, String name, long yyy)
	{
		// TODO Auto-generated method stub
		getSP(context, name).edit().putLong(TAG_TIME_ACCUMULATE, yyy).commit();
	}

	private static long getLastTime(Context context, String name)
	{
		return getSP(context, name).getLong(TAG_LAST_TOGGLE_TIME, Calendar.getInstance().getTimeInMillis());
	}

	private static void setLastTime(Context context, String name, long y)
	{
		getSP(context, name).edit().putLong(TAG_LAST_TOGGLE_TIME, y).commit();
	}

	private static void setOnFlag(Context context, String name, boolean on)
	{
		TADataCenter.getSP(context, name).edit().putBoolean(TAG_IS_TIME_COMSUMING_FLAG, on).commit();
	}

	private static SharedPreferences getSP(Context context, String name)
	{
		SharedPreferences x = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		return x;
	}

	public static class SPCenter
	{
		private static final String TAG = "spnames";
		private static final String TAG_CNT = "cnt";
		private static final String TAG_LAST_NAME = "TAG_LAST_NAME";
		private static final int INVALID_ID = -1;

		private static int getLastNameId(Context context)
		{
			SharedPreferences x = context.getSharedPreferences(SPCenter.TAG, Context.MODE_PRIVATE);
			return x.getInt(SPCenter.TAG_LAST_NAME, INVALID_ID);
		}

		public static void setLastNameId(Context context, int id)
		{
			SharedPreferences x = context.getSharedPreferences(SPCenter.TAG, Context.MODE_PRIVATE);
			x.edit().putInt(TAG_LAST_NAME, id).commit();
		}

		public static String[] getSPNames(Context context)
		{
			SharedPreferences x = context.getSharedPreferences(SPCenter.TAG, Context.MODE_PRIVATE);
			int cnt = x.getInt(SPCenter.TAG_CNT, 0);
			if (cnt <= 0)
			{
				return new String[] {};
			}
			String[] rlt = new String[cnt];
			for (int i = 0; i < cnt; i++)
			{
				rlt[i] = x.getString(SPCenter.makeKey(i), "");
			}
			return rlt;
		}

		public static String getLastSPName(Context context)
		{
			return getSPNameById(context, getLastNameId(context));
		}

		/**
		 * @param context
		 * @param name
		 * @return the ID of the SPName
		 */
		public static int addSPName(Context context, String name)
		{
			if (name == null || name.length() == 0)
			{
				return SPCenter.INVALID_ID;
			}
			String names[] = getSPNames(context);
			for (int i = 0; names != null && i < names.length; i++)
			{
				if (name.equalsIgnoreCase(names[i]))
				{
					return i;
				}
			}
			SharedPreferences x = context.getSharedPreferences(SPCenter.TAG, Context.MODE_PRIVATE);
			int cnt = names.length;
			x.edit().putString(SPCenter.makeKey(cnt), name).commit();
			x.edit().putInt(SPCenter.TAG_CNT, cnt + 1).commit();
			return cnt;
		}

		private static String getSPNameById(Context context, int id)
		{
			String retErr = "";
			if (id == INVALID_ID)
			{
				return retErr;
			}
			String[] names = getSPNames(context);
			if (names != null && 0 <= id && id < names.length)
			{
				return names[id];
			}
			return retErr;
		}

		private static String makeKey(int i)
		{
			return "" + i;
		}
	}
}