/*
 * Copyright 2013 Qidu Lin
 * 
 * This file is part of TimeAccumulater.
 * 
 * TimeAccumulater is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * TimeAccumulater is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * TimeAccumulater. If not, see <http://www.gnu.org/licenses/>.
 */
package com.qidu.lin.time.accumulater.bg;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import android.net.Uri;
import au.com.bytecode.opencsv.CSVReader;

public class TACSVParser
{
	private List<String[]> myEntries = null;

	public TACSVParser(Uri uri) throws IOException
	{
		if (uri == null)
		{
			throw new IOException("null uri");
		}

		FileReader fr = new FileReader(uri.getPath());
		CSVReader reader = new CSVReader(fr);
		myEntries = reader.readAll();
	}

	public List<String[]> getMyEntries()
	{
		return myEntries;
	}
}
