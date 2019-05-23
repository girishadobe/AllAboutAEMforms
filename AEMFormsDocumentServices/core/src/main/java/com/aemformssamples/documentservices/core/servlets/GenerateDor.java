package com.aemformssamples.documentservices.core.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aemfd.docmanager.Document;
import com.aemformssamples.documentservices.core.DocumentServices;
import com.mergeandfuse.getserviceuserresolver.GetResolver;

@Component(service = { Servlet.class }, property = { "sling.servlet.methods=post",
		"sling.servlet.paths=/bin/generateDor" })
public class GenerateDor extends SlingAllMethodsServlet {
	@Reference
	DocumentServices documentServices;
	@Reference
	GetResolver getResolver;
	private static final Logger log = LoggerFactory.getLogger(GenerateDor.class);

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		System.out.println("The afname is " + request.getParameter(":selfUrl") + "attachments are "
				+ request.getParameter("fileAttachmentMap"));

		String dataXml = request.getParameter("jcr:data");

		try {

			Document documentToReturn = documentServices.GenerateDor(request.getParameter(":selfUrl"), dataXml);
			InputStream fileInputStream = documentToReturn.getInputStream();
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
