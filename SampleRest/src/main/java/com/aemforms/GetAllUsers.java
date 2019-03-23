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

@Path("/getuserslist")
public class GetAllUsers {
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsersList(@PathParam("id")String id)
	{
		java.util.List<UserClass> listOfUsers = new ArrayList<>();
		UserClass a = new UserClass();
		a.setAge(98);
		a.setName("Peter");
		a.setId(id);
		
		UserClass uc = new UserClass();
		uc.setAge(30);
		uc.setName("Girish");
		uc.setId("007");
		System.out.println("Got id ...."+id);
		System.out.println("Now Returning users....");
		listOfUsers.add(a);
		listOfUsers.add(uc);
		System.out.println("I am now returning ..."+listOfUsers.size());
		HashMap<String,List<UserClass>> map = new HashMap<String, List<UserClass>>();
		map.put("users",listOfUsers);
		return Response.status(201).entity(listOfUsers).build();
		
		
		
	}


}
