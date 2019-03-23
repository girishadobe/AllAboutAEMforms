package com.aemforms;

public class StockTransaction {
	String stockSymbol;
	double pricePaid;
	double quantity;

	public String getStockSymbol() {
		return stockSymbol;
	}

	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}

	public double getPricePaid() {
		return pricePaid;
	}

	public void setPricePaid(double d) {
		this.pricePaid = d;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

}
