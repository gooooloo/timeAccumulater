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

		long x =  getSP(context).getLong(TAG_TIME_ACCUMULATE, 0);
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

}