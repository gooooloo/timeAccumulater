package com.qidu.lin.time.accumulater;

import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class IntentFilterReceiver extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent_filter_receiver);

		Uri uri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
		List<TATomato> x = TAClockwiseTomatoCSVParser.parse(uri);
		
		String s = "";
		for (TATomato y : x)
		{
			s += y.duration + "; ";
		}
		TextView tv = (TextView) findViewById(R.id.tv);
		tv.setText(s);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_intent_filter_receiver, menu);
		return true;
	}

}
