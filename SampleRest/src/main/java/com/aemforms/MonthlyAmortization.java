package com.aemforms;

public class MonthlyAmortization {
int paymentNumber;
String interestPaid;
String principalPaid;
String loanBalance;
public int getPaymentNumber() {
	return paymentNumber;
}
public void setPaymentNumber(int paymentNumber) {
	this.paymentNumber = paymentNumber;
}
public String getInterestPaid() {
	return interestPaid;
}
public void setInterestPaid(String interestPaid) {
	this.interestPaid = interestPaid;
}
public String getPrincipalPaid() {
	return principalPaid;
}
public void setPrincipalPaid(String principalPaid) {
	this.principalPaid = principalPaid;
}
public String getLoanBalance() {
	return loanBalance;
}
public void setLoanBalance(String loanBalance) {
	this.loanBalance = loanBalance;
}

}
