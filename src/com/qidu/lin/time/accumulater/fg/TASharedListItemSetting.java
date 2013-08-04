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

package com.qidu.lin.time.accumulater.fg;

import android.widget.TextView;

import com.qidu.lin.time.accumulater.R;
import com.qidu.lin.time.accumulater.bg.TADataCenter;

public class TASharedListItemSetting
{
	public static void setupListItemTextView(TextView tv, String projectName)
	{
		// FIXME : dimensions are hard coded.
		tv.setTextSize(24);
		tv.setPadding(10, 10, 10, 10);
		int colorForOn = tv.getResources().getColor(R.color.colorForOn);
		int colorForOff = tv.getResources().getColor(R.color.colorForOff);
		tv.setTextColor(TADataCenter.getOnFlag(tv.getContext(), projectName) ? colorForOn : colorForOff);
	}
}
