package com.aemforms;

public class UserClass {
	String name;
	int age;
	String id;
	String address;
	String city;
	String state;
	String zip;
	String groupName;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(String groupNumber) {
		this.groupNumber = groupNumber;
	}

	String groupNumber;
	int onlineBanking;
	int directDeposit;
	double possibleWithdrawals;
	double currentContributions;
	double credit;

	public void setCredit(double credit) {
		this.credit = 50000;
	}

	public double getCredit() {
		return this.credit;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return this.state;
	}

	public void setPossibleWithdrawals(double possibleWithdrawals) {
		this.possibleWithdrawals = possibleWithdrawals;
	}

	public double getPossibleWithdrawals() {
		return this.possibleWithdrawals;
	}

	public void setCurrentContributions(double currentContributions) {
		this.currentContributions = currentContributions;
	}

	public double getCurrentContributions() {
		return this.currentContributions;
	}

	public void setOnlineBanking(int onlineBanking) {
		this.onlineBanking = onlineBanking;
	}

	public int getOnlineBanking() {
		return this.onlineBanking;
	}

	public void setDirectDeposit(int directDeposit) {
		this.directDeposit = directDeposit;
	}

	public int getDirectDeposit() {
		return this.directDeposit;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getZip() {
		return this.zip;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return this.city;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return this.name;
	}

	public int getAge() {
		return this.age;
	}

	public String getId() {
		return this.id;
	}

}
