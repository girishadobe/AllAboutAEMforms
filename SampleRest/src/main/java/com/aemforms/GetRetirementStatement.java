package com.aemforms;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/getStatement")
public class GetRetirementStatement {
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
public Response getStatement(@PathParam("id")String id){
	UserClass uc = new UserClass();
	uc.setName("Gloria Rios");
	uc.setAddress("345 Park Ave");
	uc.setId(id);
	uc.setCity("San Jose");
	uc.setZip("95110");
	uc.setState("CA");
	uc.setDirectDeposit(1);
	uc.setOnlineBanking(1);
	uc.setCurrentContributions(1589);
	uc.setPossibleWithdrawals(4190);
	System.out.println("Created user");
	CurrentAssetMix currentAssetMix = new CurrentAssetMix();
	currentAssetMix.setAssetType("Stocks");
	currentAssetMix.setPercentage(80.2);
	
	CurrentAssetMix currentAssetMix1 = new CurrentAssetMix();
	currentAssetMix1.setAssetType("Bonds");
	currentAssetMix1.setPercentage(19.8);
	
	ModelAssetMix modelAssetMix = new ModelAssetMix();
	modelAssetMix.setAssetType("Stocks");
	modelAssetMix.setPercentage(70);
	
	ModelAssetMix modelAssetMix1 = new ModelAssetMix();
	modelAssetMix1.setAssetType("Bonds");
	modelAssetMix1.setPercentage(20);
	
	ModelAssetMix modelAssetMix2 = new ModelAssetMix();
	modelAssetMix2.setAssetType("Cash");
	modelAssetMix2.setPercentage(10);
	
	java.util.List<CurrentAssetMix> currentAssetMixList = new ArrayList<>();
	currentAssetMixList.add(currentAssetMix);
	currentAssetMixList.add(currentAssetMix1);
	
	java.util.List<ModelAssetMix> modelAssetMixList = new ArrayList<>();
	 modelAssetMixList.add(modelAssetMix);
	 modelAssetMixList.add(modelAssetMix1);
	 modelAssetMixList.add(modelAssetMix2);
	
	EstimatedIncome ei1 = new EstimatedIncome();
	ei1.setIncome(4190);
	ei1.setContribution(1589);
	
	EstimatedIncome ei2 = new EstimatedIncome();
	ei2.setIncome(4396);
	ei2.setContribution(1794);
	
	EstimatedIncome ei3 = new EstimatedIncome();
	ei3.setIncome(4601);
	ei3.setContribution(2000);
	
	AccountSummary as1 = new AccountSummary();
	as1.setDescription("Begining Balance");
	as1.setCurrent("$331,854.71");
	as1.setYtd("$316,790.00");
	
	AccountSummary as2 = new AccountSummary();
	as2.setDescription("Your contributions");
	as2.setCurrent("$5,276.28");
	as2.setYtd("$11,552.73");
	
	
	AccountSummary as3 = new AccountSummary();
	as3.setDescription("Employer contributions");
	as3.setCurrent("$1,055.28");
	as3.setYtd("$2,813.09");
	
	AccountSummary as4 = new AccountSummary();
	as4.setDescription("Market gain/loss");
	as4.setCurrent("-$2,322.59");
	as4.setYtd("$3,492.80");
	
	AccountSummary as5 = new AccountSummary();
	as5.setDescription("Other transactions");
	as5.setCurrent("$1,264.32");
	as5.setYtd("$2,479.38");
	
	AccountSummary as6 = new AccountSummary();
	as6.setDescription("Ending balance");
	as6.setCurrent("$337,128.00");
	as6.setYtd("$337,128.00");
	
	AccountSummary as7 = new AccountSummary();
	as7.setDescription("Vested balance");
	as7.setCurrent("$337,128.00");
	as7.setYtd("$337,128.00");
	
	
	
		
	Balance b1 = new Balance();
	b1.setYear("2015");
	b1.setBalance(190000);
	
	Balance b2 = new Balance();
	b2.setYear("2016");
	b2.setBalance(220000);
	
	Balance b3 = new Balance();
	b3.setYear("2017");
	b3.setBalance(340000);
	
	java.util.List<EstimatedIncome> esimatedIncome = new ArrayList<>();
	esimatedIncome.add(ei1);
	esimatedIncome.add(ei2);
	esimatedIncome.add(ei3);
	
	java.util.List<Balance> balances = new ArrayList<>();
	balances.add(b1);
	balances.add(b2);
	balances.add(b3);
	
	java.util.List<AccountSummary> accountSummary = new ArrayList<>();
	accountSummary.add(as1);
	accountSummary.add(as2);
	accountSummary.add(as3);
	accountSummary.add(as4);
	accountSummary.add(as5);
	accountSummary.add(as6);
	accountSummary.add(as7);
	
	
	HashMap<String,Object> map = new HashMap<String, Object>();
	map.put("accountSummary",accountSummary);
	map.put("estimatedIncome", esimatedIncome);
	map.put("balances",balances);
	map.put("user",uc);
	map.put("currentAssetMix",currentAssetMixList);
	map.put("modelAssetMix",modelAssetMixList);
	
	return Response.status(201).entity(map).build();
	
}
}
