package com.aemformssamples.documentservices.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aemformssamples.documentservices.core.DocumentServices;
import com.aemformssamples.documentservices.core.SortJSONArray;
import com.mergeandfuse.getserviceuserresolver.GetResolver;

@Component(service = { Servlet.class }, property = { "sling.servlet.methods=post",
		"sling.servlet.paths=/bin/stitchxdpfragments" })
public class StichFragments2 extends SlingAllMethodsServlet {
	@Reference
	DocumentServices documentServices;
	@Reference
	GetResolver getResolver;
	private static final Logger log = LoggerFactory.getLogger(StichFragments2.class);
	private static final long serialVersionUID = 1L;

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		System.out.println("The #### stich fragments is called");
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		String fileName;
		JSONArray fragments = null;
		String templateName = null;
		try {
			br = request.getReader();
			while ((fileName = br.readLine()) != null) {
				sb.append(fileName);
			}
			fragments = new JSONArray(sb.toString());
			JSONObject jsonWithTemplateName = fragments.getJSONObject(fragments.length() - 1);
			System.out.println("The json object with template information" + fragments.get(fragments.length() - 1));
			templateName = jsonWithTemplateName.getString("templateName");
		} catch (IOException | JSONException ioException) {

		}
		System.out.println("##### Number of fragments to assemble is   " + fragments.length());
		Map<Integer, Map> assembleFragmentsMap = new HashMap();
		JSONArray sortedfragments = new JSONArray();
		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < fragments.length() - 1; i++) {
			{
				try {
					jsonValues.add(fragments.getJSONObject(i));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Collections.sort(jsonValues, new SortJSONArray());
			System.out.println("After sorting");
			for (int k = 0; k < jsonValues.size(); k++) {
				JSONObject jsonObj = jsonValues.get(k);
				System.out.println("The json object is " + jsonObj.toString());
				String fragmentName = null;
				String fragment = null;
				try {
					fragmentName = (String) jsonObj.get("fragmentName");
					fragment = fragmentName.substring(0, (fragmentName.length() - 4));
					System.out.println("The fragmentName is " + fragmentName + " fragment  is " + fragment);
					Map<String, String> fragmentsMap = new HashMap<String, String>();
					fragmentsMap.put("insertionPoint", "position1");
					fragmentsMap.put("source", fragmentName);
					fragmentsMap.put("fragment", fragment);
					assembleFragmentsMap.put(k + 1, fragmentsMap);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
		com.adobe.aemfd.docmanager.Document ddxDocument = documentServices.createDDXForFragments(templateName,
				assembleFragmentsMap);
		String path = documentServices.insertFragments(templateName, assembleFragmentsMap, ddxDocument);

	}
}
