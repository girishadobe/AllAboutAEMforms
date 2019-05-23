package com.aemformssamples.documentservices.core.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.adobe.aemfd.docmanager.Document;
import com.adobe.fd.assembler.service.AssemblerService;
import com.aemformssamples.documentservices.core.DocumentServices;
import com.mergeandfuse.getserviceuserresolver.GetResolver;

@Component(service = { Servlet.class }, property = { "sling.servlet.methods=post",
		"sling.servlet.paths=/bin/assemblefiles" })
public class AssembleUploadedFiles extends SlingAllMethodsServlet {
	@Reference
	AssemblerService assemblerService;
	@Reference
	GetResolver getResolver;
	@Reference
	DocumentServices documentServices;
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(AssembleUploadedFiles.class);

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		doPost(request, response);
	}

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		System.out.println("In Assemble Uploaded Files");

		Map<String, Object> mapOfDocuments = new HashMap<String, Object>();
		final boolean isMultipart = org.apache.commons.fileupload.servlet.ServletFileUpload.isMultipartContent(request);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		org.w3c.dom.Document ddx = docBuilder.newDocument();
		Element rootElement = ddx.createElementNS("http://ns.adobe.com/DDX/1.0/", "DDX");

		ddx.appendChild(rootElement);
		Element pdfResult = ddx.createElement("PDF");
		pdfResult.setAttribute("result", "GeneratedDocument.pdf");
		rootElement.appendChild(pdfResult);
		if (isMultipart) {
			final java.util.Map<String, org.apache.sling.api.request.RequestParameter[]> params = request
					.getRequestParameterMap();
			for (final java.util.Map.Entry<String, org.apache.sling.api.request.RequestParameter[]> pairs : params
					.entrySet()) {
				final String k = pairs.getKey();

				final org.apache.sling.api.request.RequestParameter[] pArr = pairs.getValue();
				final org.apache.sling.api.request.RequestParameter param = pArr[0];

				try {
					if (!param.isFormField()) {
						final InputStream stream = param.getInputStream();
						log.debug("the file name is " + param.getFileName());
						log.debug("Got input Stream inside my servlet####" + stream.available());
						com.adobe.aemfd.docmanager.Document document = new Document(stream);
						mapOfDocuments.put(param.getFileName(), document);
						org.w3c.dom.Element pdfSourceElement = ddx.createElement("PDF");
						pdfSourceElement.setAttribute("source", param.getFileName());
						pdfSourceElement.setAttribute("bookmarkTitle", param.getFileName());
						pdfResult.appendChild(pdfSourceElement);
						log.debug("The map size is " + mapOfDocuments.size());
					} else {
						log.debug("The form field is" + param.getString());

					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		com.adobe.aemfd.docmanager.Document ddxDocument = documentServices.orgw3cDocumentToAEMFDDocument(ddx);
		Document assembledDocument = documentServices.assembleDocuments(mapOfDocuments, ddxDocument);
		String path = documentServices.saveDocumentInCrx("/content/ocrfiles", assembledDocument);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("path", path);
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "nocache");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = null;
			out = response.getWriter();
			out.println(jsonObject.toString());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
