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

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.qidu.lin.time.accumulater.R;
import com.qidu.lin.time.accumulater.bg.TADataCenter;
import com.qidu.lin.time.accumulater.bg.TATime;
import com.qidu.lin.time.accumulater.bg.TATomato;
import com.qidu.lin.time.accumulater.bg.TATomato.StringFilter;
import com.qidu.lin.time.accumulater.bg.TATomatoPersistence;

public class TATomatoHistoryListActivity extends Activity
{
	private static final String TAG_PROJECT_NAME = "TAG_PROJECT_NAME";
	private static final String TAG_ONLY_PAST_24_HOURS = "TAG_ONLY_PAST_24_HOURS";
	private static final String NAME_PRESENTING_ALL_PROJECTS = "thisIsATagPresentingAllProjectsAndWeAssumeNoRealProjectWillUseThisNameSoNoConflict";

	private final class TomatoListAdapter implements ListAdapter
	{
		List<TATomato> list = null;

		public TomatoListAdapter(List<TATomato> list)
		{
			this.list = list;
		}

		@Override
		public int getCount()
		{
			return list.size();
		}

		@Override
		public Object getItem(int position)
		{
			return list.get(position);
		}

		public TATomato getTomato(int position)
		{
			return (TATomato) getItem(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public int getItemViewType(int position)
		{
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View v = null;
			TATomato item = getTomato(position);
			Context context = TATomatoHistoryListActivity.this;
			if (convertView == null)
			{
				v = getLayoutInflater().inflate(R.layout.tomato, null);
			}
			else
			{
				v = convertView;
			}

			((TextView) v.findViewById(R.id.date)).setText(item.getString(context, StringFilter.StartDate));
			((TextView) v.findViewById(R.id.start)).setText(item.getString(context, StringFilter.StartTime));
			((TextView) v.findViewById(R.id.end)).setText(item.getString(context, StringFilter.EndTime));
			((TextView) v.findViewById(R.id.duration)).setText(item.getString(context, StringFilter.Duration));

			String tomatoNote = TATomatoPersistence.getTomatoNote(context, item.getId());
			if (tomatoNote == null)
			{
				tomatoNote = "";
			}
			((TextView) v.findViewById(R.id.note)).setText(tomatoNote);

			Date date = new Date();
			date.setTime(item.startMs);
			date.setHours(0);
			date.setMinutes(0);
			date.setSeconds(0);
			int msPerDay = 1000 * 60 * 60 * 24;
			float x1 = item.startMs - date.getTime();
			x1 /= msPerDay;
			float x2 = item.getDurationMs();
			x2 /= msPerDay;

			setWeight(v, R.id.pastInDay, x1);
			setWeight(v, R.id.nowInDay, x2);
			setWeight(v, R.id.futureInDay, 1 - x1 - x2);
			return v;
		}

		private void setWeight(View v, int id, float w)
		{
			View vv = v.findViewById(id);
			LinearLayout.LayoutParams xxx = (android.widget.LinearLayout.LayoutParams) vv.getLayoutParams();
			xxx.weight = w;
			vv.setLayoutParams(xxx);
		}

		@Override
		public int getViewTypeCount()
		{
			return 1;
		}

		@Override
		public boolean hasStableIds()
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEmpty()
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public boolean areAllItemsEnabled()
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEnabled(int position)
		{
			// TODO Auto-generated method stub
			return true;
		}

	}

	public static Intent getLauncherIntent(Context context, String projectName)
	{
		return getLauncherIntent(context, projectName, false);
	}

	public static Intent getLauncherIntentForPast24Hours(Context context)
	{
		return getLauncherIntent(context, NAME_PRESENTING_ALL_PROJECTS, true);
	}

	private static Intent getLauncherIntent(Context context, String projectName, boolean onlyPast24Hours)
	{
		Intent intent = new Intent(context, TATomatoHistoryListActivity.class);
		intent.putExtra(TAG_PROJECT_NAME, projectName);
		intent.putExtra(TAG_ONLY_PAST_24_HOURS, onlyPast24Hours);
		return intent;
	}

	private String projectName;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tatomato_history_list);

		this.projectName = getIntent().getStringExtra(TAG_PROJECT_NAME);
		if (projectName == null)
		{
			finish();
			return;
		}

		List<TATomato> list = null;
		boolean forAllProjectsIn24Hours = false;
		if (projectName.equals(NAME_PRESENTING_ALL_PROJECTS))
		{
			forAllProjectsIn24Hours = getIntent().getBooleanExtra(TAG_ONLY_PAST_24_HOURS, false);
			if (forAllProjectsIn24Hours)
			{
				list = TADataCenter.getAllReverseTomatosWithin24Hours(this);
			}
			else
			{
				// do nothing.
			}
		}
		else
		{
			list = TADataCenter.getReverseTomatoListForProject(this, projectName);
		}

		if (list == null)
		{
			finish();
			return;
		}

		ListView lv = (ListView) findViewById(R.id.listView);
		final TomatoListAdapter adapter = new TomatoListAdapter(list);
		lv.setAdapter(adapter);
		TATime x = TADataCenter.getAccumulateTime(this, projectName);

		if (forAllProjectsIn24Hours)
		{
			this.setTitle(getString(R.string.past_24_hours));
		}
		else
		{
			this.setTitle(projectName + "  " + getString(R.string.timeResultShort, x.hours, x.minute, x.second));
		}

		lv.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3)
			{
				final Context context = TATomatoHistoryListActivity.this;
				final LinearLayout ll = new LinearLayout(context);
				ll.setOrientation(LinearLayout.VERTICAL);
				final EditText editText = new EditText(context);
				ll.addView(editText);
				RatingBar ratingBar = new RatingBar(context);
				ll.addView(ratingBar);

				editText.setHint(R.string.input_tomato_note_hint);

				ratingBar.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				ratingBar.setNumStars(5);
				ratingBar.setRating(3);
				ratingBar.setStepSize(1.0f);
				final long tomatoId = adapter.getTomato(position).getId();
				String note = TATomatoPersistence.getTomatoNote(context, tomatoId);
				if (note != null)
				{
					editText.setText(note);
				}
				new AlertDialog.Builder(context).setTitle(R.string.input_tomato_note_hint).setView(ll)
						.setPositiveButton(android.R.string.ok, new OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								TATomatoPersistence.saveTomatoNote(context, tomatoId, editText.getText().toString());
							}
						}).setNegativeButton(android.R.string.cancel, null).show();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tatomato_history_list, menu);
		return true;
	}

}
