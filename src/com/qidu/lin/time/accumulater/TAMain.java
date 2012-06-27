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

public class TAMain extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button x = (Button) this.findViewById(R.id.btn);
		x.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				TADataCenter.Toggle(TAMain.this);
				TAMain.this.updateBtn();
			}
		});
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		this.updateBtn();
	}

	private void updateBtn()
	{

		Button x = (Button) this.findViewById(R.id.btn);
		x.setText(TADataCenter.getOnFlag(this) ? "set On" : "set Off");

	}

}