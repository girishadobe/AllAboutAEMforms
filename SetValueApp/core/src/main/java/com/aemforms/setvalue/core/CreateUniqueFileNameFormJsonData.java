package com.aemforms.setvalue.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;

import org.apache.sling.commons.json.JSONObject;
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
		"process.label"+"=Create Unique File Name From Json Data Elements"})
public class CreateUniqueFileNameFormJsonData implements WorkflowProcess {
	private static final Logger log = LoggerFactory.getLogger(CreateUniqueFileNameFormJsonData.class);
	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap arg2) throws WorkflowException {
		// TODO Auto-generated method stub
		log.debug("The process arguments passed ..."+arg2.get("PROCESS_ARGS","string").toString());
		String params = arg2.get("PROCESS_ARGS","string").toString();
					
		String parameters[] = params.split(",");
		log.debug("The %%%% length of parameters is "+parameters.length);
		WorkflowData wfData = workItem.getWorkflowData();
		String payloadPath = workItem.getWorkflowData().getPayload().toString();
		String dataFilePath = payloadPath+"/Data.xml/jcr:content";
		Session session = workflowSession.adaptTo(Session.class);
		workflowSession.updateWorkflowData(workItem.getWorkflow(),wfData);
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
				BufferedReader streamReader = new BufferedReader(new InputStreamReader(xmlDataStream,"UTF-8"));
			    StringBuilder responseStrBuilder = new StringBuilder();
			    String inputStr;
			    while ((inputStr = streamReader.readLine()) != null)
			        responseStrBuilder.append(inputStr);
			    JSONObject jo = new JSONObject(responseStrBuilder.toString());

		      	String fieldValue = "";
		      	for(int i=0;i<parameters.length;i++)
				{
		      		JSONObject finalObject = null;
		      			
		    		
		    		String []jsonObjects= parameters[i].split("\\.");
		    	   		
		    		String propertyName = jsonObjects[jsonObjects.length-1];
					log.debug("The property name to extract is ..."+propertyName);
					for(int j=0;j<jsonObjects.length-1;j++)
					{
						if(j==0)
				    	{
				    		finalObject = jo.getJSONObject(jsonObjects[j]);
				    	}
				    	else
				    	{
				    		finalObject = finalObject.getJSONObject(jsonObjects[j]);
				    		System.out.println("The name is .."+finalObject.toString());
				    	}
				    	
					}
		      		
		      		
				
					if(i>0)
					{
						fieldValue = fieldValue+"_"+finalObject.get(propertyName);
					}
					else
					{
						fieldValue = finalObject.getString(propertyName);
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
