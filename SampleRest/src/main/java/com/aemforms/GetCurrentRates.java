package com.aemforms;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/getCurrentRates")
public class GetCurrentRates {
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRates(@PathParam("id")String configType){
		java.util.List<CurrentRates> currentRates = new ArrayList<>();
		HashMap<String,Object> map = new HashMap<String, Object>();
		UserClass uc = new UserClass();
		OtherSettings otherSettings = new OtherSettings();
		otherSettings.setConfigType(configType);
		
		uc.setId(configType);
	
		if(configType.equalsIgnoreCase("2"))
		{
			uc.setName("Interest to be Charged Above Brokers’ Call Money Rate");
			otherSettings.setColumnHeading("Interest to be Charged Above Brokers’ Call Money Rate");
		
			CurrentRates tier1 = new CurrentRates();
			tier1.setTier("1");
			tier1.setDebitBalance("$0–$9,999.99");
			tier1.setInterestRate("3.50%");
			CurrentRates tier2 = new CurrentRates();
			tier2.setTier("2");
			tier2.setDebitBalance("$10,000–$24,999.99");
			tier2.setInterestRate("3.25%");
			CurrentRates tier3 = new CurrentRates();
			tier3.setTier("3");
			tier3.setDebitBalance("$25,000–$99,999.99");
			tier3.setInterestRate("2.25%");
			CurrentRates tier4 = new CurrentRates();
			tier4.setTier("4");
			tier4.setDebitBalance("$100,000 -$499,000");
			tier4.setInterestRate("1.75%");
			
			CurrentRates tier5 = new CurrentRates();
			tier5.setTier("5");
			tier5.setDebitBalance("$500,000 and over");
			tier5.setInterestRate("1.50%");
			currentRates.add(tier1);
			currentRates.add(tier2);
			currentRates.add(tier3);
			currentRates.add(tier4);
			currentRates.add(tier5);
			map.put("currentRates",currentRates);
			map.put("otherSettings",otherSettings);
			
		}
		if(configType.equalsIgnoreCase("3"))
		{
			CurrentRates tier1 = new CurrentRates();
			uc.setName("Interest to be Charged Above NB LR");
			otherSettings.setColumnHeading("Interest to be Charged Above NB LR");
			tier1.setTier("1");
			tier1.setDebitBalance("Under $25,000");
			tier1.setInterestRate("2.50%");
			CurrentRates tier2 = new CurrentRates();
			tier2.setTier("2");
			tier2.setDebitBalance("$25,000–$49,999");
			tier2.setInterestRate("2.00%");
			CurrentRates tier3 = new CurrentRates();
			tier3.setTier("3");
			tier3.setDebitBalance("$50,000–$74,999");
			tier3.setInterestRate("1.50%");
			CurrentRates tier4 = new CurrentRates();
			tier4.setTier("4");
			tier4.setDebitBalance("$75,000 -$99,999");
			tier4.setInterestRate("1.00%");
			
			CurrentRates tier5 = new CurrentRates();
			tier5.setTier("5");
			tier5.setDebitBalance("$100,000 and over");
			tier5.setInterestRate(".75%");
			currentRates.add(tier1);
			currentRates.add(tier2);
			currentRates.add(tier3);
			currentRates.add(tier4);
			currentRates.add(tier5);
			map.put("currentRates",currentRates);
			map.put("otherSettings",otherSettings);
			
		}
		if(configType.equalsIgnoreCase("1")|| (configType.equalsIgnoreCase("4")))
		{
				uc.setName("Interest to be Charged Above /Below Your Base Rate");
				otherSettings.setColumnHeading("Interest to be Charged Above /Below Your Base Rate");
				CurrentRates tier1 = new CurrentRates();
				tier1.setTier("1");
				tier1.setDebitBalance("$0–$9,999.99");
				tier1.setInterestRate("+3.00%");
				CurrentRates tier2 = new CurrentRates();
				tier2.setTier("2");
				tier2.setDebitBalance("$10,000–$49,999.99");
				tier2.setInterestRate("+1.00%");
				CurrentRates tier3 = new CurrentRates();
				tier3.setTier("3");
				tier3.setDebitBalance("$50,000–$99,999.99");
				tier3.setInterestRate("-1.00%");
				CurrentRates tier4 = new CurrentRates();
				tier4.setTier("4");
				tier4.setDebitBalance("$100,000 and over");
				tier4.setInterestRate("-2.00%");
				currentRates.add(tier1);
				currentRates.add(tier2);
				currentRates.add(tier3);
				currentRates.add(tier4);
			
				map.put("currentRates",currentRates);
				//map.put("user",uc);
				map.put("otherSettings",otherSettings);
		}
				return Response.status(201).entity(map).build();
		
	
		
	}

}
