package com.aemforms;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/getuser")
public class GetUser {
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsersList(@PathParam("id")String id)
	{
		UserClass user = new UserClass();
		user.setAge(98);
		user.setName("Peter");
		user.setId(id);
		user.setAddress("345 Park Ave");
		user.setCity("San Jose");
		user.setZip("94086");
		user.setOnlineBanking(1);
		user.setDirectDeposit(0);
		return Response.status(201).entity(user).build();
		
		
		
	}


}
