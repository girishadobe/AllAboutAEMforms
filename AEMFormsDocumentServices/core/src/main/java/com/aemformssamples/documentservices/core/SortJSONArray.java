package com.aemformssamples.documentservices.core;

import java.util.Comparator;

import org.json.JSONException;
import org.json.JSONObject;

public class SortJSONArray implements Comparator<JSONObject> {

	@Override
	public int compare(JSONObject a, JSONObject b) {
		// TODO Auto-generated method stub
		try {
			int valA = (int) a.getInt("position");
			int valB = (int) b.getInt("position");
			System.out.println("Got values " + valA + valB);
			return valA - valB;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;

	}

}
