package com.aemforms;

public class Expenses {
String category;
double amount;

public Expenses(String category,double amount)
{
	this.category = category;
	this.amount = amount;
}
public void setCategory(String category)
{
	this.category = category;
}
public String getCategory()
{
	return this.category;
}
public void setAmount(double amount)
{
	this.amount = amount;
}
public double getAmount()
{
	return this.amount;
}
}
