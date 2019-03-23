package com.aemforms;

import java.util.List;

public class StockPrices {
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ClosingPrice> getClosingPrices() {
		return ClosingPrices;
	}

	public void setClosingPrices(List<ClosingPrice> closingPrices) {
		ClosingPrices = closingPrices;
	}

	List<ClosingPrice> ClosingPrices;

}
