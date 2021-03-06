package com.qidu.lin.time.accumulater.fg;

import java.util.Calendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;

import com.qidu.lin.time.accumulater.R;
import com.qidu.lin.time.accumulater.bg.TADataCenter;
import com.qidu.lin.time.accumulater.bg.TATomato;
import com.qidu.lin.time.accumulater.bg.TATomatoPersistence;

public class TAManualRecord extends Activity
{
	private static final long defaultMsDuration = 1000 * 60 * 25;
	private static final int selectdode = 0;
	Calendar begin;
	private Button beginButton;
	Calendar end;
	private Button endButton;
	Button projectButton = null;
	private String projectname = "";

	private void doRecord()
	{
		long beginMs = begin.getTimeInMillis();
		long endMs = end.getTimeInMillis();
		long durationMs = endMs - beginMs;
		TADataCenter.addPastTimeToAccumulate(this, projectname, durationMs);
		TADataCenter.setATomato(this, projectname, beginMs, endMs);
		TATomatoPersistence.saveProjectName(this, new TATomato(beginMs, endMs).getId(), projectname);
		
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == selectdode && resultCode == Activity.RESULT_OK)
		{
			int id = data.getIntExtra(TAProjectListActivity.ID, 0);
			projectname = TADataCenter.ProjectCenter.getProjectNameById(this, id);
			updateUI();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void onBeginOrEndTimeSet(boolean isBegin, Calendar calendar)
	{
		if (isBegin)
		{
			begin = calendar;
		}
		else
		{
			end = calendar;
		}

		updateUI();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tamanual_record);

		end = Calendar.getInstance();
		end.setTimeInMillis(System.currentTimeMillis());

		begin = Calendar.getInstance();
		begin.setTimeInMillis(end.getTimeInMillis() - defaultMsDuration);

		projectButton = (Button) this.findViewById(R.id.project);
		((TAProjectButton) projectButton).setup(this, selectdode);

		beginButton = (Button) findViewById(R.id.begin);
		beginButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				selectBegin();
			}
		});
		endButton = (Button) findViewById(R.id.end);
		endButton.setOnClickListener(new OnClickListener()
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

		updateUI();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tamanual_record, menu);
		return true;
	}

	protected void selectBegin()
	{
		selectTime(true);
	}

	protected void selectEnd()
	{
		selectTime(false);
	}

	private void selectTime(final boolean isBegin)
	{
		Calendar c = isBegin ? begin : end;
		TimePickerDialog d = new TimePickerDialog(this, new OnTimeSetListener()
		{

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute)
			{
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				calendar.set(Calendar.MINUTE, minute);
				onBeginOrEndTimeSet(isBegin, calendar);
			}

		}, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
		d.show();
	}

	public void updateUI()
	{
		projectButton.setText(projectname + getString(R.string.chooseProject));
		beginButton.setText(getString(R.string.begin_time) + begin.getTime().toLocaleString());
		endButton.setText(getString(R.string.end_time) + end.getTime().toLocaleString());
	}

}
