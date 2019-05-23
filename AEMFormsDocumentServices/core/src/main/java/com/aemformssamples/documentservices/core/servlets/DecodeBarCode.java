package com.aemformssamples.documentservices.core.servlets;

import com.adobe.aemfd.docmanager.Document;
import com.aemformssamples.documentservices.core.DocumentServices;
import com.mergeandfuse.getserviceuserresolver.GetResolver;
import java.io.IOException;
import java.io.PrintWriter;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;







@Component(service={Servlet.class}, property={"sling.servlet.methods=get", "sling.servlet.paths=/bin/decodebarcode"})
public class DecodeBarCode
  extends SlingSafeMethodsServlet
{
  @Reference
  DocumentServices documentServices;
  @Reference
  GetResolver getResolver;
  private static final Logger log = LoggerFactory.getLogger(DecodeBarCode.class);
  
  public DecodeBarCode() {}
  
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) { ResourceResolver fd = getResolver.getFormsServiceResolver();
    Node pdfDoc = (Node)fd.getResource(request.getParameter("pdfPath")).adaptTo(Node.class);
    Document pdfDocument = null;
    log.debug("The path of the pdf I got was " + request.getParameter("pdfPath"));
    try {
      pdfDocument = new Document(pdfDoc.getPath());
      JSONObject decodedData = documentServices.extractBarCode(pdfDocument);
      response.setContentType("application/json");
      response.setHeader("Cache-Control", "nocache");
      response.setCharacterEncoding("utf-8");
      PrintWriter out = null;
      out = response.getWriter();
      out.println(decodedData.toString());
    }
    catch (RepositoryException|IOException e1) {
      e1.printStackTrace();
    }
  }
  
  private static final long serialVersionUID = 1L;
}
