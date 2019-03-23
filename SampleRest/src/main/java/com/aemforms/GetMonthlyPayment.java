package com.aemforms;

import java.text.NumberFormat;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/getmonthlypayment")
public class GetMonthlyPayment {
	@GET
	@Path("{loanAmount}/{loanTerm}/{interestRate}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMonthlyPayment(@PathParam("loanAmount")int loanAmount,@PathParam("loanTerm")int loanTerm,
			@PathParam("interestRate")double interestRate){
		interestRate /= 100.0;
		double monthlyRate = interestRate / 12.0;
		int termInMonths = loanTerm * 12;
		 double monthlyPayment = 
		         (loanAmount*monthlyRate) / 
		            (1-Math.pow(1+monthlyRate, -termInMonths));
		MonthlyPayment mp = new MonthlyPayment();
		 NumberFormat currencyFormat = 
		         NumberFormat.getCurrencyInstance();
		 
		String payment = currencyFormat.format(monthlyPayment);
		mp.setYourPayment(payment);
		
		return Response.status(200).entity(mp).build();

}
}
