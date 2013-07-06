package com.qidu.lin.time.accumulater;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;

public class IntentFilterReceiver extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent_filter_receiver);

		ViewGroup root = (ViewGroup) findViewById(R.id.root);

		Uri uri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
		List<TATomato> tomatoListReverse = TAClockwiseTomatoCSVParser.parseInReverse(uri);
		int index = tomatoListReverse.size();
		for (TATomato y : tomatoListReverse)
		{
			Button btn = new Button(this);
			btn.setText("#" + index + " " + y.getStartTimeString() + "  " + y.getDurationString());
			root.addView(btn);
			index--;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_intent_filter_receiver, menu);
		return true;
	}

}
