package com.qidu.lin.time.accumulater;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class IntentFilterReceiver extends Activity
{
	protected static final int selectdode = 0;
	private static final String DURATION = "DURATION";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent_filter_receiver);

		ViewGroup root = (ViewGroup) findViewById(R.id.root);

		Uri uri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
		List<TATomato> tomatoListReverse = TAClockwiseTomatoCSVParser.parseInReverse(uri);
		int index = tomatoListReverse.size();
		for (final TATomato y : tomatoListReverse)
		{
			Button btn = new Button(this);
			btn.setText("#" + index + " " + y.getStartTimeString() + "  " + y.getDurationString());
			root.addView(btn);

			btn.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					onTomatoClicked(y);
				}
			});
			index--;
		}
	}

	protected void onTomatoClicked(TATomato y)
	{
		Intent intent = new Intent(this, TASelect.class);
		intent.putExtra(DURATION, y.getDurationMs());
		startActivityForResult(intent, selectdode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == TAMain.selectdode && resultCode == Activity.RESULT_OK)
		{
			int id = data.getIntExtra(TASelect.ID, 0);
			int duration = data.getIntExtra(DURATION, 0);
			String projectName = TADataCenter.ProjectCenter.getProjectNameById(this, id);
			TADataCenter.addPastTimeToAccumulate(this, projectName, duration);
			
			Toast.makeText(this, "time accumulated!", Toast.LENGTH_SHORT).show();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_intent_filter_receiver, menu);
		return true;
	}

}
