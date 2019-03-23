package com.aemforms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.json.JSONArray;
import org.json.JSONObject;

@Path("/getMutualFundPrices")
public class GetMutualFundHistory {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStockPrices(@QueryParam("stocksymbols") List<String> stocksymbols,
			@QueryParam("api_token") String api_token) {

		System.out.println("Getting .... stock quotes for " + stocksymbols.get(0));
		// String parametersGot = stocksymbols.get(0).substring(1,
		// stocksymbols.get(0).length() - 1);
		// System.out.println("Getting .... stock quotes for " + parametersGot);

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
				HttpGet getReq = new HttpGet("/api/v1/mutualfund?symbol=" + stocksymbol + "&api_token=" + api_token);
				HttpClient httpClient = HttpClientBuilder.create().build();
				HttpResponse result = httpClient.execute(server, getReq);
				InputStream instream = result.getEntity().getContent();
				String jsonString = convertStreamToString(instream);
				JSONObject jsonResponse = new JSONObject(jsonString);
				JSONArray dataArray = jsonResponse.getJSONArray("data");
				List<MutualFundReturns> mfReturns;
				List<MutualFundDetails> mutualFundsList = new ArrayList<>();
				String[] returnStrings = { "return_52week", "return_156week", "return_260week" };

				for (int k = 0; k < dataArray.length(); k++) {
					mfReturns = new ArrayList<>();
					JSONObject data = dataArray.getJSONObject(k);
					// System.out.println("The data object is " +
					// data.toString());
					for (int j = 0; j < 3; j++) {
						MutualFundReturns mfReturn = new MutualFundReturns();
						mfReturn.setTerm(returnStrings[j]);
						mfReturn.setPerformance(Double.parseDouble(data.getString(returnStrings[j])));

						System.out.println("Adding fund name" + data.getString("name") + "Added mfReturn : term  "
								+ mfReturn.getTerm() + "return:" + mfReturn.getPerformance());
						mfReturns.add(j, mfReturn);

					}

					MutualFundDetails mfFund = new MutualFundDetails();
					mfFund.setName(data.getString("name"));
					// mfFund.setYearlyReturn(Double.parseDouble(data.getString("return_52week")));
					// mfFund.setThreeYearReturn(Double.parseDouble(data.getString("return_156week")));
					// mfFund.setFiveYearReturn(Double.parseDouble(data.getString("return_260week")));
					mfFund.setFundPerformance(mfReturns);

					mutualFundsList.add(mfFund);

				}
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("MutualFunds", mutualFundsList);
				HashMap<String, Object> outerMap = new HashMap<String, Object>();
				outerMap.put("Results", map);
				HashMap<String, Object> finalMap = new HashMap<String, Object>();
				finalMap.put("FundDetails", outerMap);
				HashMap<String, Object> responseMap = new HashMap<String, Object>();
				finalMap.put("Response", finalMap);

				return Response.ok(responseMap, MediaType.APPLICATION_JSON).build();
			} catch (Exception e) {

			}
		}
		return null;

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
