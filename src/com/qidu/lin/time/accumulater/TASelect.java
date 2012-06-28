/**
 * @copyright: qiduLin
 * 
 */

package com.qidu.lin.time.accumulater;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TASelect extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select);

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
					TADataCenter.SPCenter.setLastNameId(TASelect.this, id);
					TASelect.this.finish();
				}
				else
				{
					input.setVisibility(View.VISIBLE);
				}
			}
		});
	}
}