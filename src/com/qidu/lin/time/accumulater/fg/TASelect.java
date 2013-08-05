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
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.qidu.lin.time.accumulater.R;
import com.qidu.lin.time.accumulater.bg.TADataCenter;
import com.qidu.lin.time.accumulater.bg.TATime;

public class TASelect extends Activity
{
	private final class ProjectListAdapter implements ListAdapter
	{
		private final String[] names;

		private ProjectListAdapter(String[] names)
		{
			this.names = names;
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer)
		{

		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer)
		{

		}

		@Override
		public boolean isEmpty()
		{
			return false;
		}

		@Override
		public boolean hasStableIds()
		{
			return false;
		}

		@Override
		public int getViewTypeCount()
		{
			return 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			TextView tv = (convertView != null) ? ((TextView) convertView) : new TextView(TASelect.this);
			String projectName = getProjectName(position);
			TASharedListItemSetting.setupListItemTextView(tv, projectName);

			// TODO : make UI better
			TATime x = TADataCenter.getAccumulateTime(TASelect.this, projectName);
			tv.setText(projectName + "         " + TASelect.this.getString(R.string.timeResultShort, x.hours, x.minute, x.second));

			return tv;
		}

		public String getProjectName(int position)
		{
			return (String) getItem(position);
		}

		@Override
		public int getItemViewType(int position)
		{
			return 0;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public Object getItem(int position)
		{
			return names[position];
		}

		@Override
		public int getCount()
		{
			return names.length;
		}

		@Override
		public boolean isEnabled(int position)
		{
			return true;
		}

		@Override
		public boolean areAllItemsEnabled()
		{
			return false;
		}
	}

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
		lv.setAdapter(new ProjectListAdapter(TADataCenter.ProjectCenter.getProjectNames(this)));
		lv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
			{
				ProjectListAdapter listAdapter = (ProjectListAdapter) lv.getAdapter();
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
						ProjectListAdapter listAdapter = (ProjectListAdapter) lv.getAdapter();
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
											lv.setAdapter(new ProjectListAdapter(TADataCenter.ProjectCenter.getProjectNames(TASelect.this)));
										}
									}).setNegativeButton(android.R.string.cancel, null).show();
						}
						else if (which == index_delete)
						{
							TADataCenter.deleteAllTomatoForProject(context, projectName);
							TADataCenter.ProjectCenter.removeProjectName(context, projectName);

							// force UI refresh
							lv.setAdapter(new ProjectListAdapter(TADataCenter.ProjectCenter.getProjectNames(TASelect.this)));
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