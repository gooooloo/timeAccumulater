/**
 * @copyright: qiduLin
 * 
 */

package com.qidu.lin.time.accumulater;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class TASelect extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select);

		TableLayout tableLayout = (TableLayout) this.findViewById(R.id.table);

		final EditText input = (EditText) this.findViewById(R.id.input);
		Button v = (Button) this.findViewById(R.id.add);
		v.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (input.getVisibility() == View.VISIBLE)
				{
					int id = TADataCenter.SPCenter.addSPName(TASelect.this, input.getText().toString());
					TASelect.this.onSelect(id);
				}
				else
				{
					input.setVisibility(View.VISIBLE);
				}
			}
		});

		String[] names = TADataCenter.SPCenter.getSPNames(this);
		if (names != null && names.length != 0)
		{
			TableRow tr = new TableRow(this);
			for (int i = 0; i < names.length; i++)
			{
				final int numARow = 4;
				Button btn = new Button(this);
				btn.setText(names[i]);
				final int id = i;
				btn.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						TASelect.this.onSelect(id);
					}
				});
				btn.setOnLongClickListener(new OnLongClickListener()
				{

					@Override
					public boolean onLongClick(View v)
					{
						Toast.makeText(TASelect.this, "Long click feature is to be done", 1000).show();
						return true;
					}
				});
				tr.addView(btn);

				if (tr.getChildCount() == numARow)
				{
					tableLayout.addView(tr);
					tr = new TableRow(this);
				}
			}
			if (tr.getChildCount() != 0)
			{
				tableLayout.addView(tr);
			}
		}
	}

	private void onSelect(int id)
	{

		TADataCenter.SPCenter.setLastNameId(this, id);
		TASelect.this.finish();
	}
}