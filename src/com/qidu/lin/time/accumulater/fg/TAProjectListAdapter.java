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

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.qidu.lin.time.accumulater.R;
import com.qidu.lin.time.accumulater.bg.TADataCenter;
import com.qidu.lin.time.accumulater.bg.TATime;

final class TAProjectListAdapter implements ListAdapter
{
	private final String[] names;
	private final Context context;

	TAProjectListAdapter(Context context, String[] names)
	{
		this.context = context;
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
		TextView tv = (convertView != null) ? ((TextView) convertView) : new TextView(context);
		String projectName = getProjectName(position);
		TASharedListItemSetting.setupListItemTextView(tv, projectName);

		// TODO : make UI better
		TATime x = TADataCenter.getAccumulateTime(context, projectName);
		tv.setText(projectName + "         " + context.getString(R.string.timeResultShort, x.hours, x.minute, x.second));

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