package com.qidu.lin.time.accumulater;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class TATomatoHistoryListActivity extends Activity
{
	private static final String TAG_PROJECT_NAME = "TAG_PROJECT_NAME";

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
			this.setTitle("" + list.size());
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
