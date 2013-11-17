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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Build;
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

import com.example.android.swipedismiss.SwipeDismissListViewTouchListener;
import com.qidu.lin.time.accumulater.R;
import com.qidu.lin.time.accumulater.bg.TADataCenter;
import com.qidu.lin.time.accumulater.bg.TATime;
import com.qidu.lin.time.accumulater.bg.TATomato;
import com.qidu.lin.time.accumulater.bg.TATomato.StringFilter;
import com.qidu.lin.time.accumulater.bg.TATomatoPersistence;

public class TATomatoListActivity extends Activity
{
	private static final String TAG_PROJECT_NAME = "TAG_PROJECT_NAME";
	private static final String TAG_TITLE = "TAG_TITLE";
	private static final String TAG_WITHIN_HOURS = "TAG_WITHIN_HOURS";
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
			Context context = TATomatoListActivity.this;
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

			String tomatoNote = TATomatoPersistence.loadTomatoNote(context, item.getId());
			if (tomatoNote == null)
			{
				tomatoNote = "";
			}
			((TextView) v.findViewById(R.id.note)).setText(tomatoNote);

			if (isActivityForMultiProjects())
			{
				String name = TATomatoPersistence.getProjectName(context, item.getId());
				if (name == null)
				{
					name = "no project";
				}
				((TextView) v.findViewById(R.id.project)).setText(name);

			}

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

			final RatingBar ratingbar = (RatingBar) v.findViewById(R.id.rating);
			final float rating = TATomatoPersistence.loadTomatoRating(context, item.getId());
			if (rating > 0)
			{
				ratingbar.setVisibility(View.VISIBLE);
				ratingbar.setNumStars((int) rating);
			}
			else
			{
				ratingbar.setVisibility(View.INVISIBLE);
			}
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
		return getLauncherIntent(context, projectName, 0, null);
	}

	public static Intent getLauncherIntentForPast24Hours(Context context)
	{
		return getLauncherIntent(context, NAME_PRESENTING_ALL_PROJECTS, 24, context.getString(R.string.past_24_hours));
	}

	public static Intent getLauncherIntentForPast7Days(Context context)
	{
		return getLauncherIntent(context, NAME_PRESENTING_ALL_PROJECTS, 24 * 7, context.getString(R.string.past_7_days));
	}

	private static Intent getLauncherIntent(Context context, String projectName, int hoursWithin, String title)
	{
		Intent intent = new Intent(context, TATomatoListActivity.class);
		intent.putExtra(TAG_PROJECT_NAME, projectName);
		intent.putExtra(TAG_WITHIN_HOURS, hoursWithin);
		intent.putExtra(TAG_TITLE, title);
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
		if (isActivityForMultiProjects())
		{
			int withInHours = getIntent().getIntExtra(TAG_WITHIN_HOURS, 0);
			if (withInHours > 0)
			{
				list = TADataCenter.getAllReverseTomatosWithinHours(this, withInHours);
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

		final String title = getIntent().getStringExtra(TAG_TITLE);
		if (title != null)
		{
			this.setTitle(title);
		}
		else if (!isActivityForMultiProjects())
		{
			this.setTitle(projectName + "  " + getString(R.string.timeResultShort, x.hour, x.minute, x.second));
		}
		else
		{
		}

		lv.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3)
			{
				final long tomatoId = adapter.getTomato(position).getId();
				final Context context = TATomatoListActivity.this;
				final LinearLayout ll = new LinearLayout(context);
				ll.setOrientation(LinearLayout.VERTICAL);
				final EditText editText = new EditText(context);
				ll.addView(editText);
				final RatingBar ratingBar = new RatingBar(context);
				ll.addView(ratingBar);

				editText.setHint(R.string.input_tomato_note_hint);

				ratingBar.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				final float tomatoRating = TATomatoPersistence.loadTomatoRating(context, tomatoId);
				ratingBar.setNumStars(5);
				ratingBar.setRating(tomatoRating);
				ratingBar.setStepSize(1.0f);
				String note = TATomatoPersistence.loadTomatoNote(context, tomatoId);
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
								TATomatoPersistence.saveTomatoRating(context, tomatoId, ratingBar.getRating());
							}
						}).setNegativeButton(android.R.string.cancel, null).show();

			}
		});

		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(lv,
				new SwipeDismissListViewTouchListener.DismissCallbacks()
				{
					View swipedView = null;
					float swipedX = 0.0f;
					boolean done = false;

					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
					@Override
					public void onSwiping(View mDownView, int viewWidth, float deltaX)
					{
						mDownView.findViewById(R.id.edit).setVisibility(View.VISIBLE);
						mDownView.findViewById(R.id.info).setTranslationX(swipedX + deltaX);
						final float tmpAlpha = Math.max(0f, Math.min(1f, 1f - 2f * Math.abs(deltaX) / viewWidth));
						mDownView.findViewById(R.id.edit).setAlpha(swipedView == null ? 1 - tmpAlpha : tmpAlpha);
					}

					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
					@Override
					public void onSwipeDone(View mDownView, int mViewWidth, long mAnimationTime)
					{
						if (swipedView != null && swipedView != mDownView)
						{
							swipedView.findViewById(R.id.info).animate().translationX(0).alpha(1).setDuration(mAnimationTime)
									.setListener(null);
							swipedView.findViewById(R.id.edit).setVisibility(View.GONE);
							swipedView = null;
							swipedX = 0.0f;
						}

						if ((swipedView == mDownView))
						{
							mDownView.findViewById(R.id.info).animate().translationX(0).alpha(1).setDuration(mAnimationTime)
									.setListener(null);
							mDownView.findViewById(R.id.edit).setVisibility(View.GONE);
							swipedView = null;
							swipedX = 0.0f;
						}
						else
						{
							final int translationX = mViewWidth / 2;
							mDownView.findViewById(R.id.info).animate().translationX(translationX).alpha(1).setDuration(mAnimationTime)
									.setListener(null);
							mDownView.findViewById(R.id.edit).setVisibility(View.VISIBLE);
							swipedView = mDownView;
							swipedX = translationX;
						}
					}
				});
		lv.setOnTouchListener(touchListener);
		lv.setOnScrollListener(touchListener.makeScrollListener());
	}

	private boolean isActivityForMultiProjects()
	{
		return projectName.equals(NAME_PRESENTING_ALL_PROJECTS);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tatomato_history_list, menu);
		return true;
	}

}
