package com.aemforms;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

@Path("/getMembers")
public class GetMembers {
	@GET
	@Path("{ssn}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCreditScore(@PathParam("ssn") String ssn) {
		GetResultsFromDB getDB = new GetResultsFromDB();
		JSONArray jasonArray = getDB.getResultSet("SELECT * FROM leads.eob");
		JSONObject jo = jasonArray.getJSONObject(0);
		System.out.println("The zip is  is " + jo.getJSONObject("data").getJSONObject("member").getString("zip"));
		return Response.status(201).entity(jasonArray.toString()).build();
	}

}
