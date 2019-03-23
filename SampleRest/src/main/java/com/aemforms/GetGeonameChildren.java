package com.aemforms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Path("/getChildren")
public class GetGeonameChildren {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGeonameSiblings(@QueryParam("geonameId") String geonameId,
			@QueryParam("username") String username) {
		System.out.println("Getting .... childrenOf of  geonameId " + geonameId);
		try {
			HttpHost server = new HttpHost("api.geonames.org", 80, "http");
			HttpGet getReq = new HttpGet("/children?geonameId=" + geonameId + "&username=gbedekar");
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse result = httpClient.execute(server, getReq);
			System.out.println("The response I got was" + result.getStatusLine().getStatusCode());
			XPathFactory factory = XPathFactory.newInstance();
			XPath xPath = factory.newXPath();
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			// Document responseXml =
			// dBuilder.parse(result.getEntity().getContent());
			NodeList geonames = (NodeList) xPath.evaluate("/geonames/geoname",
					new InputSource(result.getEntity().getContent()), XPathConstants.NODESET);

			// org.w3c.dom.NodeList geonamesList =
			// responseXml.getElementsByTagName("geoname");
			List<Geoname> listofGeonames = new ArrayList<>();
			for (int i = 0; i < geonames.getLength(); i++) {
				Element geonameElementNode = (Element) geonames.item(i);
				Geoname geoname = new Geoname();
				geoname.setToponymName(geonameElementNode.getElementsByTagName("name").item(0).getTextContent());
				geoname.setGeonameId(geonameElementNode.getElementsByTagName("geonameId").item(0).getTextContent());
				geoname.setCountryCode(geonameElementNode.getElementsByTagName("countryCode").item(0).getTextContent());
				geoname.setCountryName(geonameElementNode.getElementsByTagName("countryName").item(0).getTextContent());
				listofGeonames.add(geoname);
				System.out.println(
						"The name  is" + geonameElementNode.getElementsByTagName("name").item(0).getTextContent());
			}
			HashMap<String, List<Geoname>> map = new HashMap<String, List<Geoname>>();
			map.put("geonames", listofGeonames);
			return Response.status(201).entity(map).build();

		}

		catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
