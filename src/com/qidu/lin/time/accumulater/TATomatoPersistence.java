package com.qidu.lin.time.accumulater;

import android.content.Context;

public class TATomatoPersistence
{
	private static final String KEY_TOMATO_ID = "KEY_TOMATO_ID_";
	private static final String TAG_TOMATO_PERSISTANCE = "TAG_TOMATO_PERSISTANCE";

	public static void save(Context context, long tomatoId, String projectName)
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

	private static boolean invalidId(long tomatoId)
	{
		return tomatoId <= 0;
	}

	private static String getKeyByTomatoId(long tomatoId)
	{
		return KEY_TOMATO_ID + tomatoId;
	}
}
