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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.qidu.lin.time.accumulater.R;
import com.qidu.lin.time.accumulater.bg.TADataCenter;

public class TASelect extends Activity
{
	public static final String ID = "com.qidu.lin.time.accumulater.id";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select);

		final EditText input = (EditText) this.findViewById(R.id.input);
		final Button addButton = (Button) this.findViewById(R.id.add);
		addButton.setOnClickListener(new OnClickListener()
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
					addButton.setText(android.R.string.ok);
				}

			}
		});

		final ListView lv = (ListView) findViewById(R.id.listView);
		lv.setAdapter(new TAProjectListAdapter(this, TADataCenter.ProjectCenter.getProjectNames(this)));
		lv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
			{
				TAProjectListAdapter listAdapter = (TAProjectListAdapter) lv.getAdapter();
				String projectName = listAdapter.getProjectName(position);

				onSelect(TADataCenter.ProjectCenter.getProjectIdByName(TASelect.this, projectName));
			}
		});
		lv.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3)
			{
				final Context context = TASelect.this;

				final int index_history = 0;
				final int index_rename = 1;
				final int index_delete = 2;
				final int index_count = index_delete + 1;
				CharSequence[] xx = new CharSequence[index_count];
				xx[index_history] = context.getString(R.string.history);
				xx[index_rename] = context.getString(R.string.rename);
				xx[index_delete] = context.getString(R.string.delete);

				new AlertDialog.Builder(context).setItems(xx, new android.content.DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						TAProjectListAdapter listAdapter = (TAProjectListAdapter) lv.getAdapter();
						final String projectName = listAdapter.getProjectName(position);

						if (which == index_history)
						{
							startActivity(new Intent(TATomatoHistoryListActivity.getLauncherIntent(TASelect.this, projectName)));
						}
						else if (which == index_rename)
						{
							final EditText editText = new EditText(context);
							editText.setText(projectName);
							new AlertDialog.Builder(context).setTitle(R.string.rename).setView(editText)
									.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
									{

										@Override
										public void onClick(DialogInterface dialog, int which)
										{
											String projectNameNew = editText.getText().toString();
											TADataCenter.moveTomatoToAnotherProject(context, projectName, projectNameNew);
											TADataCenter.ProjectCenter.changeProjectName(context, projectName, projectNameNew);

											// force UI refresh
											lv.setAdapter(new TAProjectListAdapter(TASelect.this, TADataCenter.ProjectCenter.getProjectNames(TASelect.this)));
										}
									}).setNegativeButton(android.R.string.cancel, null).show();
						}
						else if (which == index_delete)
						{
							TADataCenter.deleteAllTomatoForProject(context, projectName);
							TADataCenter.ProjectCenter.removeProjectName(context, projectName);

							// force UI refresh
							lv.setAdapter(new TAProjectListAdapter(TASelect.this, TADataCenter.ProjectCenter.getProjectNames(TASelect.this)));
						}
					}
				}).show();
				return true;
			}
		});
	}

	private void onSelect(int id)
	{
		Intent intent = getIntent();
		intent.putExtra(ID, id);
		setResult(RESULT_OK, intent);
		TASelect.this.finish();
	}
}