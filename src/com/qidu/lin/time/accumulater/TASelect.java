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
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class TASelect extends Activity
{
	public static final String ID = "com.qidu.lin.time.accumulater.id";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select);

		TableLayout tableLayout = (TableLayout) this.findViewById(R.id.table);

		final EditText input = (EditText) this.findViewById(R.id.input);
		((Button) this.findViewById(R.id.add)).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (input.getVisibility() == View.VISIBLE)
				{
					int id = TADataCenter.ProjectCenter.addProjectName(TASelect.this, input.getText().toString());
					TASelect.this.onSelect(id);
				}
				else
				{
					input.setVisibility(View.VISIBLE);
				}
			}
		});

		String[] names = TADataCenter.ProjectCenter.getProjectNames(this);
		if (names != null && names.length != 0)
		{
			TableRow tr = new TableRow(this);
			for (int i = 0; i < names.length; i++)
			{
				final int numARow = 4;
				Button btn = new Button(this);
				btn.setText(names[i]);
				final int id = i;
				btn.setTextColor(TADataCenter.getOnFlag(this, names[i])
						? this.getResources().getColor(R.color.colorForOn)
								: this.getResources().getColor(R.color.colorForOff));
				btn.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						TASelect.this.onSelect(id);
					}
				});
				btn.setOnLongClickListener(new OnLongClickListener()
				{

					@Override
					public boolean onLongClick(View v)
					{
						Toast.makeText(TASelect.this, "Long click feature is to be done", Toast.LENGTH_SHORT).show();
						return true;
					}
				});
				tr.addView(btn);

				if (tr.getChildCount() == numARow)
				{
					tableLayout.addView(tr);
					tr = new TableRow(this);
				}
			}
			if (tr.getChildCount() != 0)
			{
				tableLayout.addView(tr);
			}
		}
	}

	private void onSelect(int id)
	{
		Intent intent = getIntent();
		intent.putExtra(ID, id);
		setResult(RESULT_OK, intent);
		TASelect.this.finish();
	}
}