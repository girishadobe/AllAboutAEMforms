package com.aemforms;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

@Path("/getCreditScore")
public class GetCreditScore {
	@GET
	@Path("{ssn}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCreditScore(@PathParam("ssn") String ssn) {
		JSONObject employee = new JSONObject();
		employee.put("Fulltime", true);
		employee.put("Department", "Finance");

		if (ssn.equalsIgnoreCase("000")) {
			ErrorResponse er = new ErrorResponse();
			er.setMessage("Invalid Social Security Number");
			return Response.status(405).entity(er).build();
		} else {
			CreditScore cs = new CreditScore();
			cs.setCreditScore(ssn);
			cs.setJsonData(employee.toString());
			// return Response.ok(cs).type(MediaType.APPLICATION_JSON).build();
			return Response.status(201).entity(cs).build();
		}

	}

}
