package com.aemforms;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

@Path("/getEOB")
public class EoB {
	@GET
	@Path("{ssn}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCreditScore(@PathParam("ssn") String ssn) {
		GetResultsFromDB getDB = new GetResultsFromDB();
		JSONArray jasonArray = getDB.getResultSet("SELECT * FROM leads.eob where id =" + ssn);
		JSONObject jo = jasonArray.getJSONObject(0);
		System.out
				.println("The zip code is is is " + jo.getJSONObject("data").getJSONObject("member").getString("zip"));
		if (ssn.equalsIgnoreCase("000")) {
			ErrorResponse er = new ErrorResponse();
			er.setMessage("Invalid Social Security Number");
			return Response.status(405).entity(er).build();
		} else {
			UserClass userClass = new UserClass();
			userClass.setAddress("345 Park Ave");
			userClass.setCity("San Jose");
			userClass.setName("John Jacobs");
			userClass.setState("CA");
			userClass.setZip("94087");
			userClass.setGroupName("WeFinance HSA");
			userClass.setGroupNumber("394847");
			userClass.setId(ssn);
			KeyTerms kt1 = new KeyTerms();
			kt1.setAmount("$100");
			kt1.setDescription("The amount your doctor or health care provider billed for services");
			kt1.setTerm("Amount billed");

			KeyTerms kt2 = new KeyTerms();
			kt2.setAmount("$75");
			kt2.setDescription(
					"The agreed upon amount your doctor or health care provider in our network accepts as their fee");
			kt2.setTerm("Member rate");

			java.util.List<KeyTerms> keyTerms = new ArrayList<>();
			keyTerms.add(kt1);
			keyTerms.add(kt2);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("member", userClass);
			map.put("keyTerms", keyTerms);
			// return Response.status(201).entity(map).build();
			return Response.status(201).entity(jo.getJSONObject("data").toString()).build();
		}

	}

}
