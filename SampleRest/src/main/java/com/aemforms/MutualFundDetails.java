package com.aemforms;

import java.util.List;

public class MutualFundDetails {

	public String name;
	public String symbol;
	public Double yearlyReturn;
	public Double threeYearReturn;
	public Double fiveYearReturn;
	public List<MutualFundReturns> fundPerformance;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Double getYearlyReturn() {
		return yearlyReturn;
	}

	public void setYearlyReturn(Double yearlyReturn) {
		this.yearlyReturn = yearlyReturn;
	}

	public Double getThreeYearReturn() {
		return threeYearReturn;
	}

	public void setThreeYearReturn(Double threeYearReturn) {
		this.threeYearReturn = threeYearReturn;
	}

	public Double getFiveYearReturn() {
		return fiveYearReturn;
	}

	public void setFiveYearReturn(Double fiveYearReturn) {
		this.fiveYearReturn = fiveYearReturn;
	}

	public List<MutualFundReturns> getFundPerformance() {
		return fundPerformance;
	}

	public void setFundPerformance(List<MutualFundReturns> fundPerformance) {
		this.fundPerformance = fundPerformance;
	}

}
