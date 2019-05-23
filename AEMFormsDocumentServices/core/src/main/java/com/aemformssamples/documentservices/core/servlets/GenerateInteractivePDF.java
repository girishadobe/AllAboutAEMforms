package com.aemformssamples.documentservices.core.servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.aemfd.docmanager.Document;
import com.aemformssamples.documentservices.core.DocumentServices;

@Component(service = { Servlet.class }, property = { "sling.servlet.methods=post",
		"sling.servlet.paths=/bin/generateinteractivepdf" })

public class GenerateInteractivePDF extends SlingAllMethodsServlet {
	@Reference
	DocumentServices documentServices;

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		doPost(request, response);
	}

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		String dataXml = request.getParameter("formData");
		System.out.println("The data xml is " + dataXml);
		/*
		 * StringBuffer stringBuffer = new StringBuffer(); String line = null;
		 * try { InputStreamReader isReader = new
		 * InputStreamReader(request.getInputStream(), "UTF-8");
		 * System.out.println("Got IS ####"); // BufferedReader reader =
		 * request.getReader(); BufferedReader reader = new
		 * BufferedReader(isReader); while ((line = reader.readLine()) != null)
		 * stringBuffer.append(line); } catch (Exception e) {
		 * System.out.println("Error"); } String xmlData = new
		 * String(stringBuffer);
		 */
		org.w3c.dom.Document xmlDataDoc = documentServices.w3cDocumentFromStrng(dataXml);
		Document xmlDocument = documentServices.orgw3cDocumentToAEMFDDocument(xmlDataDoc);

		// System.out.println(xmlData);
		Document generatedPDF = documentServices.mobileFormToInteractivePdf(xmlDocument);
		try {
			generatedPDF.copyToFile(new File("c:\\scrap\\mobileform.pdf"));
			InputStream fileInputStream = generatedPDF.getInputStream();
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=AemFormsRocks.pdf");
			response.setContentLength((int) fileInputStream.available());
			OutputStream responseOutputStream = response.getOutputStream();
			int bytes;
			while ((bytes = fileInputStream.read()) != -1) {
				responseOutputStream.write(bytes);
			}
			responseOutputStream.flush();
			responseOutputStream.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
