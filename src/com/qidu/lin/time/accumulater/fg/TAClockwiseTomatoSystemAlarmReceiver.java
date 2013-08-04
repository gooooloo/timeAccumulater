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

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.qidu.lin.time.accumulater.R;
import com.qidu.lin.time.accumulater.bg.TAClockwiseTomatoCSVParser;
import com.qidu.lin.time.accumulater.bg.TADataCenter;
import com.qidu.lin.time.accumulater.bg.TASystemAlarmRecordParser;
import com.qidu.lin.time.accumulater.bg.TATomato;
import com.qidu.lin.time.accumulater.bg.TATomatoPersistence;

public class TAClockwiseTomatoSystemAlarmReceiver extends Activity
{
	private enum DataSource
	{
		none, ClockwiseTomato, SystemAlarm,
	}

	private class TomatoListReverseWithSource
	{
		final public DataSource source;
		final List<TATomato> tomatoListReverse;

		public TomatoListReverseWithSource(DataSource source, List<TATomato> tomatoListReverse)
		{
			this.source = source;
			this.tomatoListReverse = tomatoListReverse;
		}
	}

	private class FilterRules
	{
		public boolean unaccumalatedOnly = false;
		public boolean within2DaysOnly = false;
	}

	private static final String DURATION = "DURATION";
	private static final int selectdode = 0;

	private static final String TOMATO_ID = "TOMATO_ID";
	private static final String TOMATO_BEGIN_MS = "TOMATO_BEGIN_MS";
	private static final String TOMATO_END_MS = "TOMATO_END_MS";
	private static final String TOMATO_SOURCE = "TOMATO_SOURCE";

	FilterRules filterRules = new FilterRules();
	List<TATomato> tomatoListReverse = null;
	DataSource source = DataSource.none;

	private boolean filterOff(TATomato tomato, FilterRules filterRules)
	{
		if (filterRules.unaccumalatedOnly)
		{
			if (TATomatoPersistence.getProjectName(this, tomato.getId()) != null)
			{
				return true;
			}
		}

		if (filterRules.within2DaysOnly)
		{
			long msUntilNow = System.currentTimeMillis() - tomato.startMs;
			int msPerDay = 1000 * 60 * 60 * 24;
			if (msUntilNow > msPerDay * 2)
			{
				return true;
			}
		}

		return false;
	}

	private void filterTomatoListByUISettings(List<TATomato> tomatoListReverse2, FilterRules filterRules)
	{
		for (int i = tomatoListReverse2.size() - 1; i >= 0; i--)
		{
			if (filterOff(tomatoListReverse2.get(i), filterRules))
			{
				tomatoListReverse2.remove(i);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == selectdode && resultCode == Activity.RESULT_OK)
		{
			int projectId = data.getIntExtra(TASelect.ID, 0);
			int duration = data.getIntExtra(DURATION, 0);
			long tomatoId = data.getLongExtra(TOMATO_ID, 0);
			long beginMs = data.getLongExtra(TOMATO_BEGIN_MS, 0);
			long endMs = data.getLongExtra(TOMATO_END_MS, 0);
			int source = data.getIntExtra(TOMATO_SOURCE, 0);
			String projectName = TADataCenter.ProjectCenter.getProjectNameById(this, projectId);
			TADataCenter.addPastTimeToAccumulate(this, projectName, duration);
			TADataCenter.saveATomato(this, projectName, beginMs, endMs);
			TATomatoPersistence.saveProjectName(this, tomatoId, projectName);
			TATomatoPersistence.saveTomatoNote(this, tomatoId, source == DataSource.ClockwiseTomato.ordinal() ? "tomato"
					: source == DataSource.SystemAlarm.ordinal() ? "alarm" : "");

			Toast.makeText(this, "time accumulated!", Toast.LENGTH_SHORT).show();

			updateUI();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent_filter_receiver);

		TomatoListReverseWithSource data = parseTomatoListReverseAndSource();
		if (data == null || data.tomatoListReverse == null)
		{
			finish();
			return;
		}
		tomatoListReverse = data.tomatoListReverse;
		source = data.source;

		updateUI();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_intent_filter_receiver, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menu_unaccumulatedOnly:
			filterRules.unaccumalatedOnly = true;
			updateUI();
			break;
		case R.id.menu_within2daysonly:
			filterRules.within2DaysOnly = true;
			updateUI();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	protected void onTomatoClicked(TATomato y)
	{
		Intent intent = new Intent(this, TASelect.class);
		intent.putExtra(DURATION, y.getDurationMs());
		intent.putExtra(TOMATO_ID, y.getId());
		intent.putExtra(TOMATO_BEGIN_MS, y.startMs);
		intent.putExtra(TOMATO_END_MS, y.endMs);
		intent.putExtra(TOMATO_SOURCE, source.ordinal());
		startActivityForResult(intent, selectdode);
	}

	private TomatoListReverseWithSource parseTomatoListReverseAndSource()
	{
		Intent intent = getIntent();
		assert (intent.getType().equalsIgnoreCase("text/plain"));
		Uri stream = intent.getParcelableExtra(Intent.EXTRA_STREAM);

		List<TATomato> tomatoListReverse = null;
		DataSource source = null;
		try
		{
			tomatoListReverse = TAClockwiseTomatoCSVParser.parseInReverse(stream);
			source = DataSource.ClockwiseTomato;
		}
		catch (IOException e)
		{
			String content = intent.getStringExtra(Intent.EXTRA_TEXT);
			tomatoListReverse = TASystemAlarmRecordParser.parse(content);
			source = DataSource.SystemAlarm;
		}

		return new TomatoListReverseWithSource(source, tomatoListReverse);
	}

	private void updateUI()
	{
		ViewGroup root = (ViewGroup) findViewById(R.id.root);
		root.removeAllViews();

		filterTomatoListByUISettings(tomatoListReverse, filterRules);

		int index = tomatoListReverse.size();
		for (final TATomato y : tomatoListReverse)
		{
			Button btn = new Button(this);
			final String projectName = TATomatoPersistence.getProjectName(this, y.getId());
			String text = "#" + index + " " + y.getStartTimeString() + "  " + y.getDurationString();
			if (projectName != null)
			{
				text += " " + projectName;
			}
			btn.setText(text);
			root.addView(btn);

			btn.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (projectName != null)
					{
						Toast.makeText(TAClockwiseTomatoSystemAlarmReceiver.this, "dont support modify project yet", Toast.LENGTH_SHORT)
								.show();
						return;
					}

					onTomatoClicked(y);
				}
			});
			btn.setOnLongClickListener(new View.OnLongClickListener()
			{
				@Override
				public boolean onLongClick(View v)
				{
					if (projectName != null)
					{
						startActivity(TATomatoHistoryListActivity.getLauncherIntent(TAClockwiseTomatoSystemAlarmReceiver.this, projectName));
					}
					return true;
				}
			});
			index--;
		}
	}

}
