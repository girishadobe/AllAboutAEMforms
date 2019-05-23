package com.aemformssamples.documentservices.core.servlets;

import com.adobe.pdfg.exception.ConversionException;
import com.adobe.pdfg.exception.FileFormatNotSupportedException;
import com.adobe.pdfg.exception.InvalidParameterException;
import com.aemformssamples.documentservices.core.DocumentServices;
import com.mergeandfuse.getserviceuserresolver.GetResolver;
import java.io.IOException;
import java.io.PrintWriter;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








@Component(service={Servlet.class}, property={"sling.servlet.methods=get", "sling.servlet.paths=/bin/ocr"})
public class OcrTiffImages
  extends SlingSafeMethodsServlet
{
  @Reference
  DocumentServices documentServices;
  @Reference
  GetResolver getResolver;
  private static final Logger log = LoggerFactory.getLogger(OcrTiffImages.class);
  
  public OcrTiffImages() {}
  
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) { String fileName = request.getParameter("fileName");
    String tiffFile = request.getParameter("jcrPath");
    try {
      String savedOcrFile = documentServices.ocrDocument(tiffFile, fileName);
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("path", savedOcrFile);
      response.setContentType("application/json");
      response.setHeader("Cache-Control", "nocache");
      response.setCharacterEncoding("utf-8");
      PrintWriter out = null;
      out = response.getWriter();
      out.println(jsonObject.toString());
    }
    catch (ConversionException|InvalidParameterException|FileFormatNotSupportedException|RepositoryException|IOException e)
    {
      e.printStackTrace();
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
  }
  
  private static final long serialVersionUID = 1L;
}
