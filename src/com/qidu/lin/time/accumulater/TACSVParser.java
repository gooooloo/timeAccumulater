package com.qidu.lin.time.accumulater;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import android.net.Uri;
import au.com.bytecode.opencsv.CSVReader;

public class TACSVParser
{
	private List<String[]> myEntries = null;

	public TACSVParser(Uri uri)
	{
		try
		{
			FileReader fr = new FileReader(uri.getPath());
			CSVReader reader = new CSVReader(fr);
			myEntries = reader.readAll();

		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public List<String[]> getMyEntries()
	{
		return myEntries;
	}
}
