package com.aemforms;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;

@Path("/getCallableNote")
public class GetCallableNotes {
	@GET
	@Path("{faceAmount}/{discounRate}/{maturity}/{settlement}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProceedsToIssuer(@PathParam("faceAmount")String faceAmount,@PathParam("discounRate")String discounRate,@PathParam("maturity")String maturity,@PathParam("settlement")String settlement)
	{
		System.out.println("The face amount I got was ..."+faceAmount);
		JSONObject jObject = new JSONObject();
		jObject.put("valid",true);
		
		
		CallableNote cn = new CallableNote();
		
		cn.setFaceAmount(Double.parseDouble(faceAmount));
		cn.setDiscountRate(Double.parseDouble(discounRate));
		try {
			cn.setDifferenceInDays(maturity.replaceAll("\\.","/"), settlement.replaceAll("\\.","/"));
			cn.setProceedsToIssuer(Double.parseDouble(faceAmount),Double.parseDouble(discounRate));
			cn.setCallPeriod(maturity.replaceAll("\\.","/"));
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("The Response returning I got was ..."+Response.status(201).entity(cn).build());
		jObject.put("proceedsToIssuer",cn.getProceedsToIssuer());
		jObject.put("faceAmount",cn.getFaceAmount());
		jObject.put("maturityDate",cn.getMaturityDate());
		jObject.put("settlementDate",cn.getSettlementDate());
		jObject.put("settlementDate",cn.getSettlementDate());
		jObject.put("callPeriod",cn.getCallPeriod());
		jObject.put("initialPurchasePrice",cn.getInitialPurchasePrice());
		
		
	
		return Response.status(Status.OK).entity(jObject.toString()).build();
	}

}
