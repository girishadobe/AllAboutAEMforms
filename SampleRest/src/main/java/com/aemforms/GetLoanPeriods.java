package com.aemforms;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/getLoanPeriods")
public class GetLoanPeriods {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLoanTerms() {
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
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("LoanTerm", LoanTerms);
		return Response.status(200).entity(map).build();

	}
}
