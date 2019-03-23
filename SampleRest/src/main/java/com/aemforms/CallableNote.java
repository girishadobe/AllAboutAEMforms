package com.aemforms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class CallableNote {
	double proceedsToIssuer;
	double faceAmount;
	double discountRate;
	String initialPurchasePrice;
	String maturityDate;
	String settlementDate;
	String callPeriod;
	long differenceInDays;
	public String getCallPeriod() {
		return callPeriod;
	}

	public void setCallPeriod(String maturityDate) {
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		try {
			Date d2 = df.parse(maturityDate);
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(d2);
			cal.add(Calendar.DATE,-45);
			String month = Integer.toString(cal.get(Calendar.MONTH)+1);
			if(cal.get(Calendar.MONTH)<10)
			{
				month = "0"+Integer.toString(cal.get(Calendar.MONTH)+1);
				
			}
			String year = Integer.toString(cal.get(Calendar.YEAR));
			String dayOfMonth = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
			
			if(cal.get(Calendar.DAY_OF_MONTH)<10)
			{
				dayOfMonth = "0"+Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
				
			}
				this.callPeriod = month+"/"+dayOfMonth+"/"+year;
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}

	
	
	public String getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}

	public String getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(String settlementDate) {
		this.settlementDate = settlementDate;
	}

	
	
	public String getInitialPurchasePrice() {
		return initialPurchasePrice;
	}

	public void setInitialPurchasePrice(String initialPurchasePrice) {
		this.initialPurchasePrice = initialPurchasePrice;
	}

	public double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}

	
	

	public long getDifferenceInDays() {
		return differenceInDays;
	}

	public void setDifferenceInDays(String maturityDate,String settlementDate) throws java.text.ParseException {
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		this.setMaturityDate(maturityDate);
		this.setSettlementDate(settlementDate);
		System.out.println("The dates I got were "+maturityDate+"...."+settlementDate);
		Date d2 = df.parse(maturityDate);
		Date d1 = df.parse(settlementDate);
		long difference = Math.abs(d2.getTime() - d1.getTime());
		long differenceDates = difference / (24 * 60 * 60 * 1000);
		System.out.println("The difference in dates is "+differenceDates);
		this.differenceInDays = differenceDates;
	}

	
	public double getFaceAmount() {
		return faceAmount;
	}

	public void setFaceAmount(double faceAmount) {
		this.faceAmount = faceAmount;
	}
	
	
	public double getProceedsToIssuer() {
		return this.proceedsToIssuer;
	}

	public void setProceedsToIssuer(double faceAmount,double discountRate) {
		double a1 = (faceAmount * discountRate)/100;
		double a2 = a1/360*differenceInDays;
		
		this.proceedsToIssuer = faceAmount - a2;
		double ipp = (this.proceedsToIssuer/faceAmount)*100;
		String ippStrin = Double.toString(ipp)+"%";
		this.setInitialPurchasePrice(ippStrin);
		
	
	}

	
}
