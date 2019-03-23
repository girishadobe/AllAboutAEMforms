package com.aemforms;

public class Transaction {
String transactionDate;
String amount;
String description;
String category;
double withdrawals;
double balance;
public void setTransactionDate(String transactionDate)
{
	this.transactionDate = transactionDate;
}
public String getTransactionDate()
{
	return this.transactionDate;
}
public void setDescription(String description)
{
	this.description = description;
}
public String getDescriptiion()
{
	return this.description;
}
public void setCategory(String category)
{
	this.category = category;
}
public String getCategory()
{
	return this.category;
}
public void setAmount(String amount)
{
	this.amount = amount;
}
public String getAmount()
{
	return this.amount;
}
public void setWithdrawals(double amount)
{
	this.withdrawals = amount;
}
public double getWithdrawals()
{
	return this.withdrawals;
}
public void setBalance(double amount)
{
	this.balance = amount;
}
public double getBalance()
{
	return this.balance;
}
}
