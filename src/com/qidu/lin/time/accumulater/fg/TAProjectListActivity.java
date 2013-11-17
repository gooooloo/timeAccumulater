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
import android.view.Menu;
import android.view.MenuItem;
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

public class TAProjectListActivity extends Activity
{
	public static final String ID = "com.qidu.lin.time.accumulater.id";
	private static final String TAG_PURPOSE = "TAG_PURPOSE";

	public enum ActivityPurpose
	{
		select, manage,
	}

	public static Intent getProjectsIntent(Context context, ActivityPurpose purpose)
	{
		Intent intent = new Intent(context, TAProjectListActivity.class);
		intent.putExtra(TAG_PURPOSE, purpose.ordinal());
		return intent;
	}

	// by default it is select.
	private ActivityPurpose purpose = ActivityPurpose.select;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select);
		purpose = (getIntent().getIntExtra(TAG_PURPOSE, ActivityPurpose.select.ordinal()) == ActivityPurpose.manage.ordinal()) ? ActivityPurpose.manage
				: ActivityPurpose.select;

		final EditText input = (EditText) this.findViewById(R.id.input);
		final Button addButton = (Button) this.findViewById(R.id.add);
		addButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (input.getVisibility() == View.VISIBLE)
				{
					int id = TADataCenter.ProjectCenter.addProjectName(TAProjectListActivity.this, input.getText().toString());
					TAProjectListActivity.this.onSelect(id);
				}
				else
				{
					input.setVisibility(View.VISIBLE);
					addButton.setText(android.R.string.ok);
				}

			}
		});

		final ListView lv = (ListView) findViewById(R.id.listView);
		final TAProjectListAdapter adapter = new TAProjectListAdapter(this);
		adapter.setItems(TADataCenter.ProjectCenter.getProjectNames(TAProjectListActivity.this));
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3)
			{
				final TAProjectListAdapter listAdapter = getListAdapter();
				String projectName = listAdapter.getProjectName(position);

				if (purpose == ActivityPurpose.select)
				{
					onSelect(TADataCenter.ProjectCenter.getProjectIdByName(TAProjectListActivity.this, projectName));
				}
				else if (purpose == ActivityPurpose.manage)
				{
					final Context context = TAProjectListActivity.this;

					final int index_rename = 0;
					final int index_delete = 1;
					final int index_count = index_delete + 1;
					CharSequence[] xx = new CharSequence[index_count];
					xx[index_rename] = context.getString(R.string.rename);
					xx[index_delete] = context.getString(R.string.delete);

					new AlertDialog.Builder(context).setItems(xx, new android.content.DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							final String projectName = listAdapter.getProjectName(position);

							if (which == index_rename)
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
												TADataCenter.moveAllTomatoToAnotherProject(context, projectName, projectNameNew);
												TADataCenter.ProjectCenter.changeProjectName(context, projectName, projectNameNew);

												adapter.setItems(TADataCenter.ProjectCenter.getProjectNames(TAProjectListActivity.this));
											}
										}).setNegativeButton(android.R.string.cancel, null).show();
							}
							else if (which == index_delete)
							{
								TADataCenter.deleteAllTomatoForProject(context, projectName);
								TADataCenter.ProjectCenter.removeProjectName(context, projectName);

								adapter.setItems(TADataCenter.ProjectCenter.getProjectNames(TAProjectListActivity.this));
							}
						}
					}).show();
				}
			}
		});
		lv.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3)
			{
				final String projectName = (getListAdapter()).getProjectName(position);
				startActivity(new Intent(TATomatoListActivity.getLauncherIntent(TAProjectListActivity.this, projectName)));

				return true;
			}
		});
	}

	private void onSelect(int id)
	{
		Intent intent = getIntent();
		intent.putExtra(ID, id);
		setResult(RESULT_OK, intent);
		TAProjectListActivity.this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_project_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menu_sort_by_total_time_less_to_more:
			this.sortByTimeFromLessToMore();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void sortByTimeFromLessToMore()
	{
		getListAdapter().sortByTimeLessToMore();
	}

	public TAProjectListAdapter getListAdapter()
	{
		final ListView lv = (ListView) findViewById(R.id.listView);
		final TAProjectListAdapter listAdapter = (TAProjectListAdapter) lv.getAdapter();
		return listAdapter;
	}

}