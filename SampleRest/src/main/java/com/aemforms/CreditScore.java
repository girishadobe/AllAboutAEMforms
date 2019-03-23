package com.aemforms;

import java.util.Random;

public class CreditScore {
	int creditScore;
	String jsonData;

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public void setCreditScore(String ssn) {
		Random random = new Random();

		// generate a random integer from 0 to 899, then add 100
		int x = random.nextInt(700) + 100;

		this.creditScore = x;

	}

	public int getCreditScore() {
		return this.creditScore;
	}

}
