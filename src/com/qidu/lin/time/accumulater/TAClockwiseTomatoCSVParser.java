package com.qidu.lin.time.accumulater;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;

public class TAClockwiseTomatoCSVParser
{
	public static List<TATomato> parse(Uri uri)
	{
		ArrayList<TATomato> list = new ArrayList<TATomato>();
		
		TACSVParser csvParser = new TACSVParser(uri);

		long lineCnt = 0;
		for (String [] y : csvParser.getMyEntries())
		{
			lineCnt++;
			if (lineCnt == 1)
			{
				// pass for header line.
				continue;
			}
			
			TATomato tomato = new TATomato(y[5], y[6]);
			list.add(tomato);
		}
		
		return list;
	}
}
