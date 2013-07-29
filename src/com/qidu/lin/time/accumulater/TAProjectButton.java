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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class TAProjectButton extends Button
{

	public static final int selectdode = 1;

	public TAProjectButton(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public TAProjectButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public TAProjectButton(Context context)
	{
		super(context);
	}
	
	public void setup(final Activity activity)
	{

		((Button) this.findViewById(R.id.project)).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				activity.startActivityForResult(new Intent(activity, TASelect.class), TAProjectButton.selectdode);
			}
		});

		((Button) this.findViewById(R.id.project)).setOnLongClickListener(new OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				activity.startActivity(TATomatoHistoryListActivity.getLauncherIntent(activity,
						TADataCenter.ProjectCenter.getLastProjectName(activity)));
				return true;
			}
		});
	}

}
