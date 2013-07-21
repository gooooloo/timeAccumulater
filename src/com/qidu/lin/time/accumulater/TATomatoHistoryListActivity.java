package com.qidu.lin.time.accumulater;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TATomatoHistoryListActivity extends Activity
{
	private static final String TAG_PROJECT_NAME = "TAG_PROJECT_NAME";

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
			TextView tv = null;
			TATomato item = getTomato(position);
			if (convertView == null)
			{
				tv = new TextView(TATomatoHistoryListActivity.this);
			}
			else
			{
				tv = (TextView) convertView;
			}
			tv.setText(item.getStartTimeString());
			return tv;
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
			return false;
		}

	}

	public static Intent getLauncherIntent(Context context, String projectName)
	{
		Intent intent = new Intent(context, TATomatoHistoryListActivity.class);
		intent.putExtra(TAG_PROJECT_NAME, projectName);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tatomato_history_list);

		String projectName = getIntent().getStringExtra(TAG_PROJECT_NAME);
		if (projectName == null)
		{
			finish();
			return;
		}

		List<TATomato> list = TADataCenter.getTomatoListForProject(this, projectName);
		if (list != null)
		{
			ListView lv = (ListView) findViewById(R.id.listView);
			lv.setAdapter(new TomatoListAdapter(list));
			TATime x = TADataCenter.getAccumulateTime(this, projectName);
			this.setTitle(projectName + "  " + getString(R.string.timeResult, x .hours, x.minute, x.second));
		}
		else
		{
			finish();
			return;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tatomato_history_list, menu);
		return true;
	}

}