package com.aemforms.setvalue.core;

import java.io.InputStream;
import java.util.Dictionary;
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


@Component(property={Constants.SERVICE_DESCRIPTION+"=Set MetaData",
		Constants.SERVICE_VENDOR+"=Adobe Systems",
		"process.label"+"=Set Workflow MetaData"})
public class SetWorkflowMetaData implements WorkflowProcess {
	private static final Logger log = LoggerFactory.getLogger(SetWorkflowMetaData.class);
	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap arg2) throws WorkflowException {
		// TODO Auto-generated method stub
		log.debug("The process arguments passed ..."+arg2.get("PROCESS_ARGS","string").toString());
		String params = arg2.get("PROCESS_ARGS","string").toString();
		String parameters[] = params.split(",");
		log.debug("The length of parameters is "+parameters.length);
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
		      	for(int i=0;i<parameters.length;i++)
				{
		      		String nameValuePair = parameters[i];
		      		log.debug("###The string I got was ..."+nameValuePair);
		    		String nameAndValue[] = nameValuePair.split("=");
					String propertyName = nameAndValue[0];
					String nameOfNode = nameAndValue[1];
					log.debug("####The property name  to create is "+propertyName);
					org.w3c.dom.Node node = (org.w3c.dom.Node)xPath.compile(nameOfNode).evaluate(xmlDocument, javax.xml.xpath.XPathConstants.NODE);
					log.debug("###The value is "+node.getTextContent());
					wfData.getMetaDataMap().put(propertyName.trim(),node.getTextContent());
					//workflowSession.updateWorkflowData(workItem.getWorkflow(),wfData);
					log.debug("####Created property "+propertyName.trim()+" and set its value to"+node.getTextContent());
				}
	      	
	       	workflowSession.updateWorkflowData(workItem.getWorkflow(),wfData);
	       	xmlDataStream.close();
       	}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

}
