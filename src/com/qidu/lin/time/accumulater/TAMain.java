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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TAMain extends Activity
{
	protected static final int selectdode = 0;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		((Button) this.findViewById(R.id.btn)).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				TADataCenter.Toggle(TAMain.this, TADataCenter.ProjectCenter.getLastProjectName(TAMain.this));
				TAMain.this.updateUI();
			}
		});

		((Button) this.findViewById(R.id.project)).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				TAMain.this.startActivityForResult(new Intent(TAMain.this, TASelect.class), TAMain.selectdode);
			}
		});

		((Button) this.findViewById(R.id.project)).setOnLongClickListener(new OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				startActivity(new Intent(TAMain.this, TATomatoHistoryListActivity.class));
				return true;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == TAMain.selectdode && resultCode == Activity.RESULT_OK)
		{
			int id = data.getIntExtra(TASelect.ID, 0);
			TADataCenter.ProjectCenter.setLastProjectId(this, id);
			this.updateUI();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		this.updateUI();
	}

	private void updateUI()
	{

		String spName = TADataCenter.ProjectCenter.getLastProjectName(this);

		((Button) this.findViewById(R.id.project)).setText(spName + this.getString(R.string.chooseProject));
		((Button) this.findViewById(R.id.btn)).setText(TADataCenter.getOnFlag(this, spName) ? R.string.setTimerOff : R.string.setTimerOn);
		this.updateText();

	}

	private void updateText()
	{
		TextView tv = (TextView) this.findViewById(R.id.tv);

		if (TADataCenter.getOnFlag(this, TADataCenter.ProjectCenter.getLastProjectName(this)))
		{
			tv.setText(R.string.timeAccumulating);
		}
		else
		{
			TATime x = TADataCenter.getAccumulateTime(this, TADataCenter.ProjectCenter.getLastProjectName(this));
			tv.setText(this.getString(R.string.timeResult, x.hours, x.minute, x.second));
		}
	}
}