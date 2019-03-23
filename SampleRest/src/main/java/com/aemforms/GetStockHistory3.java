package com.aemforms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

@Path("/getStockPrices3")
public class GetStockHistory3 {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStockPrices(@QueryParam("stocksymbols") List<String> stocksymbols,
			@QueryParam("api_token") String api_token, @QueryParam("date_from") String date_from,
			@QueryParam("date_to") String date_to) {

		System.out.println("Getting .... stock quotes for " + stocksymbols.get(0));
		String parametersGot = stocksymbols.get(0).substring(1, stocksymbols.get(0).length() - 1);
		System.out.println("Getting .... stock quotes for " + parametersGot);

		// String[] stockSymbolarray = { "adbe", "msft", "orcl" };
		// String[] stockSymbolArray = parametersGot.split(",");

		List<StockPrices> stockPricesList = new ArrayList();
		for (int i = 0; i < stocksymbols.size(); i++) {
			String stocksymbol = stocksymbols.get(i);
			// stocksymbol = stocksymbol.substring(1, stocksymbol.length() - 1);
			System.out.println("Getting .... stock quotes for  " + stocksymbol);
			String requestURL = "https://www.worldtradingdata.com/api/v1/history?symbol=AAPL&sort=newest&api_token=HZjfUqtZI9Qy4Nm596D14DLcDFMfRn7ObRpeuYwXgGrghrOVfcCXLKV5GXPT&date_from=2018-01-01&date_to=2018-01-03";
			try {
				HttpHost server = new HttpHost("worldtradingdata.com", 443, "https");
				HttpGet getReq = new HttpGet("/api/v1/history?symbol=" + stocksymbol + "&sort=newest&api_token="
						+ api_token + "&date_from=" + date_from + "&date_to=" + date_to);
				HttpClient httpClient = HttpClientBuilder.create().build();
				HttpResponse result = httpClient.execute(server, getReq);
				InputStream instream = result.getEntity().getContent();
				String jsonString = convertStreamToString(instream);
				JSONObject jsonResponse = new JSONObject(jsonString);
				JSONObject history = jsonResponse.getJSONObject("history");
				Iterator keys = history.keys();
				List<ClosingPrice> closingPrices = new ArrayList<>();
				StockPrices stockPrices = new StockPrices();
				stockPrices.setName(stocksymbol);
				List<JSONObject> jsonValues = new ArrayList<JSONObject>();
				int k = 0;
				while (keys.hasNext()) {
					String key = (String) keys.next();
					// jsonValues.add(history.getJSONObject(key));
					ClosingPrice closingPrice = new ClosingPrice();
					closingPrice.setDate(key);
					closingPrice.setClosing(history.getJSONObject(key).getDouble("close"));
					System.out.println("The closing price is  " + history.getJSONObject(key).getDouble("close")
							+ " closing date " + key);
					closingPrices.add(closingPrice);
				}
				Collections.sort(closingPrices, new SortJSONArray());
				stockPrices.setClosingPrices(closingPrices);
				stockPricesList.add(stockPrices);

				// System.out.println("The response I got was" +
				// stockPriceHistory.toString());
				// return Response.ok(historicalPrices,
				// MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {
				System.out.println("The error is " + e.getMessage());

			}
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("Stocks", stockPricesList);
		HashMap<String, Object> historicalPrices = new HashMap<String, Object>();
		historicalPrices.put("stockprices", map);
		return Response.ok(historicalPrices, MediaType.APPLICATION_JSON).build();

	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
