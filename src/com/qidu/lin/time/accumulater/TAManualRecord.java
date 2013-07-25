package com.qidu.lin.time.accumulater;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;

public class TAManualRecord extends Activity
{
	protected static final int selectdode = 0;
	Button projectButton = null;
	private String projectname = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tamanual_record);

		projectButton = (Button) this.findViewById(R.id.project);

		projectButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				TAManualRecord.this.startActivityForResult(new Intent(TAManualRecord.this, TASelect.class), TAMain.selectdode);
			}
		});

		projectButton.setOnLongClickListener(new OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				startActivity(TATomatoHistoryListActivity.getLauncherIntent(TAManualRecord.this, projectname));
				return true;
			}
		});
		findViewById(R.id.begin).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				selectBegin();
			}
		});
		findViewById(R.id.end).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				selectEnd();
			}
		});

		findViewById(R.id.ok).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				doRecord();
			}
		});
		findViewById(R.id.cancel).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				finish();
			}
		});

		updateProjectButtonUI();

	}

	protected void selectBegin()
	{
		selectTime();
	}

	private void selectTime()
	{
		TimePickerDialog d = new TimePickerDialog(this, null, 0, 0, true);
		d.show();
	}

	protected void selectEnd()
	{
		selectTime();
	}

	private void doRecord()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tamanual_record, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == selectdode && resultCode == Activity.RESULT_OK)
		{
			int id = data.getIntExtra(TASelect.ID, 0);
			projectname = TADataCenter.ProjectCenter.getProjectNameById(this, id);
			updateProjectButtonUI();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void updateProjectButtonUI()
	{
		projectButton.setText(projectname + getString(R.string.chooseProject));
	}

}
