package com.aemforms;

import java.util.Comparator;

import org.json.JSONException;

public class SortJSONArray implements Comparator<ClosingPrice> {

	@Override
	public int compare(ClosingPrice a, ClosingPrice b) {
		// TODO Auto-generated method stub
		try {
			String valA = a.getDate();
			String valB = b.getDate();
			System.out.println("Got values " + valA + valB);
			return valA.compareTo(valB);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;

	}

}
