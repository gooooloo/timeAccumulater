package com.qidu.lin.time.accumulater;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TAManualRecord extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tamanual_record);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tamanual_record, menu);
		return true;
	}

}
