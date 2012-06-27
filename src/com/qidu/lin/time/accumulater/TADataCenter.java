/**
 * @copyright: qiduLin
 * 
 */

package com.qidu.lin.time.accumulater;

import android.content.Context;
import android.content.SharedPreferences;

public class TADataCenter
{
	private static final String TAG = "abc";
	private static final String TAG2 = "def";

	public static boolean getOnFlag(Context context)
	{
		return TADataCenter.getSP(context).getBoolean(TAG2, false);
	}

	public static void setOnFlag(Context context, boolean on)
	{
		TADataCenter.getSP(context).edit().putBoolean(TAG2, on).commit();
	}

	private static SharedPreferences getSP(Context context)
	{
		SharedPreferences x = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		return x;
	}

	public static void Toggle(Context context)
	{
		setOnFlag(context, !getOnFlag(context));
	}

}