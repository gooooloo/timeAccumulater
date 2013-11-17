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

import java.util.Arrays;
import java.util.Comparator;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qidu.lin.time.accumulater.R;
import com.qidu.lin.time.accumulater.bg.TADataCenter;

final class TAProjectListAdapter extends BaseAdapter
{
	private String[] names = {};
	private final Activity activity;
	private int colorForOn;
	private int colorForOff;

	public enum SortType
	{
		TimeLessToMore, TimeMoreToLess, LastUsedRecentToPast, LastUsedPastToRecent
	};

	TAProjectListAdapter(Activity context)
	{
		this.activity = context;
		colorForOn = activity.getResources().getColor(R.color.colorForOn);
		colorForOff = activity.getResources().getColor(R.color.colorForOff);
	}

	public void sort(SortType sortType)
	{
		Arrays.sort(this.names, getProjectComparator(sortType));
		this.notifyDataSetChanged();
	}

	private Comparator<String> getProjectComparator(final SortType sortType)
	{
		switch (sortType)
		{
		case TimeLessToMore:
		case TimeMoreToLess:
			return new Comparator<String>()
			{
				@Override
				public int compare(String lhs, String rhs)
				{
					long l = TADataCenter.getAccumulateMs(activity, lhs);
					long r = TADataCenter.getAccumulateMs(activity, rhs);
					if (sortType == SortType.TimeLessToMore)
						return (l > r) ? 1 : (l < r) ? -1 : 0;
					else
						return (l > r) ? -1 : (l < r) ? 1 : 0;

				}
			};

		case LastUsedPastToRecent:
		case LastUsedRecentToPast:
			return new Comparator<String>()
			{
				@Override
				public int compare(String lhs, String rhs)
				{
					long l = TADataCenter.getProjectLastTime(activity, lhs);
					long r = TADataCenter.getProjectLastTime(activity, rhs);

					if (l == 0) // means not exists
						l = (sortType == SortType.LastUsedPastToRecent) ? Long.MAX_VALUE : 1;
					if (r == 0) // means not exists
						r = (sortType == SortType.LastUsedPastToRecent) ? Long.MAX_VALUE : 1;

					if (sortType == SortType.LastUsedPastToRecent)
						return (l > r) ? 1 : (l < r) ? -1 : 0;
					else
						return (l > r) ? -1 : (l < r) ? 1 : 0;

				}
			};

		default:
			return null;
		}
	}

	public void setItems(String[] names)
	{
		this.names = names;
		this.notifyDataSetChanged();
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
		View v = convertView;
		if (v == null)
		{
			v = activity.getLayoutInflater().inflate(R.layout.project, null);
		}

		final String projectName = getProjectName(position);

		((TextView) v.findViewById(R.id.name)).setText(projectName);
		((TextView) v.findViewById(R.id.duration)).setText(TADataCenter.getAccumulateTime(activity, projectName).toString());

		int textcolor = TADataCenter.getOnFlag(v.getContext(), projectName) ? colorForOn : colorForOff;

		((TextView) v.findViewById(R.id.name)).setTextColor(textcolor);
		((TextView) v.findViewById(R.id.duration)).setTextColor(textcolor);
		return v;
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