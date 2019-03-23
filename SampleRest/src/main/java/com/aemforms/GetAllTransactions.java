package com.aemforms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

@Path("/getTransactions")
public class GetAllTransactions {
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTransactions(@PathParam("id")String id)
	{
		UserClass uc = new UserClass();
		uc.setName("Peter Jennings");
		uc.setAddress("395 Park Ave");
		uc.setId(id);
		uc.setCity("San Jose");
		uc.setDirectDeposit(1);
		uc.setOnlineBanking(0);
		ActivitySummary as = new ActivitySummary();
		as.setBeginBalanceDate("Begining Balance on 10/24");
		as.setBeginBalance("$0.00");
		as.setDeposits(200.00);
		as.setWithdrawals(-76.18);
		as.setEndBalanceDate("Ending Balance 0n 11/21");
		as.setEndBalance("$123.82");
		
		java.util.List<Expenses> listOfExpenses = new ArrayList<>();
		
		listOfExpenses.add(new Expenses("Gas",100.90));
		listOfExpenses.add(new Expenses("Gas",20.90));
		listOfExpenses.add(new Expenses("Food",30.90));
		listOfExpenses.add(new Expenses("Food",12.90));
		listOfExpenses.add(new Expenses("Movies",10.90));
		
		java.util.List<Transaction> listOfTransactions = new ArrayList<>();
		Transaction opening = new Transaction();
		opening.setAmount("100");
		opening.setDescription("Opening Deposit");
		opening.setTransactionDate("10/31");
		opening.setBalance(100);
	
		Transaction one = new Transaction();
		one.setWithdrawals(8.32);
		one.setCategory("Grocery");
		one.setDescription("Purchase authorized on 11/12 Coffee & More Sunnyvale CA");
		one.setTransactionDate("11/13");
		one.setBalance(91.68);
	
		
		Transaction two = new Transaction();
		two.setWithdrawals(5.50);
		two.setCategory("Grocery");
		two.setDescription("Purchase authorized on 11/14 Adobe San Jose5125 San Jose CA");
		two.setTransactionDate("11/16");
		
		
		Transaction three = new Transaction();
		three.setWithdrawals(3.29);
		three.setCategory("Gas");
		three.setDescription("Purchase authorized on 11/15 Sprouts Farmers Mkt#216");
		three.setTransactionDate("11/16");
		three.setBalance(82.89);
		
		Transaction four = new Transaction();
		four.setWithdrawals(4.00);
		four.setCategory("Gas");
		four.setDescription("Purchase authorized on 11/15 Adobe San Jose5125 San Jose CA");
		four.setTransactionDate("11/17");
		
		Transaction five = new Transaction();
		five.setWithdrawals(6.84);
		five.setCategory("Gas");
		five.setDescription("Purchase authorized on 11/17 Safeway Store 2887 Sunnyvale CA");
		five.setTransactionDate("11/17");
		
		Transaction six = new Transaction();
		six.setWithdrawals(8.52);
		six.setCategory("Gas");
		six.setDescription("Purchase authorized on 11/17 Safeway Store 2887 Sunnyvale CA");
		six.setTransactionDate("11/17");
		
		Transaction seven = new Transaction();
		seven.setWithdrawals(1.68);
		seven.setCategory("Postage");
		seven.setDescription("Purchase authorized on 11/17 USPS PO 05195601 21701 St");
		seven.setTransactionDate("11/17");
		seven.setBalance(61.85);
		
		Transaction eight = new Transaction();
		eight.setAmount("100");
		eight.setCategory("Gas");
		eight.setDescription("Online Transfer From Keypoint Cu Chk xxxx4305 G. Bedekar");
		eight.setTransactionDate("11/20");
		
		
		
		Transaction nine = new Transaction();
		nine.setWithdrawals(3.75);
		nine.setCategory("Gas");
		nine.setDescription("Purchase authorized on 11/16 Adobe San Jose5125 San Jose CA");
		nine.setTransactionDate("11/20");
		
		Transaction ten = new Transaction();
		ten.setWithdrawals(12.69);
		ten.setCategory("Gas");
		ten.setDescription("Purchase authorized on 11/17 Chick-Fil-A #02772 Sunnyvale CA");
		ten.setTransactionDate("11/20");
		
		Transaction eleven = new Transaction();
		eleven.setWithdrawals(21.59);
		eleven.setCategory("Restaurant");
		eleven.setDescription("Purchase authorized on 11/18 Biryani & Kababs Sunnyvale CA");
		eleven.setTransactionDate("11/20");
		eleven.setBalance(123.82);
		
		
		
		System.out.println("Got id ...."+id);
		System.out.println("Now Returning users....");
		listOfTransactions.add(opening);
		listOfTransactions.add(one);
		listOfTransactions.add(two);
		listOfTransactions.add(three);
		listOfTransactions.add(four);
		listOfTransactions.add(five);
		listOfTransactions.add(six);
		listOfTransactions.add(seven);
		listOfTransactions.add(eight);
		listOfTransactions.add(nine);
		listOfTransactions.add(ten);
		listOfTransactions.add(eleven);
		System.out.println("I am now returning ..."+listOfTransactions.size());
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("transactions",listOfTransactions);
		map.put("user",uc);
		map.put("summary", as);
		map.put("expenses",listOfExpenses);
		return Response.status(201).entity(map).build();
		
		
		
	}


}
