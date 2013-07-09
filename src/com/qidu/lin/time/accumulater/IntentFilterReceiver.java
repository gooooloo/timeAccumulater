package com.qidu.lin.time.accumulater;

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

public class IntentFilterReceiver extends Activity
{
	protected static final int selectdode = 0;
	private static final String DURATION = "DURATION";
	private static final String TOMATO_ID = "TOMATO_ID";

	private class FilterRules
	{
		public boolean unaccumalatedOnly = false;
		public boolean within2DaysOnly = false;
	}

	FilterRules filterRules = new FilterRules();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent_filter_receiver);

		updateUI();
	}

	private void updateUI()
	{
		ViewGroup root = (ViewGroup) findViewById(R.id.root);
		root.removeAllViews();

		Uri uri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
		List<TATomato> tomatoListReverse = TAClockwiseTomatoCSVParser.parseInReverse(uri);
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
						Toast.makeText(IntentFilterReceiver.this, "dont support modify project yet", Toast.LENGTH_SHORT).show();
						return;
					}

					onTomatoClicked(y);
				}
			});
			index--;
		}
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

	protected void onTomatoClicked(TATomato y)
	{
		Intent intent = new Intent(this, TASelect.class);
		intent.putExtra(DURATION, y.getDurationMs());
		intent.putExtra(TOMATO_ID, y.getId());
		startActivityForResult(intent, selectdode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == TAMain.selectdode && resultCode == Activity.RESULT_OK)
		{
			int projectId = data.getIntExtra(TASelect.ID, 0);
			int duration = data.getIntExtra(DURATION, 0);
			long tomatoId = data.getLongExtra(TOMATO_ID, 0);
			String projectName = TADataCenter.ProjectCenter.getProjectNameById(this, projectId);
			TADataCenter.addPastTimeToAccumulate(this, projectName, duration);
			TATomatoPersistence.save(this, tomatoId, projectName);

			Toast.makeText(this, "time accumulated!", Toast.LENGTH_SHORT).show();

			updateUI();
		}
		super.onActivityResult(requestCode, resultCode, data);
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

}
