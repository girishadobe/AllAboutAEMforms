package com.aemforms.setvalue.core;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;


@Component(property={Constants.SERVICE_DESCRIPTION+"=CreateUniqueFileName",
		Constants.SERVICE_VENDOR+"=Adobe Systems",
		"process.label"+"=Create Unique File Name"})
public class CreateUniqueFileName implements WorkflowProcess {
	private static final Logger log = LoggerFactory.getLogger(CreateUniqueFileName.class);
	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap arg2) throws WorkflowException {
		// TODO Auto-generated method stub
		log.debug("The process arguments passed ..."+arg2.get("PROCESS_ARGS","string").toString());
		String params = arg2.get("PROCESS_ARGS","string").toString();
					
		String parameters[] = params.split(",");
		log.debug("The %%%% length of parameters is "+parameters.length);
		
		Map<String, String> map = new HashMap<>();
	   
		WorkflowData wfData = workItem.getWorkflowData();
		String payloadPath = workItem.getWorkflowData().getPayload().toString();
		
		String dataFilePath = payloadPath+"/Data.xml/jcr:content";
		Session session = workflowSession.adaptTo(Session.class);
		workflowSession.updateWorkflowData(workItem.getWorkflow(),wfData);
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Document xmlDocument= null;
		Node xmlDataNode = null;
		try
			{
				xmlDataNode = session.getNode(dataFilePath);
			}
		catch (PathNotFoundException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		catch (RepositoryException e1)
			{
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
		      	String fieldValue = "";
		      	for(int i=0;i<parameters.length;i++)
				{
		      		String fieldName = parameters[i];
		      		org.w3c.dom.Node node = (org.w3c.dom.Node)xPath.compile(fieldName).evaluate(xmlDocument, javax.xml.xpath.XPathConstants.NODE);
					log.debug("###The value is "+node.getTextContent());
					if(i>0)
					{
						fieldValue = fieldValue+"_"+node.getTextContent();
					}
					else
					{
						fieldValue = node.getTextContent();
					}
					
				log.debug("the field value is "+fieldValue);
				}
		      	wfData.getMetaDataMap().put("filename",fieldValue+".pdf");

		      	workflowSession.updateWorkflowData(workItem.getWorkflow(),wfData);
		       	log.debug("$$$$ Done updating the map");
		       	xmlDataStream.close();
       	
		}
		catch(Exception e)
		{
			
		}

	}

}
