/**
 * @copyright: qiduLin
 * 
 */

package com.qidu.lin.time.accumulater;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;

public class TADataCenter
{
	private static final String TAG = "abc";
	private static final String TAG_IS_TIME_COMSUMING_FLAG = "com.qidu.lin.timeAccumulate.TAG_IS_TIME_COMSUMING_FLAG";
	private static final String TAG_TIME_ACCUMULATE = "com.qidu.lin.timeAccumulate.TAG_TIME_ACCUMULATE";
	private static final String TAG_LAST_TOGGLE_TIME = "com.qidu.lin.timeAccumulate.TAG_LAST_TOGGLE_TIME";

	static boolean getOnFlag(Context context)
	{

		return TADataCenter.getSP(context).getBoolean(TAG_IS_TIME_COMSUMING_FLAG, false);
	}

	private static void setOnFlag(Context context, boolean on)
	{
		TADataCenter.getSP(context).edit().putBoolean(TAG_IS_TIME_COMSUMING_FLAG, on).commit();
	}

	private static SharedPreferences getSP(Context context)
	{
		SharedPreferences x = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		return x;
	}

	public static void Toggle(Context context)
	{
		boolean beforeIsOn = getOnFlag(context);

		Calendar x = Calendar.getInstance();
		long y = x.getTimeInMillis();

		if (beforeIsOn)
		{
			long yy = getLastTime(context);
			long yyy = y - yy;
			setAccumulate(context, yyy + getAccumulate(context));
		}
		setLastTime(context, y);
		setOnFlag(context, !beforeIsOn);

	}

	public static long getAccumulate(Context context)
	{
		// TODO Auto-generated method stub

		long x = getSP(context).getLong(TAG_TIME_ACCUMULATE, 0);
		return x;
	}

	private static void setAccumulate(Context context, long yyy)
	{
		// TODO Auto-generated method stub
		getSP(context).edit().putLong(TAG_TIME_ACCUMULATE, yyy).commit();
	}

	private static long getLastTime(Context context)
	{
		return getSP(context).getLong(TAG_LAST_TOGGLE_TIME, Calendar.getInstance().getTimeInMillis());
	}

	private static void setLastTime(Context context, long y)
	{
		getSP(context).edit().putLong(TAG_LAST_TOGGLE_TIME, y).commit();
	}

	public static class SPCenter
	{
		private static final String TAG = null;
		private static final String TAG_CNT = "cnt";

		public static String[] getSPNames(Context context)
		{
			SharedPreferences x = context.getSharedPreferences(SPCenter.TAG, Context.MODE_PRIVATE);
			int cnt = x.getInt(SPCenter.TAG_CNT, 0);
			if (cnt <= 0)
			{
				return null;
			}
			String[] rlt = new String[cnt];
			for (int i = 0; i < cnt; i++)
			{
				rlt[i] = x.getString(SPCenter.makeKey(i), "");
			}
			return rlt;
		}

		public static void addSPName(Context context, String name)
		{
			if (name == null)
			{
				return;
			}
			SharedPreferences x = context.getSharedPreferences(SPCenter.TAG, Context.MODE_PRIVATE);
			int cnt = x.getInt(SPCenter.TAG_CNT, 0);
			x.edit().putString(SPCenter.makeKey(cnt), name);
		}

		private static String makeKey(int i)
		{
			return "" + i;
		}
	}
}