package com.aemforms;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/getstocktransactions")
public class GetStockTransactions {
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsersList(@PathParam("id") String id) {
		UserClass uc = new UserClass();
		uc.setName("Gloria Rios");
		uc.setAddress("345 Park Ave");
		uc.setId(id);
		uc.setCity("San Jose");
		uc.setZip("95110");
		uc.setState("CA");
		uc.setCredit(50000);

		java.util.List<StockTransaction> stockTransactiions = new ArrayList<>();
		StockTransaction a = new StockTransaction();
		a.setPricePaid(234.98);
		a.setQuantity(90);
		a.setStockSymbol("ADBE");

		StockTransaction b = new StockTransaction();
		b.setPricePaid(129.49);
		b.setQuantity(100);
		b.setStockSymbol("TTD");

		StockTransaction c = new StockTransaction();
		c.setPricePaid(129.49);
		c.setQuantity(70);
		c.setStockSymbol("PAYC");

		stockTransactiions.add(a);
		stockTransactiions.add(b);
		stockTransactiions.add(c);
		System.out.println("I am now returning ..." + stockTransactiions.size());
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("transactions", stockTransactiions);
		map.put("user", uc);
		return Response.status(201).entity(map).build();

	}

}
