package com.aemforms.setvalue.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
@Component(property={Constants.SERVICE_DESCRIPTION+"=Set Value",
		Constants.SERVICE_VENDOR+"=Adobe Systems",
		"process.label"+"=Set Value of Element in Xml"})
public class SetValueinXml implements WorkflowProcess {
	private static final Logger log = LoggerFactory.getLogger(SetValueinXml.class);

	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap arg2) throws WorkflowException {
		log.debug("The string I got was ..."+arg2.get("PROCESS_ARGS","string").toString());
		String params = arg2.get("PROCESS_ARGS","string").toString();
		String parameters[] = params.split(",");
		String nodeName = parameters[0];
		String value = parameters[1];
		String payloadPath = workItem.getWorkflowData().getPayload().toString();
		log.debug("The payload  in HandleCMSubmission is "+workItem.getWorkflowData().getPayload().toString());
		String dataFilePath = payloadPath+"/Data.xml/jcr:content";
		Session session = workflowSession.adaptTo(Session.class);
		 DocumentBuilderFactory factory = null;
		    DocumentBuilder builder = null;
		    Document xmlDocument= null;
		Node xmlDataNode = null;
		try {
			xmlDataNode = session.getNode(dataFilePath);
		} catch (PathNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (RepositoryException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
				InputStream xmlDataStream = xmlDataNode.getProperty("jcr:data").getBinary().getStream();
				log.debug("Got InputStream.... and the size available is ..."+xmlDataStream.available());
				XPath xPath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
				factory = DocumentBuilderFactory.newInstance();
		      	builder = factory.newDocumentBuilder();
		      	xmlDocument = builder.parse(xmlDataStream);
		      	org.w3c.dom.Node node = (org.w3c.dom.Node)xPath.compile(nodeName).evaluate(xmlDocument, javax.xml.xpath.XPathConstants.NODE);
		       	log.debug("%%%%Bingo Getting node text content"+node.getTextContent()); 
		     	node.setTextContent(value);
		     	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				DOMSource source = new DOMSource(xmlDocument);
				StreamResult outputTarget = new StreamResult(outputStream);
				TransformerFactory.newInstance().newTransformer().transform(source, outputTarget);
				InputStream is1 = new ByteArrayInputStream(outputStream.toByteArray());
				Binary binary = session.getValueFactory().createBinary(is1);
				xmlDataNode.setProperty("jcr:data", binary);
				session.save();
		     
		     
		}
		catch(Exception e)
		{
			log.debug("Got error"+e.getMessage());
		}


	}

}
