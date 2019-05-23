package com.aemformssamples.documentservices.core.servlets;

import com.adobe.aemfd.docmanager.Document;
import com.adobe.fd.forms.api.FormsService;
import com.aemformssamples.documentservices.core.DocumentServices;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.Servlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








@Component(service={Servlet.class}, property={"sling.servlet.methods=get", "sling.servlet.paths=/bin/renderandextend"})
public class RenderAndReaderExtend
  extends SlingSafeMethodsServlet
{
  @Reference
  FormsService formsService;
  @Reference
  DocumentServices documentServices;
  private static final Logger log = LoggerFactory.getLogger(RenderAndReaderExtend.class);
  
  public RenderAndReaderExtend() {}
  
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) { log.debug("The path of the XDP I got was " + request.getParameter("xdpPath"));
    Document renderedPDF = documentServices.renderAndExtendXdp(request.getParameter("xdpPath"));
    response.setContentType("application/pdf");
    response.addHeader("Content-Disposition", "attachment; filename=AemFormsRocks.pdf");
    try {
      response.setContentLength((int)renderedPDF.length());
      InputStream fileInputStream = null;
      fileInputStream = renderedPDF.getInputStream();
      OutputStream responseOutputStream = null;
      responseOutputStream = response.getOutputStream();
      int bytes;
      while ((bytes = fileInputStream.read()) != -1)
      {
        responseOutputStream.write(bytes);
      }
      
    }
    catch (IOException e2)
    {
      e2.printStackTrace();
    }
  }
  
  private static final long serialVersionUID = 1L;
}
