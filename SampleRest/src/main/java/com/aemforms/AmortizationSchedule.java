package com.aemforms;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/getAmortizationSchedule")
public class AmortizationSchedule {
	@GET
	@Path("{loanAmount}/{loanTerm}/{interestRate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAmortizationSchedule(@PathParam("loanAmount") double loanAmount,
			@PathParam("loanTerm") int loanTerm, @PathParam("interestRate") double interestRate) {
		interestRate /= 100.0;
		double monthlyRate = interestRate / 12.0;
		double principal;
		int termInMonths = loanTerm * 12;
		double monthlyPayment = (loanAmount * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -termInMonths));

		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

		java.util.List<MonthlyAmortization> amortizationSchedule = new ArrayList<>();
		java.util.List<LoanTerm> LoanTerms = new ArrayList<>();
		LoanTerm FiveYears = new LoanTerm();
		FiveYears.setYears(5);
		LoanTerm TenYears = new LoanTerm();
		TenYears.setYears(10);
		LoanTerm FifteenYears = new LoanTerm();
		FifteenYears.setYears(15);
		LoanTerms.add(FiveYears);
		LoanTerms.add(TenYears);
		LoanTerms.add(FifteenYears);

		for (int month = 1; month <= termInMonths; month++) {
			MonthlyAmortization ma = new MonthlyAmortization();
			// Calculate monthly interest.
			double monthlyInterest = interestRate / 12.0 * loanAmount;
			if (month != 360) {
				// Calculate payment applied to principal
				principal = monthlyPayment - monthlyInterest;
			} else // This is the last month.
			{
				principal = loanAmount;
				monthlyPayment = loanAmount + monthlyInterest;
			}
			loanAmount -= principal;
			System.out.println("The Loan balance  is ......" + currencyFormat.format(loanAmount));
			ma.setPaymentNumber(month);
			ma.setInterestPaid(currencyFormat.format(monthlyInterest));
			ma.setPrincipalPaid(currencyFormat.format(principal));
			ma.setLoanBalance(currencyFormat.format(loanAmount));
			amortizationSchedule.add(ma);
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("amortization", amortizationSchedule);
		map.put("LoanTerm", LoanTerms);
		return Response.status(200).entity(map).build();

	}
}
