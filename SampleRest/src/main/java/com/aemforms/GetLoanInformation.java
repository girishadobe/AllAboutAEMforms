package com.aemforms;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/getLoanInformation")
public class GetLoanInformation {
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
public Response getLoanDetails(@PathParam("id")String id){
		LoanSummary ls1 = new LoanSummary();
		ls1.setDescription("Pricipal Balance");
		ls1.setValue("$397,596");
		
		LoanSummary ls2 = new LoanSummary();
		ls2.setDescription("Next Due Date:");
		ls2.setValue("04/01/2018");
		
		LoanSummary ls3 = new LoanSummary();
		ls3.setDescription("Interest Rate:");
		ls3.setValue("3.44");
		
		LoanSummary ls4 = new LoanSummary();
		ls4.setDescription("Monthly Payment:");
		ls4.setValue("$2090.00");
		
		LoanSummary ls5 = new LoanSummary();
		ls4.setDescription("Total Amount Due:");
		ls4.setValue("$00.00");
		
		PaidYearToDate escrow = new PaidYearToDate();
		escrow.setDescription("Escrow");
		escrow.setAmount("$0.00");
		PaidYearToDate principal = new PaidYearToDate();
		principal.setDescription("Principal");
		principal.setAmount("$118,37.00");
		PaidYearToDate interest = new PaidYearToDate();
		interest.setDescription("Inerest");
		interest.setAmount("$8,409");
		
		LoanInformation li = new LoanInformation();
		li.setDescription("Principal Balance");
		li.setValue("$397,909");
		
		LoanInformation li1 = new LoanInformation();
		li1.setDescription("Loan Origination Date");
		li1.setValue("01/17/2013");
		
		LoanInformation li2 = new LoanInformation();
		li2.setDescription("Interest Rate");
		li2.setValue("3.44%");
		

		java.util.List<LoanSummary> loanSummaryList = new ArrayList<LoanSummary>();
		loanSummaryList.add(ls1);
		loanSummaryList.add(ls2);
		loanSummaryList.add(ls3);
		loanSummaryList.add(ls4);
		loanSummaryList.add(ls5);
		
		
		java.util.List<LoanInformation> loanInfo = new ArrayList<LoanInformation>();
		loanInfo.add(li);
		loanInfo.add(li2);
		loanInfo.add(li1);
		
		java.util.List<PaidYearToDate> paidYearToDateList = new ArrayList<PaidYearToDate>();
		paidYearToDateList.add(principal);
		paidYearToDateList.add(interest);
		paidYearToDateList.add(escrow);
		
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("LoanInformation",loanInfo);
		map.put("PaidYearToDate",paidYearToDateList);
		map.put("LoanSummary",loanSummaryList);
		
		return Response.status(201).entity(map).build();
	}
	

}
